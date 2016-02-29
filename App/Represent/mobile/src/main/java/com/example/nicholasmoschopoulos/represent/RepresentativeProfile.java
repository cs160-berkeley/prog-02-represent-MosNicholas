package com.example.nicholasmoschopoulos.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepresentativeProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_profile);

        Intent intent = getIntent();
        int repID = intent.getIntExtra(RepresentativesList.REPRESENTATIVE_ID, 0);

        HashMap<String, String> repData = getDataForRep(repID);

        ImageView image = (ImageView) findViewById(R.id.representative_image);
        TextView name = (TextView) findViewById(R.id.representative_name);
        TextView eot = (TextView) findViewById(R.id.representative_eot);

        int imageID = this.getResources().getIdentifier(repData.get("image") , "drawable", this.getPackageName());
        image.setImageResource(imageID);
        name.setText(repData.get("name"));
        eot.setText(repData.get("eot"));

        GridView gridView = (GridView) findViewById(R.id.grid_bills_committees);
        String[] billArray = repData.get("bills").split(",");
        String[] committeeArray = repData.get("committees").split(",");
        String[] combinedArrays = concatArrays(billArray, committeeArray);
        ArrayAdapter<String> billAdapter = new ArrayAdapter<>(this, R.layout.representative_bill_item, combinedArrays);
        gridView.setAdapter(billAdapter);
    }

    private HashMap<String, String> getDataForRep(int repID) {
        List<HashMap<String, String>> mockData = LoadRepresentativeData.fillMockData();
        return mockData.get(repID);
    }

    private String[] concatArrays(String[] bills, String[] committees) {
        int maxOfTwo = Math.max(bills.length, committees.length);
        String[] combined = new String[maxOfTwo * 2 + 2];
        int b = 0, c = 0, index = 0;
        combined[index++] = "List of bills";
        combined[index++] = "List of committees";
        for (int i=0; i < maxOfTwo; i++) {
            if (b < bills.length) {
                combined[index++] = bills[b];
                b++;
            } else {
                combined[index++] = "";
            }

            if (c < committees.length) {
                combined[index++] = committees[c];
                c++;
            } else {
                combined[index++] = "";
            }
        }

        return combined;
    }

}
