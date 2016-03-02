package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

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
            String location = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            intent.putExtra(MainActivity.LOCATION, location);
            intent.putExtra(MainActivity.ZIP_OR_GPS, MainActivity.GPS);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
