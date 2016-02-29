package com.example.nicholasmoschopoulos.represent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

public class RepresentativesList extends AppCompatActivity {

    // Will be changed with API calls
    public final static String REPRESENTATIVE_ID = "com.represent.REPRESENTATIVE_ID";

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
        representativeData = new LoadRepresentativeData(adapter);
        representativeData.loadData(location);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                representativeClicked(view, position);
            }
        });
    }

    private void representativeClicked(View view, int position) {
        Intent intent = new Intent(this, RepresentativeProfile.class);
        intent.putExtra(REPRESENTATIVE_ID, position); // To be changed with API calls. Use a unique rep ID?
        startActivity(intent);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
