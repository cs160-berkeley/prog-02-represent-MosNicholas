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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joleary and noon and nicholasmoschopoulos
 * on 2/19/16 and 2/29/16 respectively at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {

    public final static String REP_DATA = "com.represent.REP_DATA";
    public final static String REP_IDS = "com.represent.REP_IDS";
    private final static String REP_DATA_PATH = "/rep_data_path";
    private final static String START_ACTIVITY_PATH = "/show_rep_list";

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
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals(REP_DATA_PATH)) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                ArrayList<String> repIDs = dataMapItem.getDataMap().getStringArrayList(REP_IDS);
                DataMap repData = dataMapItem.getDataMap().getDataMap(REP_DATA);

                if (repIDs == null) { return; }

                Intent intent = new Intent(this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(REP_IDS, repIDs);
                intent.putExtra(REP_DATA, repData.toBundle());
                startActivity(intent);
            }
        }
    }
}