package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

/**
 * Created by nicholasmoschopoulos on 2/28/16.
 */
public class RepresentativesGridAdapter extends GridPagerAdapter {

    public final static String INTENT_REP_DATA = "com.represent.INTENT_REP_DATA";

    private final static int NUM_ROWS = 2;
    private final Context mContext;
    private ArrayList<Representative> representatives;

    public RepresentativesGridAdapter(Context context) { mContext = context; }

    public void loadData(ArrayList<String> repIDs, Bundle data) {
        representatives = new ArrayList<>();
        for (String repID : repIDs) {
            Bundle rep = data.getBundle(repID);
            representatives.add(Representative.fromBundle(rep));
        }
    }

    @Override
    public int getRowCount() { return NUM_ROWS; }

    @Override
    public int getColumnCount(int row) { return representatives.size(); }

    @Override
    public int getCurrentColumnForRow(int row, int col) { return col; }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, final int col) {
        final Representative rd = representatives.get(col);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (row == 0) {
            view = inflater.inflate(R.layout.representative_list_item, null);

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.image_holder_rl);
            byte[] byteImage = rd.getImage();
            if (byteImage != null) {
                BitmapFactory.decodeByteArray(rd.getImage(), 0, byteImage.length);
                Bitmap image = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
                relativeLayout.setBackground(new BitmapDrawable(mContext.getResources(), image));
            }

            LinearLayout representativeTitle = (LinearLayout) view.findViewById(R.id.representative_title);
            TextView repName = (TextView) view.findViewById(R.id.rep_name);
            TextView repParty = (TextView) view.findViewById(R.id.rep_party);
            repName.setText(rd.getRoleAndName());
            repParty.setText(rd.getParty().toString());
            if (rd.getParty() == Representative.Party.Democrat) {
                representativeTitle.setBackgroundColor(mContext.getResources().getColor(R.color.democratFaded));
            } else {
                representativeTitle.setBackgroundColor(mContext.getResources().getColor(R.color.republicanFaded));
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), WatchToPhoneService.class);
                    intent.putExtra(INTENT_REP_DATA, rd.asBundle());
                    intent.putExtra(WatchToPhoneService.MESSAGE_KEY, WatchToPhoneService.REPRESENTATIVE_CHOSEN);
                    view.getContext().startService(intent);
                }
            });
        } else {
            view = inflater.inflate(R.layout.presidential_election_data, null);
            TextView county = (TextView) view.findViewById(R.id.county_pres_data);
            county.setText(String.format("%s, %s", rd.getCounty(), rd.getState()));
            PieChart pieChart = (PieChart) view.findViewById(R.id.vote_chart);
            createPieChart(pieChart, rd.getObamaVotes(), rd.getRomneyVotes());
        }

        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int row, int col, Object o) { viewGroup.removeView((View) o); }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

    private void createPieChart(PieChart pieChart, Double obamaVotes, Double romneyVotes) {
        pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(40f);
        pieChart.setRotationEnabled(false);
        pieChart.getLegend().setEnabled(false);

        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(obamaVotes.floatValue(), 0));
        yVals.add(new Entry(romneyVotes.floatValue(), 1));
        PieDataSet dataSet = new PieDataSet(yVals, "Election Results");
        dataSet.setSliceSpace(1.5f);
        dataSet.setColors(new int[]{R.color.democratFaded, R.color.republicanFaded}, mContext);

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Obama");
        xVals.add("Romney");

        PieData pieData = new PieData(xVals, dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(9f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}
