package com.example.nicholasmoschopoulos.represent;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by nicholasmoschopoulos on 3/9/16.
 */
public class TwitterUserAPIClient extends TwitterApiClient {
    public TwitterUserAPIClient(Session session) {
        super(session);
    }

    public UserService getUserService() {
        return getService(UserService.class);
    }
}

interface UserService {
    @GET("/1.1/users/show.json")
    void show(@Query("screen_name") String twitterHandle, Callback<User> cb);

    @GET("/1.1/statuses/user_timeline.json")
    void userTimeline(@Query("screen_name") String twitterHandle, @Query("count") Integer count, @Query("exclude_replies") Boolean excludeReplies, @Query("include_rts") Boolean includeRetweets, Callback<List<Tweet>> var10);
}
