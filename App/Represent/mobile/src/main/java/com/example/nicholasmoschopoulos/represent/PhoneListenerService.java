package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    private static final String REPRESENTATIVE_PATH = "/representative_chosen";
    private static final String WATCH_SHAKEN_PATH = "/watch_shaken";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();
        Log.d("T", "in PhoneListenerService, got: " + messagePath);

        Intent intent = new Intent();
        if (messagePath.equalsIgnoreCase(REPRESENTATIVE_PATH)) {
            intent = new Intent(this, RepresentativeProfile.class);
            String repName = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            intent.putExtra(RepresentativesList.REPRESENTATIVE_ID, repName);
        } else if (messagePath.equalsIgnoreCase(WATCH_SHAKEN_PATH)) {
            intent = new Intent(this, RepresentativesList.class);
            String location = getRandomCounty();
            intent.putExtra(MainActivity.LOCATION, location);
            intent.putExtra(MainActivity.ZIP_OR_GPS, MainActivity.GPS);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getRandomCounty() {
        try {
            InputStream stream = getAssets().open("counties.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);
            return jsonArray.getString(getRandomValue(jsonArray.length()));
        } catch (IOException | JSONException e) { e.printStackTrace(); }

        return null;
    }

    private int getRandomValue(int maxSize) {
        return new Random().nextInt(maxSize);
    }
}
