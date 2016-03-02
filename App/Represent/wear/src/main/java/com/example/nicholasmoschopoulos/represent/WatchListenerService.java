package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by joleary and noon and nicholasmoschopoulos
 * on 2/19/16 and 2/29/16 respectively at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {

    public final static String REP_DATA = "com.represent.REP_DATA";
    public final static String REP_NAMES = "com.represent.REP_NAMES";
    private final static String REP_DATA_PATH = "/rep_data_path";
    private final static String START_ACTIVITY_PATH = "/show_rep_list";
    private final int TIMEOUT_MS = 30;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();

        Log.d("T", "in WatchListenerService, got: " + messagePath);

        if (messagePath.equalsIgnoreCase(START_ACTIVITY_PATH)) {
            Log.d("T", "message received");
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("T", "dataChanged called");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();

            ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(TIMEOUT_MS, TimeUnit.SECONDS);

            if (!connectionResult.isSuccess()) {
                Log.e("FAIL", "Failed to connect to GoogleApiClient.");
                return;
            }

            Log.d("T", "Google Api loaded");
        }

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals(REP_DATA_PATH)) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                String[] repNames = dataMapItem.getDataMap().getStringArray(REP_NAMES);
                DataMap repData = dataMapItem.getDataMap().getDataMap(REP_DATA);

                Intent intent = new Intent(this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(REP_NAMES, repNames);
                intent.putExtra(REP_DATA, getBundleFromDataMap(repNames, repData));
                startActivity(intent);
            }
        }
    }

    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result = mGoogleApiClient.blockingConnect(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w("FAIL", "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

    private Bundle getBundleFromDataMap(String[] keys, DataMap data) {
        Bundle b = new Bundle();
        for (String k : keys) {
            Bundle b1 = new Bundle();
            DataMap d = data.getDataMap(k);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            Bitmap image = loadBitmapFromAsset(d.getAsset("image"));
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);

            b1.putString("name", d.getString("name"));
            b1.putString("party", d.getString("party"));
            b1.putString("state", d.getString("state"));
            b1.putString("county", d.getString("county"));
            b1.putString("obama_votes", d.getString("obama_votes"));
            b1.putString("romney_votes", d.getString("romney_votes"));
            b1.putByteArray("image", byteStream.toByteArray());
            b.putBundle(k, b1);
        }

        return b;
    }
}