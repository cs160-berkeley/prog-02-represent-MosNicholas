package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public final static String LOCATION = "com.represent.LOCATION";
    public final static String ZIP_OR_GPS = "com.represent.ZIP_OR_GPS";
    public final static String ZIP = "com.represent.ZIP";
    public final static String GPS = "com.represent.GPS";
    private final static int GPS_SELECTED = 0;
    private final static int ZIP_SELECTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = (ImageView) findViewById(R.id.location_pin);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendMessage(GPS_SELECTED);
            }
        });

        Button button = (Button) findViewById(R.id.zip_code_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(ZIP_SELECTED);
            }
        });
    }

    public void sendMessage(int zipOrGPS) {
        Intent intent = new Intent(this, RepresentativesList.class);

        if (zipOrGPS == ZIP_SELECTED) { // Do we need an error state, where invalid ZIP code selected?
            EditText editText = (EditText) findViewById(R.id.input_zip);
            String zipCode = editText.getText().toString();
            intent.putExtra(LOCATION, zipCode);
            intent.putExtra(ZIP_OR_GPS, ZIP);
        } else {
            intent.putExtra(LOCATION, ""); // To be updated with API code
            intent.putExtra(ZIP_OR_GPS, GPS);
        }

        startActivity(intent);

        Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        startService(sendIntent);
    }

}
