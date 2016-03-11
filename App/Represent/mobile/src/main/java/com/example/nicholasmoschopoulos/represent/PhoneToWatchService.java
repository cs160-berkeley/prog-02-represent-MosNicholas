package com.example.nicholasmoschopoulos.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

/**
 * Created by joleary on 2/19/16, updated by nicholasmoschopoulos
 */
public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;
    private final static String REP_DATA = "com.represent.REP_DATA";
    private final static String REP_IDS = "com.represent.REP_IDS";
    private final static String REP_DATA_PATH = "/rep_data_path";
    private final static String START_ACTIVITY_PATH = "/show_rep_list";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {}

                    @Override
                    public void onConnectionSuspended(int cause) {}
                }).build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PhoneToWatchService", "onStartCommand called");

        final ArrayList<String> repIDs = (ArrayList<String>) intent.getSerializableExtra(RepresentativesList.INTENT_REP_NAMES);
        final Bundle repData = intent.getBundleExtra(RepresentativesList.INTENT_REP_DATA);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mApiClient.connect();
                sendRepresentativeData(repData, repIDs);
            }
        }).start();

        return START_STICKY;
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage(final String path, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mApiClient, node.getId(), path, text.getBytes()).await();
                    Log.d("sendMessage result", result.getStatus().toString());
                }
            }
        }).start();
    }

    private void sendRepresentativeData(Bundle repData, ArrayList<String> repIDs) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(REP_DATA_PATH);

        putDataMapReq.getDataMap().putStringArrayList(REP_IDS, repIDs);
        putDataMapReq.getDataMap().putDataMap(REP_DATA, DataMap.fromBundle(repData));
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Status result = dataItemResult.getStatus();
                Log.d("sendRepData result", result.toString());
                if (result.isSuccess()) {
                    sendMessage(START_ACTIVITY_PATH, "");
                }
            }
        });
    }
}
