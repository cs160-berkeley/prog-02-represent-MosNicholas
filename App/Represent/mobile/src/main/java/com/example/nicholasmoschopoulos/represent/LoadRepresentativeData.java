package com.example.nicholasmoschopoulos.represent;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
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
        mockData = takeNRandom(fillMockData(), 3); // replace with API calls
        adapter.updateEntries(mockData);
    }

    public static List<HashMap<String, String>> fillMockData() {
        List<HashMap<String, String>> mockData = new ArrayList<>();
        HashMap<String, String> m = new HashMap<>();
        // first rep
        m.put("image", "barbara");
        m.put("name", "Barbara Lee");
        m.put("party", "democrat");
        m.put("email", "barbara@lee.house.gov");
        m.put("website", "http://lee.house.gov");
        m.put("tweet", "#FBF to @FCC’s decision one year ago today that preserved #netneutrality and kept the Internet open, fair, & free for all.");
        m.put("eot", "09/09/2014");
        m.put("committees", "privacy,nothing else");
        m.put("bills", "1,2,3,4,5,6,7,8");
        m.put("state", "CA");
        m.put("county", "Albany");
        m.put("obama_votes", "88");
        m.put("romney_votes", "12");
        mockData.add(m);
        m = new HashMap<>();
        // second rep
        m.put("image", "eric");
        m.put("name", "Eric Swalwell");
        m.put("party", "democrat");
        m.put("email", "eric@swalwell.house.gov");
        m.put("website", "http://swalwell.house.gov");
        m.put("tweet", "@Dee1music’s #SallieMaeBack is a song for our times – but it shouldn’t have to be. #FutureForum #debtfreecollege http://www.cnn.com/2016/02/24/living/sallie-mae-back-dee1-video-viral-feat/");
        m.put("eot", "02/02/2098");
        m.put("committees", "elderly,young,middle aged");
        m.put("bills", "1,2,3,4,5,6,7,8");
        m.put("state", "CA");
        m.put("county", "Albany");
        m.put("obama_votes", "88");
        m.put("romney_votes", "12");
        mockData.add(m);
        m = new HashMap<>();
        // third rep
        m.put("image", "honda");
        m.put("name", "Mike Honda");
        m.put("party", "democrat");
        m.put("email", "mike@honda.house.gov");
        m.put("website", "http://honda.house.gov");
        m.put("tweet", "Great news, #CA17: 1st 250 miles of track of CA HS rail will go to San Jose. Now @VTA #BART & @CaHSRA will be in #SV http://www.mercurynews.com/california/ci_29529618/california-bullet-train-headed-first-san-jose-big");
        m.put("eot", "01/01/01");
        m.put("committees", "CS160,Jacobs Hall");
        m.put("bills", "1,2,3,4,5,6,7,8");
        m.put("state", "CA");
        m.put("county", "Albany");
        m.put("obama_votes", "88");
        m.put("romney_votes", "12");
        mockData.add(m);
        m = new HashMap<>();
        // fourth Rep
        m.put("image", "honda");
        m.put("name", "Mitch McDonald");
        m.put("party", "republican");
        m.put("email", "mitch@mcdonald.house.gov");
        m.put("website", "http://mcdonald.house.gov");
        m.put("tweet", "CS160 is awesome");
        m.put("eot", "01/01/01");
        m.put("committees", "Berkeley,Campanile,Papers");
        m.put("bills", "8,7,6,5,6,7,8");
        m.put("state", "NM");
        m.put("county", "New Mexico");
        m.put("obama_votes", "60");
        m.put("romney_votes", "40");
        mockData.add(m);
        m = new HashMap<>();
        // fifth rep
        m.put("image", "paulos");
        m.put("name", "Eric Paulos");
        m.put("party", "republican");
        m.put("email", "eric@paulos.house.gov");
        m.put("website", "http://paulos.house.gov");
        m.put("tweet", "Congrats to Lioness Vibrator designed to Improve your Sexual Experiences - developed in the CITRIS Invention Lab");
        m.put("eot", "06/05/2016");
        m.put("committees", "Design,UC Berkeley,CS160,Teaching");
        m.put("bills", "Proj1,Proj2A,Proj2B,Proj2C");
        m.put("state", "CA");
        m.put("county", "Another county");
        m.put("obama_votes", "100");
        m.put("romney_votes", "0");
        mockData.add(m);
        m = new HashMap<>();

        return mockData;
    }

    public static List<HashMap<String, String>> getAllData() {
        return fillMockData();
    }

    public HashMap<String, String> getNthRepresentative(int n) {
        return mockData.get(n);
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

    private static <T> List<T> takeNRandom(List<T> l, int n) {
        List<T> lr = new ArrayList<>();
        Collections.shuffle(l);
        for (int i=0; i<n; i++) {
            lr.add(l.get(i));
        }
        return lr;
    }
}
