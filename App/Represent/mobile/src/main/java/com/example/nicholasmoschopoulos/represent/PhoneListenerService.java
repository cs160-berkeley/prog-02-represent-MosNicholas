package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    private static final String REPRESENTATIVE_PATH = "/representative_chosen";
    private static final String WATCH_SHAKEN_PATH = "/watch_shaken";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());

        Intent intent = new Intent(this, RepresentativesList.class);
        getRandomCounty(intent);
        intent.putExtra(MainActivity.ZIP_OR_GPS, MainActivity.GPS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("T", "dataChanged called");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals(REPRESENTATIVE_PATH)) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                DataMap repData = dataMapItem.getDataMap().getDataMap(RepresentativesList.INTENT_REP_DATA);

                Intent intent = new Intent(this, RepresentativeProfile.class);
                intent.putExtra(RepresentativesList.INTENT_REP_DATA, repData.toBundle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        }
    }

    private void getRandomCounty(Intent intent) {
        try {
            InputStream stream = getAssets().open("county-lat-longs.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject county = jsonArray.getJSONObject(getRandomValue(jsonArray.length()));
            intent.putExtra(MainActivity.LOCATION, String.format("%f,%f", county.getDouble("lat"), county.getDouble("lng")));
            intent.putExtra(MainActivity.COUNTY, county.getString("county"));
            intent.putExtra(MainActivity.STATE, county.getString("state"));
        } catch (IOException | JSONException e) { e.printStackTrace(); }
    }

    private int getRandomValue(int maxSize) {
        return new Random().nextInt(maxSize);
    }
}
