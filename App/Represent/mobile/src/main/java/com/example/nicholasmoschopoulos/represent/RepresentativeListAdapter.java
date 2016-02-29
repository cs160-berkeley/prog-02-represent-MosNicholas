package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nicholasmoschopoulos on 2/25/16.
 */
public class RepresentativeListAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<HashMap<String, String>> entries = new ArrayList<>();

    public RepresentativeListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        if (convertView == null) {
            itemView = (LinearLayout) mLayoutInflater.inflate(R.layout.representative_list_item, parent, false);
        } else {
            itemView = (LinearLayout) convertView;
        }

        ImageView repImage = (ImageView) itemView.findViewById(R.id.listImage);
        TextView repName = (TextView) itemView.findViewById(R.id.representative_name);
        TextView repEmail = (TextView) itemView.findViewById(R.id.representative_email);
        TextView repWebsite = (TextView) itemView.findViewById(R.id.representative_website);
        TextView repTweet = (TextView) itemView.findViewById(R.id.representative_tweet);

        HashMap<String, String> repData = getItem(position);

        int imageID = mContext.getResources().getIdentifier(repData.get("image") , "drawable", mContext.getPackageName());
        repImage.setImageResource(imageID);
        repName.setText(repData.get("name"));
        repEmail.setText(repData.get("email"));
        repWebsite.setText(repData.get("website"));
        repTweet.setText(repData.get("tweet"));

        int color = mContext.getResources().getIdentifier(repData.get("affiliation"), "color", mContext.getPackageName());
        itemView.setBackgroundResource(color);

        return itemView;
    }

    public void updateEntries(List<HashMap<String, String>> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }
}
