package com.example.nicholasmoschopoulos.represent;

import android.os.AsyncTask;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicholasmoschopoulos on 3/9/16.
 */
public class LoadRepresentativeBills extends AsyncTask<List<String>, Void, Map<String, ArrayList<String>>> {

    private final static String BILLS_API_URL = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=%s&apikey=3a9f30a6dcea41829ffba906077f4b0f";

    private final LoadRepresentativeData loadRepresentativeData;

    public LoadRepresentativeBills(LoadRepresentativeData loadRepresentativeData) {
        this.loadRepresentativeData = loadRepresentativeData;
    }

    @Override
    protected void onPostExecute(Map<String, ArrayList<String>> repIDToBills) {
        super.onPostExecute(repIDToBills);
        loadRepresentativeData.setRepresentativesBills(repIDToBills);
    }

    @Override
    protected Map<String, ArrayList<String>> doInBackground(List<String>... representativeIDs) {
        Map<String, ArrayList<String>> repIDsToBills = new HashMap<>();
        for (String repID : representativeIDs[0]) {
            try {
                URL url = new URL(String.format(BILLS_API_URL, repID));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                String rawJSON = LoadRepresentativeData.inputStreamToString(urlConnection.getInputStream());
                repIDsToBills.put(repID, getBillsFromRawJSON(rawJSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return repIDsToBills;
    }

    private ArrayList<String> getBillsFromRawJSON(String rawJSON) throws JSONException {
        JSONObject jsonOutput = new JSONObject(rawJSON);
        JSONArray rawBillData = jsonOutput.getJSONArray("results");
        ArrayList<String> bills = new ArrayList<>();
        for (int i=0; i<rawBillData.length(); i++) {
            JSONObject jsonCommittee = (JSONObject) rawBillData.get(i);
            String billTitle = jsonCommittee.getString("short_title");
            billTitle = (!billTitle.equals("null") ? billTitle : jsonCommittee.getString("official_title"));
            bills.add(billTitle);
        }
        return bills;
    }
}
