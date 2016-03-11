package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nicholasmoschopoulos on 2/25/16.
 */
public class LoadRepresentativeData extends AsyncTask<Map<String, String>, Void, String> {

    private final static String API_URL = "http://congress.api.sunlightfoundation.com/legislators/locate?%s&apikey=3a9f30a6dcea41829ffba906077f4b0f";
    private final static String ZIP = "zip=%s";
    private final static String GPS = "latitude=%s&longitude=%s";
    private final static String ELECTION_DATA_FILENAME = "election-county-2012.json";

    private RepresentativeListAdapter adapter;
    private Context mContext;
    private String county, state;
    private Double obamaVotes = Double.valueOf(0), romneyVotes = Double.valueOf(0);

    private List<Representative> representatives;

    public LoadRepresentativeData(Context context, RepresentativeListAdapter adapter) {
        mContext = context;
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(Map<String, String>... locations) {
        Map<String, String> locData = locations[0];
        String apiData;
        if (locData.get(MainActivity.ZIP_OR_GPS).equalsIgnoreCase(MainActivity.ZIP)) {
            apiData = String.format(ZIP, locData.get(MainActivity.ZIP));
        } else if (locData.get(MainActivity.ZIP_OR_GPS).equalsIgnoreCase(MainActivity.GPS)) {
            apiData = String.format(GPS, locData.get(RepresentativesList.LAT), locData.get(RepresentativesList.LNG));
        } else {
            return null;
        }

        try {
            URL url = new URL(String.format(API_URL, apiData));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            return inputStreamToString(urlConnection.getInputStream());
        } catch (Exception e) { e.printStackTrace(); }

        return "";
    }

    @Override
    protected void onPostExecute(String rawJSON) {
        Log.d("LoadRepresentativeData", "onPostExecute called");
        try {
            representatives = createRepresentatives(rawJSON);
        } catch (JSONException e) { e.printStackTrace(); }
        adapter.updateEntries(representatives);

        List<String> repIDs = getRepresentativeIDs();
        LoadRepresentativeBills getBillData = new LoadRepresentativeBills(this);
        getBillData.execute(repIDs);
        LoadRepresentativeCommittees getCommitteeData = new LoadRepresentativeCommittees(this);
        getCommitteeData.execute(repIDs);

        LoadRepresentativeTwitterData getTwitterData = new LoadRepresentativeTwitterData(mContext, this);
        getTwitterData.execute(getRepresentativeTwitterHandles());

        super.onPostExecute(rawJSON);
    }

    public static String inputStreamToString(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(new BufferedInputStream(stream), "UTF-8");
        String s = "";
        int count = 0;
        char[] buffer = new char[4096];
        while ((count = reader.read(buffer)) > -1) {
            s += new String(buffer, 0, count);
        }
        stream.close();
        return s;
    }

    private List<Representative> createRepresentatives(String rawJSON) throws JSONException {
        JSONObject jsonOutput = new JSONObject(rawJSON);
        JSONArray rawRepresentativeData = jsonOutput.getJSONArray("results");
        List<Representative> representatives = new ArrayList<>();
        for (int i=0; i<rawRepresentativeData.length(); i++) {
            JSONObject jsonRepresentative = (JSONObject) rawRepresentativeData.get(i);
            Representative.Party party = jsonRepresentative.getString("party").equals("D") ? Representative.Party.Democrat : Representative.Party.Republican;
            Representative rep = new Representative(
                    jsonRepresentative.getString("first_name") + " " + jsonRepresentative.getString("last_name"),
                    county, state,
                    jsonRepresentative.getString("term_end"),
                    jsonRepresentative.getString("website"),
                    jsonRepresentative.getString("oc_email"),
                    jsonRepresentative.getString("twitter_id"),
                    party,
                    jsonRepresentative.getString("bioguide_id"),
                    jsonRepresentative.getString("title"),
                    obamaVotes, romneyVotes
            );
            representatives.add(rep);
        }

        return representatives;
    }

    public void setRepresentativesBills(Map<String, ArrayList<String>> repIDsToBills) {
        Log.d("LoadRepData", "setRepresentativesBills called");
        for (Representative representative : representatives) {
            representative.setBills(repIDsToBills.get(representative.getID()));
        }
    }

    public void setRepresentativesCommittees(Map<String, ArrayList<String>> repIDsToCommittees) {
        Log.d("LoadRepData", "setRepresentativesCommittees called");
        for (Representative representative : representatives) {
            representative.setCommittees(repIDsToCommittees.get(representative.getID()));
        }
    }

    public void setRepresentativesTweet(Map<String, String> handleToTweet) {
        Log.d("LoadRepData", "setRepresentativesTweets called");
        for (Representative representative : representatives) {
            representative.setTweet(handleToTweet.get(representative.getTwitterHandle()));
        }
        adapter.updateEntries(representatives);
    }

    public void setRepresentativesImages(Map<String, byte[]> handleToImage) {
        Log.d("LoadRepData", "setRepresentativesImages called");
        for (Representative representative : representatives) {
            representative.setImage(handleToImage.get(representative.getTwitterHandle()));
        }
        adapter.updateEntries(representatives);
    }

    public Representative getNthRepresentative(int n) {
        return representatives.get(n);
    }

    public Representative getRepresentativeByID(String id) {
        for (Representative representative : representatives) {
            if (representative.getID().equals(id)) {
                return representative;
            }
        }
        return null;
    }

    public ArrayList<byte[]> getRepresentativeImages(){
        ArrayList<byte[]> repImages = new ArrayList<>();
        for (Representative representative : representatives) {
            repImages.add(representative.getImage());
        }
        return repImages;
    }

    public ArrayList<String> getRepresentativeIDs() {
        ArrayList<String> repIds = new ArrayList<>();
        for (Representative representative : representatives) {
            repIds.add(representative.getID());
        }

        return repIds;
    }

    public ArrayList<String> getRepresentativeTwitterHandles() {
        ArrayList<String> twitterHandles = new ArrayList<>();
        for (Representative representative : representatives) {
            twitterHandles.add(representative.getTwitterHandle());
        }

        return twitterHandles;
    }

    public Bundle getRepresentativeDataWatch() {
        Bundle bundle = new Bundle();
        for (Representative representative : representatives) {
            bundle.putBundle(representative.getID(), representative.asBundle());
        }
        return bundle;
    }

    public int getNumReps() {
        return representatives.size();
    }

    public void setCountyState(String county, String state) {
        this.county = county;
        this.state = state;
        setPresidentialVotes();
    }

    public void setPresidentialVotes() {
        try {
            InputStream stream = mContext.getAssets().open(ELECTION_DATA_FILENAME);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);
            String countyTemp = county.replace(" County", "");
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("county-name").equals(countyTemp)
                        && jsonObject.getString("state-postal").equals(state)) {
                    obamaVotes = jsonObject.getDouble("obama-percentage");
                    romneyVotes = jsonObject.getDouble("romney-percentage");
                }
            }
        } catch (IOException | JSONException e) { e.printStackTrace(); }
    }
}
