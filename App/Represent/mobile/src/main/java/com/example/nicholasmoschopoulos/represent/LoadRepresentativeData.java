package com.example.nicholasmoschopoulos.represent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nicholasmoschopoulos on 2/25/16.
 */
public class LoadRepresentativeData {

    private RepresentativeListAdapter adapter;

    public LoadRepresentativeData(RepresentativeListAdapter adapter) {
        this.adapter = adapter;
    }

    public void loadData(String location) {
        List<HashMap<String, String>> mockData = fillMockData(); // replace with API calls
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
        m1.put("affiliation", "democrat");
        m1.put("email", "barbara@lee.house.gov");
        m1.put("website", "http://lee.house.gov");
        m1.put("tweet", "#FBF to @FCC’s decision one year ago today that preserved #netneutrality and kept the Internet open, fair, & free for all.");
        m1.put("eot", "09/09/2014");
        m1.put("committees", "privacy,nothing else");
        m1.put("bills", "1,2,3,4,5,6,7,8");
        // second rep
        m2.put("image", "eric");
        m2.put("name", "Eric Swalwell");
        m2.put("affiliation", "democrat");
        m2.put("email", "eric@swalwell.house.gov");
        m2.put("website", "http://swalwell.house.gov");
        m2.put("tweet", "@Dee1music’s #SallieMaeBack is a song for our times – but it shouldn’t have to be. #FutureForum #debtfreecollege http://www.cnn.com/2016/02/24/living/sallie-mae-back-dee1-video-viral-feat/");
        m2.put("eot", "02/02/2098");
        m2.put("committees", "elderly,young,middle aged");
        m2.put("bills", "1,2,3,4,5,6,7,8");
        // third rep
        m3.put("image", "honda");
        m3.put("name", "Mike Honda");
        m3.put("affiliation", "democrat");
        m3.put("email", "mike@honda.house.gov");
        m3.put("website", "http://honda.house.gov");
        m3.put("tweet", "Great news, #CA17: 1st 250 miles of track of CA HS rail will go to San Jose. Now @VTA #BART & @CaHSRA will be in #SV http://www.mercurynews.com/california/ci_29529618/california-bullet-train-headed-first-san-jose-big");
        m3.put("eot", "01/01/01");
        m3.put("committees", "CS160,Jacobs Hall");
        m3.put("bills", "1,2,3,4,5,6,7,8");

        mockData.add(m1);
        mockData.add(m2);
        mockData.add(m3);
        return mockData;
    }
}
