package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

        int imageID = mContext.getResources().getIdentifier(repData.get("image"), "drawable", mContext.getPackageName());
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), imageID);
        repImage.setImageBitmap(getRoundedCornerBitmap(image));
        repName.setText(repData.get("name"));
        repEmail.setText(repData.get("email"));
        repWebsite.setText(repData.get("website"));
        repTweet.setText(repData.get("tweet"));

        int color = mContext.getResources().getIdentifier(repData.get("party"), "color", mContext.getPackageName());
        itemView.setBackgroundResource(color);

        return itemView;
    }

    public void updateEntries(List<HashMap<String, String>> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final int roundPx = 500; // change per image – set each image to a square (crop sides), then create circle based on dimensions. (240x240 gives 500 as perfect)

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
