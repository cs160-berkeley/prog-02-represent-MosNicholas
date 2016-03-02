package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;

/**
 * Created by nicholasmoschopoulos on 2/28/16.
 */
public class RepresentativesGridAdapter extends GridPagerAdapter {

    public final static String REP_ID = "com.represent.wear.REP_ID";

    private final static int NUM_ROWS = 2;
    private final Context mContext;
    private RepresentativeData[] repData;

    public RepresentativesGridAdapter(Context context) { mContext = context; }

    public void loadData(String[] names, Bundle data) {
        repData = new RepresentativeData[names.length];
        for (int i=0; i<names.length; i++) {
            Bundle rep = data.getBundle(names[i]);
            repData[i] = new RepresentativeData(rep);
        }
    }

    @Override
    public int getRowCount() { return NUM_ROWS; }

    @Override
    public int getColumnCount(int row) { return repData.length; }

    @Override
    public int getCurrentColumnForRow(int row, int col) { return col; }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, final int col) {
        final RepresentativeData rd = repData[col];
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (row == 0) {
            view = inflater.inflate(R.layout.representative_list_item, null);

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.image_holder_rl);
            BitmapFactory.decodeByteArray(rd.image, 0, rd.image.length);
            Bitmap image = BitmapFactory.decodeByteArray(rd.image, 0, rd.image.length);
            relativeLayout.setBackground(new BitmapDrawable(mContext.getResources(), image));

            TextView repName = (TextView) view.findViewById(R.id.rep_name);
            repName.setText(rd.name);
            if (rd.party.equals("democrat")) {
                repName.setBackgroundColor(mContext.getResources().getColor(R.color.democratFaded));
            } else {
                repName.setBackgroundColor(mContext.getResources().getColor(R.color.republicanFaded));
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), WatchToPhoneService.class);
                    intent.putExtra(RepresentativesGridAdapter.REP_ID, rd.name);
                    intent.putExtra(WatchToPhoneService.MESSAGE_KEY, WatchToPhoneService.REPRESENTATIVE_CHOSEN);
                    view.getContext().startService(intent);
                }
            });
        } else {
            view = inflater.inflate(R.layout.presidential_election_data, null);
            TextView state = (TextView) view.findViewById(R.id.state_pres_data);
            state.setText(rd.state);
            TextView county = (TextView) view.findViewById(R.id.county_pres_data);
            county.setText(rd.county);
            TextView obama = (TextView) view.findViewById(R.id.obama_votes);
            obama.setText(String.format("%% of vote for Obama: %s", rd.obamaVotes));
            TextView romney = (TextView) view.findViewById(R.id.romney_votes);
            romney.setText(String.format("%% of vote for Romney: %s", rd.romneyVotes));
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
}
