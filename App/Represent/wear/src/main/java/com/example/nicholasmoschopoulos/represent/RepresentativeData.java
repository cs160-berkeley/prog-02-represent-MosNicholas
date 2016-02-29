package com.example.nicholasmoschopoulos.represent;

/**
 * Created by nicholasmoschopoulos on 2/28/16.
 */
public class RepresentativeData {
    String name, party, state, county;
    Integer drawableResId, obamaVotes, romneyVotes;

    RepresentativeData(String s, String p, int dri, String st, String c, int o, int r) {
        name = s;
        party = p;
        drawableResId = dri;
        state = st;
        county = c;
        obamaVotes = o;
        romneyVotes = r;
    }
}
