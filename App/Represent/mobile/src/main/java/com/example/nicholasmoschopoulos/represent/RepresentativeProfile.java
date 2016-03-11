package com.example.nicholasmoschopoulos.represent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        TextView eot = (TextView) findViewById(R.id.representative_eot);

        byte[] byteImage = repData.getImage();
        if (byteImage != null && byteImage.length > 0) {
            Bitmap image = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            imageView.setImageBitmap(RepresentativeListAdapter.getRoundedCornerBitmap(image));
        }
        name.setText(repData.getName());
        eot.setText(repData.getEndOfTerm());

        GridView gridView = (GridView) findViewById(R.id.grid_bills_committees);
        List<String> billsAndCommittees = concatLists(repData.getBills(), repData.getCommittees());
        ArrayAdapter<String> billAdapter = new ArrayAdapter<>(this, R.layout.representative_bill_item, billsAndCommittees);
        gridView.setAdapter(billAdapter);
    }

    private Representative getDataForRep(String repID) {
        LoadRepresentativeData a = new LoadRepresentativeData(null, null); // this will fail.
        return a.getRepresentativeByID(repID);
    }

    private List<String> concatLists(ArrayList<String> bills, ArrayList<String> committees) {
        List<String> combined = new ArrayList<>();
        combined.add("List of bills");
        combined.add("List of committees");
        Iterator<String> billIterator = bills.iterator();
        Iterator<String> committeeIterator = committees.iterator();
        while (billIterator.hasNext() && committeeIterator.hasNext()) {
            combined.add(billIterator.next());
            combined.add(committeeIterator.next());
        }
        while (billIterator.hasNext()) {
            combined.add(billIterator.next());
            combined.add("");
        }
        while (committeeIterator.hasNext()) {
            combined.add("");
            combined.add(committeeIterator.next());
        }

        return combined;
    }

}
