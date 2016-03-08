package com.example.nicholasmoschopoulos.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 * Edited by nicholasmoschopoulos also very late in the night
 */
public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private static final String REPRESENTATIVE_PATH = "/representative_chosen";
    private static final String WATCH_SHAKEN_PATH = "/watch_shaken";
    public static final String MESSAGE_KEY = "message_key";
    public static final String REPRESENTATIVE_CHOSEN = "representative";
    public static final String WATCH_SHAKEN = "shake_that";

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        mWatchApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("T", "in onconnected");
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                    }
                });
    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String messageToSend = intent.getStringExtra(MESSAGE_KEY);
        if (messageToSend.equals(REPRESENTATIVE_CHOSEN)) {
            final String repName = intent.getStringExtra(RepresentativesGridAdapter.REP_ID);
            sendMessage(REPRESENTATIVE_PATH, repName);
        } else if (messageToSend.equals(WATCH_SHAKEN)) {
            System.out.println("Sending watch shaken message");
            sendMessage(WATCH_SHAKEN_PATH, "");
        }

        return START_STICKY;
    }

    private void sendMessage(final String path, final String text) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }

}

