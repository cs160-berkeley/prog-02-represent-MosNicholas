package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class RepresentativesList extends AppCompatActivity {

    public final static String INTENT_REP_NAMES = "com.represent.INTENT_REP_NAMES";
    public final static String INTENT_REP_DATA = "com.represent.INTENT_REP_DATA";

    public final static String LAT = "com.represent.LATITUDE";
    public final static String LNG = "com.represent.LONGITUDE";

    private LoadRepresentativeData representativeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives_list);

        Intent intent = getIntent();
        String location = intent.getStringExtra(MainActivity.LOCATION);
        String county = intent.getStringExtra(MainActivity.COUNTY);
        String state = intent.getStringExtra(MainActivity.STATE);
        String zipOrGPS = intent.getStringExtra(MainActivity.ZIP_OR_GPS);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.title_bar);
        setSupportActionBar(myToolbar);

        Map<String, String> locationMetaData = new HashMap<>();
        locationMetaData.put(MainActivity.ZIP_OR_GPS, zipOrGPS);

        if (zipOrGPS.equals(MainActivity.ZIP)) {
            locationMetaData.put(MainActivity.ZIP, location);
            setTitle(String.format("Zip code: %s", location));
        } else {
            String[] latLng = location.split(",");
            locationMetaData.put(LAT, latLng[0]);
            locationMetaData.put(LNG, latLng[1]);
            setTitle(String.format("Current location: %s, %s", county, state));
        }

        RepresentativeListAdapter adapter = new RepresentativeListAdapter(this);
        ListView list = (ListView) findViewById(R.id.representative_list);
        list.setAdapter(adapter);

        representativeData = new LoadRepresentativeData(this, adapter);
        representativeData.setCountyState(county, state);
        representativeData.execute(locationMetaData);

//        adapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                startWatchActivity();
//            }
//
//            @Override
//            public void onInvalidated() {
//                super.onInvalidated();
//            }
//        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                representativeClicked(representativeData.getNthRepresentative(position));
            }
        });
    }

    private void startWatchActivity() {
        Log.d("RepresentativeList", "startWatchActivity called");
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra(INTENT_REP_NAMES, representativeData.getRepresentativeIDs());
        sendIntent.putExtra(INTENT_REP_DATA, representativeData.getRepresentativeDataWatch());
        startService(sendIntent);
    }

    private void representativeClicked(Representative representative) {
        Intent intent = new Intent(this, RepresentativeProfile.class);
        intent.putExtra(INTENT_REP_DATA, representative.asBundle());
        startActivity(intent);
    }
}
