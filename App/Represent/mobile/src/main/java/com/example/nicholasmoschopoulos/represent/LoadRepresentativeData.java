package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nicholasmoschopoulos on 2/25/16.
 */
public class LoadRepresentativeData {

    private RepresentativeListAdapter adapter;
    private List<HashMap<String, String>> mockData;
    private Context mContext;

    public LoadRepresentativeData(Context context, RepresentativeListAdapter adapter) {
        mContext = context;
        this.adapter = adapter;
    }

    public void loadData(String location) {
        mockData = fillMockData(); // replace with API calls
        adapter.updateEntries(mockData);
    }

    public static List<HashMap<String, String>> fillMockData() {
        List<HashMap<String, String>> mockData = new ArrayList<>();
        HashMap<String, String> m1 = new HashMap<>();
        HashMap<String, String> m2 = new HashMap<>();
        HashMap<String, String> m3 = new HashMap<>();
        // first rep
        m1.put("image", "barbara");
        m1.put("name", "Barbara Lee");
        m1.put("party", "democrat");
        m1.put("email", "barbara@lee.house.gov");
        m1.put("website", "http://lee.house.gov");
        m1.put("tweet", "#FBF to @FCC’s decision one year ago today that preserved #netneutrality and kept the Internet open, fair, & free for all.");
        m1.put("eot", "09/09/2014");
        m1.put("committees", "privacy,nothing else");
        m1.put("bills", "1,2,3,4,5,6,7,8");
        m1.put("state", "CA");
        m1.put("county", "Albany");
        m1.put("obama_votes", "88");
        m1.put("romney_votes", "12");
        // second rep
        m2.put("image", "eric");
        m2.put("name", "Eric Swalwell");
        m2.put("party", "democrat");
        m2.put("email", "eric@swalwell.house.gov");
        m2.put("website", "http://swalwell.house.gov");
        m2.put("tweet", "@Dee1music’s #SallieMaeBack is a song for our times – but it shouldn’t have to be. #FutureForum #debtfreecollege http://www.cnn.com/2016/02/24/living/sallie-mae-back-dee1-video-viral-feat/");
        m2.put("eot", "02/02/2098");
        m2.put("committees", "elderly,young,middle aged");
        m2.put("bills", "1,2,3,4,5,6,7,8");
        m2.put("state", "CA");
        m2.put("county", "Albany");
        m2.put("obama_votes", "88");
        m2.put("romney_votes", "12");
        // third rep
        m3.put("image", "honda");
        m3.put("name", "Mike Honda");
        m3.put("party", "democrat");
        m3.put("email", "mike@honda.house.gov");
        m3.put("website", "http://honda.house.gov");
        m3.put("tweet", "Great news, #CA17: 1st 250 miles of track of CA HS rail will go to San Jose. Now @VTA #BART & @CaHSRA will be in #SV http://www.mercurynews.com/california/ci_29529618/california-bullet-train-headed-first-san-jose-big");
        m3.put("eot", "01/01/01");
        m3.put("committees", "CS160,Jacobs Hall");
        m3.put("bills", "1,2,3,4,5,6,7,8");
        m3.put("state", "CA");
        m3.put("county", "Albany");
        m3.put("obama_votes", "88");
        m3.put("romney_votes", "12");

        mockData.add(m1);
        mockData.add(m2);
        mockData.add(m3);
        return mockData;
    }

    public int[] getRepresentativeImageIds(){
        int[] imageIds = new int[mockData.size()];
        for (int i=0; i<mockData.size(); i++) {
            String imageName = mockData.get(i).get("image");
            int imageID = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
            imageIds[i] = imageID;
        }

        return imageIds;
    }

    public String[] getRepresentativeNames() {
        String[] repNames = new String[mockData.size()];
        for (int i=0; i<mockData.size(); i++) {
            repNames[i] = mockData.get(i).get("name");
        }
        return repNames;
    }

    public Bundle getRepresentativeDataWatch() {
        Bundle allRepsData = new Bundle();
        for (HashMap<String, String> h : mockData) {
            allRepsData.putBundle(h.get("name"), getHashMapAsBundle(h));
        }
        return allRepsData;
    }

    private Bundle getHashMapAsBundle(HashMap<String, String> h) {
        Bundle b = new Bundle();
        for (String key : h.keySet()) {
            b.putString(key, h.get(key));
        }
        return b;
    }
}
