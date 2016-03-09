package com.example.nicholasmoschopoulos.represent;

import android.os.AsyncTask;

import java.util.Map;

/**
 * Created by nicholasmoschopoulos on 3/8/16.
 */
public class RepresentativeAPICalls extends AsyncTask<Map<String, String>, Void, String> {

    private final String API_URL = "http://www.congress.api.sunlightfoundation.com/legislators/locate?%s&apikey=3a9f30a6dcea41829ffba906077f4b0f";
    private final String ZIP = "zip=%s";
    private final String GPS = "latitude=%s&longitude=%s";

    @Override
    protected void onPreExecute() {}

    @Override
    protected String doInBackground(Map<String, String>... allRepData) {

        return null;
    }

    @Override
    protected void onPostExecute(String response) {}

}
