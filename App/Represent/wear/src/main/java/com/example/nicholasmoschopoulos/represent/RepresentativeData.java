package com.example.nicholasmoschopoulos.represent;

import android.os.Bundle;

/**
 * Created by nicholasmoschopoulos on 2/28/16.
 */
public class RepresentativeData {
    String name, party, state, county, obamaVotes, romneyVotes;
    byte[] image;

    RepresentativeData(String s, String p, String st, String c, String o, String r, byte[] i) {
        name = s;
        party = p;
        state = st;
        county = c;
        obamaVotes = o;
        romneyVotes = r;
        image = i;
    }

    RepresentativeData(Bundle bundle) {
        name = bundle.getString("name");
        party = bundle.getString("party");
        state = bundle.getString("state");
        county = bundle.getString("county");
        obamaVotes = bundle.getString("obama_votes");
        romneyVotes = bundle.getString("romney_votes");
        image = bundle.getByteArray("image");
    }
}
