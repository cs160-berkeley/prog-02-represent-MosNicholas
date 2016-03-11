package com.example.nicholasmoschopoulos.represent;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicholasmoschopoulos on 3/9/16.
 */
public class LoadRepresentativeCommittees extends AsyncTask<List<String>, Void, Map<String, ArrayList<String>>> {

    private final static String COMMITTEES_API_URL = "http://congress.api.sunlightfoundation.com/committees?member_ids=%s&apikey=3a9f30a6dcea41829ffba906077f4b0f";

    private final LoadRepresentativeData loadRepresentativeData;

    public LoadRepresentativeCommittees(LoadRepresentativeData loadRepresentativeData) {
        this.loadRepresentativeData = loadRepresentativeData;
    }

    @Override
    protected Map<String, ArrayList<String>> doInBackground(List<String>... representativeIDs) {
        Map<String, ArrayList<String>> repIDsToCommittees = new HashMap<>();
        for (String repID : representativeIDs[0]) {
            try {
                URL url = new URL(String.format(COMMITTEES_API_URL, repID));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                String rawJSON = LoadRepresentativeData.inputStreamToString(urlConnection.getInputStream());
                repIDsToCommittees.put(repID, getCommitteesFromRawJSON(rawJSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return repIDsToCommittees;
    }

    @Override
    protected void onPostExecute(Map<String, ArrayList<String>> repIDsToCommittees) {
        super.onPostExecute(repIDsToCommittees);
        loadRepresentativeData.setRepresentativesCommittees(repIDsToCommittees);
    }

    private ArrayList<String> getCommitteesFromRawJSON(String rawJSON) throws JSONException{
        JSONObject jsonOutput = new JSONObject(rawJSON);
        JSONArray rawCommitteeData = jsonOutput.getJSONArray("results");
        ArrayList<String> committees = new ArrayList<>();
        for (int i=0; i<rawCommitteeData.length(); i++) {
            JSONObject jsonCommittee = (JSONObject) rawCommitteeData.get(i);
            committees.add(jsonCommittee.getString("name"));
        }
        return committees;
    }
}
