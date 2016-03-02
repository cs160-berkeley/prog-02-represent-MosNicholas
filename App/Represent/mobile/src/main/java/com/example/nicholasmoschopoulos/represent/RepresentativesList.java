package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class RepresentativesList extends AppCompatActivity {

    // Will be changed with API calls
    public final static String REPRESENTATIVE_ID = "com.represent.REPRESENTATIVE_ID";
    public final static String INTENT_REP_IMAGES = "com.represent.INTENT_REP_IMAGES";
    public final static String INTENT_REP_NAMES = "com.represent.INTENT_REP_NAMES";
    public final static String INTENT_REP_DATA = "com.represent.INTENT_REP_DATA";

    private RepresentativeListAdapter adapter;
    private ListView list;
    private LoadRepresentativeData representativeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives_list);

        Intent intent = getIntent();
        String location = intent.getStringExtra(MainActivity.LOCATION);
        String zipOrGPS = intent.getStringExtra(MainActivity.ZIP_OR_GPS);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.title_bar);
        setSupportActionBar(myToolbar);
        if (zipOrGPS.equals(MainActivity.ZIP)) {
            setTitle("Zip code: " + location);
        } else {
            setTitle("Current location: " + location);
        }

        adapter = new RepresentativeListAdapter(this);
        list = (ListView) findViewById(R.id.representative_list);
        list.setAdapter(adapter);
        representativeData = new LoadRepresentativeData(this, adapter);
        representativeData.loadData(location);

        startWatchActivity();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                representativeClicked(view, position);
            }
        });
    }

    private void startWatchActivity() {
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra(INTENT_REP_IMAGES, representativeData.getRepresentativeImageIds());
        sendIntent.putExtra(INTENT_REP_NAMES, representativeData.getRepresentativeNames()); // Replace with Rep ids later w/ API calls?
        sendIntent.putExtra(INTENT_REP_DATA, representativeData.getRepresentativeDataWatch());
        startService(sendIntent);
    }

    private void representativeClicked(View view, int position) {
        Intent intent = new Intent(this, RepresentativeProfile.class);
        intent.putExtra(REPRESENTATIVE_ID, position); // To be changed with API calls. Use a unique rep ID?
        startActivity(intent);
    }
}
