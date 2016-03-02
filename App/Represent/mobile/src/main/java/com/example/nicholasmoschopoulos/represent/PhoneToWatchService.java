package com.example.nicholasmoschopoulos.represent;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;

/**
 * Created by joleary on 2/19/16, updated by nicholasmoschopoulos
 */
public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;
    private final static String REP_DATA = "com.represent.REP_DATA";
    private final static String REP_NAMES = "com.represent.REP_NAMES";
    private final static String REP_DATA_PATH = "/rep_data_path";
    private final static String START_ACTIVITY_PATH = "/show_rep_list";

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

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
        final int[] images = intent.getIntArrayExtra(RepresentativesList.INTENT_REP_IMAGES);
        final String[] names = intent.getStringArrayExtra(RepresentativesList.INTENT_REP_NAMES);
        final Bundle repData = intent.getBundleExtra(RepresentativesList.INTENT_REP_DATA);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mApiClient.connect();
                sendRepresentativeData(repData, names, images);
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

    private void sendRepresentativeData(Bundle repData, String[] keyNames, int[] imageIds) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(REP_DATA_PATH);

        DataMap repDataAsDataMap = DataMap.fromBundle(repData);
        for (int i=0; i<imageIds.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageIds[i]);
            Asset asset = createAssetFromBitmap(bitmap);
            repDataAsDataMap.getDataMap(keyNames[i]).putAsset("image", asset);
        }
        putDataMapReq.getDataMap().putDataMap(REP_DATA, repDataAsDataMap);
        putDataMapReq.getDataMap().putStringArray(REP_NAMES, keyNames);
        putDataMapReq.getDataMap().putLong("Time", System.currentTimeMillis());
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
