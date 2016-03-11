package com.example.nicholasmoschopoulos.represent;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static String LOCATION = "com.represent.LOCATION";
    public final static String ZIP_OR_GPS = "com.represent.ZIP_OR_GPS";
    public final static String COUNTY = "com.represent.COUNTY_NAME";
    public final static String STATE = "com.represent.STATE_NAME";
    public final static String COUNTRY = "com.represent.COUNTRY_NAME";
    public final static String ZIP = "com.represent.ZIP";
    public final static String GPS = "com.represent.GPS";
    private final static int GPS_SELECTED = 0;
    private final static int ZIP_SELECTED = 1;

    private final static String BAD_ZIP_TEXT = "Bad zip code";
    private final static String NO_LOCATION_TEXT = "Location not found";

    private static final String TWITTER_KEY = "UGMY2WlftKFtE64zLssqwGtMw";
    private static final String TWITTER_SECRET = "ktu2Zq3T1IztGgEO8AB2I8rXqQ9gEK9napk9Yt9Qda5hRlqwIu";

    private GoogleApiClient mGoogleApiClient;
    private GeoApiContext geoContext;
    private TwitterSession twitterSession;
    private TwitterAuthClient authClient;
    private Location mLastLocation;
    private String locationToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (geoContext == null) {
            geoContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_key));
        }

        authClient = new TwitterAuthClient();
        authClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
            }

            @Override
            public void failure(TwitterException e) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authClient.onActivityResult(requestCode, resultCode, data);
    }

    public void sendMessage(int zipOrGPS) {
        Intent intent = new Intent(this, RepresentativesList.class);

        if (zipOrGPS == ZIP_SELECTED) { // Do we need an error state, where invalid ZIP code selected?
            EditText editText = (EditText) findViewById(R.id.input_zip);
            locationToSearch = editText.getText().toString();
            intent.putExtra(ZIP_OR_GPS, ZIP);
        } else {
            if (mLastLocation != null) {
                locationToSearch = String.format("%f,%f", mLastLocation.getLatitude(), mLastLocation.getLongitude());
                intent.putExtra(ZIP_OR_GPS, GPS);
            }
        }

        Map<String, String> locationMeta = getLocationMetaData(locationToSearch);
        if (locationMeta == null || !isValidLocation(locationMeta.get(COUNTRY))) {
            if (zipOrGPS == ZIP_SELECTED) {
                showBadZipToast();
            } else { showFailedLocationToast(); }
            return;
        }

        intent.putExtra(COUNTY, locationMeta.get(COUNTY));
        intent.putExtra(STATE, locationMeta.get(STATE));
        intent.putExtra(LOCATION, locationToSearch);
        startActivity(intent);
    }

    private Map<String, String> getLocationMetaData(String location) {
        if (location == null || location.isEmpty() || location.length() < 5) { return null; }
        Map<String, String> geoMeta = new HashMap<>();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoContext, location).await();
            if (results != null && results.length > 0) {
                GeocodingResult result = results[0];
                for (AddressComponent a : result.addressComponents) {
                    for (AddressComponentType act : a.types) {
                        String aType = act.toString().toLowerCase();
                        switch (aType) {
                            case "administrative_area_level_1" :
                                geoMeta.put(STATE, a.shortName);
                                break;
                            case "administrative_area_level_2" :
                                geoMeta.put(COUNTY, a.shortName);
                                break;
                            case "country" :
                                geoMeta.put(COUNTRY, a.shortName);
                                break;
                        }
                    }
                }

                return geoMeta;
            }
        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    // Returns whether the given location is valid (i.e. in the US).
    // Will update when I get the APIs
    private boolean isValidLocation(String country) {
        return country != null && (country.equalsIgnoreCase("us") || country.equalsIgnoreCase("usa"));
    }

    private void showFailedLocationToast() {
        showFailToast(NO_LOCATION_TEXT);
    }

    private void showBadZipToast() { showFailToast(BAD_ZIP_TEXT); }

    private void showFailToast(String toastText) {
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Main", "Connected Location App");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
