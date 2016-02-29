package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nicholasmoschopoulos on 2/28/16.
 */
public class RepresentativesGridAdapter extends GridPagerAdapter {

    private final static int NUM_ROWS = 2;
    private final Context mContext;
    private RepresentativeData[] repData = new RepresentativeData[]{};

    public RepresentativesGridAdapter(Context context) { mContext = context; }

    @Override
    public int getRowCount() { return NUM_ROWS; }

    @Override
    public int getColumnCount(int row) { return repData.length; }

    @Override
    public int getCurrentColumnForRow(int row, int col) { return col; }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, int col) {
        RepresentativeData rd = repData[col];
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        View view;

        if (row == 0) {
            view = inflater.inflate(R.layout.representative_list_item, viewGroup);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.image_holder_rl);
            relativeLayout.setBackgroundResource(rd.drawableResId);
            TextView repName = (TextView) view.findViewById(R.id.rep_name);
            repName.setText(rd.name);
            repName.setBackgroundColor(mContext.getResources().getColor(R.color.democratFaded));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Launch profile on phone
                }
            });
        } else {
            view = inflater.inflate(R.layout.presidential_election_data, viewGroup);
            TextView state = (TextView) view.findViewById(R.id.state_pres_data);
            state.setText(rd.state);
            TextView county = (TextView) view.findViewById(R.id.county_pres_data);
            county.setText(rd.county);
            TextView obama = (TextView) view.findViewById(R.id.obama_votes);
            obama.setText(String.format("%% of vote for Obama: %d", rd.obamaVotes));
            TextView romney = (TextView) view.findViewById(R.id.romney_votes);
            romney.setText(String.format("%% of vote for Romney: %d", rd.romneyVotes));
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
