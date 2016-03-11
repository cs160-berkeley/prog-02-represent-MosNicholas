package com.example.nicholasmoschopoulos.represent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RepresentativeProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_profile);

        Intent intent = getIntent();
        Bundle representativeData = intent.getBundleExtra(RepresentativesList.INTENT_REP_DATA);
        Representative repData = Representative.fromBundle(representativeData);

        ImageView imageView = (ImageView) findViewById(R.id.representative_image);
        TextView name = (TextView) findViewById(R.id.representative_name);
        TextView party = (TextView) findViewById(R.id.representative_party);
        TextView eot = (TextView) findViewById(R.id.representative_eot);
        final TextView listOfBills = (TextView) findViewById(R.id.representative_bills_button);
        final View billsSubtitle = (View) findViewById(R.id.bill_underline);
        final TextView listOfCommittees = (TextView) findViewById(R.id.representative_committees_button);
        final View committeesSubtitle = (View) findViewById(R.id.committee_underline);
        final ListView listBills = (ListView) findViewById(R.id.list_bills);
        final ListView listCommittees = (ListView) findViewById(R.id.list_committees);

        byte[] byteImage = repData.getImage();
        if (byteImage != null && byteImage.length > 0) {
            Bitmap image = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            imageView.setImageBitmap(RepresentativeListAdapter.getRoundedCornerBitmap(image));
        }
        name.setText(repData.getRoleAndName());
        party.setText(repData.getParty().toString());
        eot.setText(String.format("In office until %s", getPrettyEndDate(repData.getEndOfTerm())));

        ArrayAdapter<String> billAdapter = new ArrayAdapter<>(this, R.layout.representative_bill_item, repData.getBills());
        listBills.setAdapter(billAdapter);

        ArrayAdapter<String> committeeAdapter = new ArrayAdapter<>(this, R.layout.representative_bill_item, repData.getCommittees());
        listCommittees.setAdapter(committeeAdapter);

        listOfBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfCommittees.setTypeface(null, Typeface.NORMAL);
                listOfBills.setTypeface(null, Typeface.BOLD);
                committeesSubtitle.setVisibility(View.INVISIBLE);
                billsSubtitle.setVisibility(View.VISIBLE);
                listBills.setVisibility(View.VISIBLE);
                listCommittees.setVisibility(View.GONE);
            }
        });

        listOfCommittees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfBills.setTypeface(null, Typeface.NORMAL);
                listOfCommittees.setTypeface(null, Typeface.BOLD);
                billsSubtitle.setVisibility(View.INVISIBLE);
                committeesSubtitle.setVisibility(View.VISIBLE);
                listCommittees.setVisibility(View.VISIBLE);
                listBills.setVisibility(View.GONE);
            }
        });
    }

    private String getPrettyEndDate(String endOfTerm) {
        String[] nums = endOfTerm.split("-");
        String prettyDate = "";
        switch (Integer.valueOf(nums[1])) {
            case 1:
                prettyDate = "Jan ";
                break;
            case 2:
                prettyDate = "Feb ";
                break;
            case 3:
                prettyDate = "Mar ";
                break;
            case 4:
                prettyDate = "Apr ";
                break;
            case 5:
                prettyDate = "May ";
                break;
            case 6:
                prettyDate = "Jun ";
                break;
            case 7:
                prettyDate = "Jul ";
                break;
            case 8:
                prettyDate = "Aug ";
                break;
            case 9:
                prettyDate = "Sep ";
                break;
            case 10:
                prettyDate = "Oct ";
                break;
            case 11:
                prettyDate = "Nov ";
                break;
            case 12:
                prettyDate = "Dec ";
                break;
        }
        return prettyDate + nums[0];
    }

}
