package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicholasmoschopoulos on 3/9/16.
 */
public class LoadRepresentativeTwitterData extends AsyncTask<Map<String, String>, Void, Map<String, byte[]>> {

    private final LoadRepresentativeData loadRepresentativeData;
    private TwitterSession session;

    private Map<String, String> handleToTweet = new HashMap<>();
    private Map<String, String> handleToImageURL = new HashMap<>();
    private int numTweets, numImages;
    private Context mContext;

    public LoadRepresentativeTwitterData(Context context, LoadRepresentativeData loadRepresentativeData) {
        this.loadRepresentativeData = loadRepresentativeData;
        mContext = context;
        numTweets = loadRepresentativeData.getNumReps();
        numImages = loadRepresentativeData.getNumReps();
        session = Twitter.getSessionManager().getActiveSession();
    }

    protected Boolean execute(List<String> twitterHandles) {
        TwitterUserAPIClient twitterUserAPIClient = new TwitterUserAPIClient(session);
        UserService userService = twitterUserAPIClient.getUserService();
        for (final String twitterHandle: twitterHandles) {
            userService.userTimeline(twitterHandle, 1, true, false, new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    Tweet lastTweet = result.data.get(0);
                    addTweetToTwitterHandle(twitterHandle, Html.fromHtml(lastTweet.text).toString());
                }

                @Override
                public void failure(TwitterException e) {}
            });

            userService.show(twitterHandle, new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    addImageURLToTwitterHandle(twitterHandle, result.data.profileImageUrl.replace("normal", "400x400"));
                }

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                }
            });
        }
        return true;
    }

    private void addTweetToTwitterHandle(String twitterHandle, String tweet) {
        handleToTweet.put(twitterHandle, tweet);
        numTweets--;
        if (numTweets == 0) {
            updateRepresentativeTweetData();
        }
    }

    private void addImageURLToTwitterHandle(String twitterHandle, String url) {
        handleToImageURL.put(twitterHandle, url);
        numImages--;
        if (numImages == 0) {
            execute(handleToImageURL);
        }
    }

    private void updateRepresentativeTweetData() {
        loadRepresentativeData.setRepresentativesTweet(handleToTweet);
    }

    @Override
    protected Map<String, byte[]> doInBackground(Map<String, String>... handleToImageURLs) {
        Map<String, String> handleToImageURL = handleToImageURLs[0];
        Map<String, byte[]> handleToImage = new HashMap<>();
        Picasso picasso = Picasso.with(mContext);
        for (String twitterHandle : handleToImageURL.keySet()) {
            Uri uri = Uri.parse(handleToImageURL.get(twitterHandle));
            try {
                Bitmap bitmap = picasso.load(uri).get();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteStream);
                handleToImage.put(twitterHandle, byteStream.toByteArray());
            } catch (IOException e) {}
        }
        return handleToImage;
    }

    @Override
    protected void onPostExecute(Map<String, byte[]> handleToImage) {
        super.onPostExecute(handleToImage);
        loadRepresentativeData.setRepresentativesImages(handleToImage);
    }
}
