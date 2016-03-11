package com.example.nicholasmoschopoulos.represent;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by nicholasmoschopoulos on 3/8/16.
 */
public class Representative {
    private final String name, county, state, endOfTerm, website, email, twitterHandle, ID, role;
    private String tweet;
    private Double obamaVotes = Double.valueOf(0), romneyVotes = Double.valueOf(0);
    private final Party party;
    private ArrayList<String> committees, bills;
    private byte[] image = null;

    public enum Party {
        Democrat {
            @Override
            public String toString() {
                return "democrat";
            }
        },
        Republican {
            @Override
            public String toString() {
                return "republican";
            }
        }
    }

    public Representative(String name, String county, String state, String endOfTerm, String website, String email, String twitterHandle, Party party, String ID, String role, Double obamaVotes, Double romneyVotes) {
        this.name = name;
        this.county = county;
        this.state = state;
        this.endOfTerm = endOfTerm;
        this.website = website;
        this.email = email;
        this.twitterHandle = twitterHandle;
        this.party = party;
        this.ID = ID;
        this.role = role;
        this.obamaVotes = obamaVotes;
        this.romneyVotes = romneyVotes;
    }

    public Representative(String name, String county, String state, String endOfTerm, String website, String email, String twitterHandle, Party party, String ID, String role, byte[] image, String tweet, Double obamaVotes, Double romneyVotes, ArrayList<String> bills, ArrayList<String> committees) {
        this.name = name;
        this.county = county;
        this.state = state;
        this.endOfTerm = endOfTerm;
        this.website = website;
        this.email = email;
        this.twitterHandle = twitterHandle;
        this.party = party;
        this.ID = ID;
        this.role = role;
        this.image = image;
        this.tweet = tweet;
        this.obamaVotes = obamaVotes;
        this.romneyVotes = romneyVotes;
        this.bills = bills;
        this.committees = committees;
    }

    public String getName() {
        return name;
    }

    public String getCounty() {
        return county;
    }

    public String getState() {
        return state;
    }

    public String getEndOfTerm() {
        return endOfTerm;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Double getObamaVotes() {
        return obamaVotes;
    }

    public Double getRomneyVotes() {
        return romneyVotes;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public ArrayList<String> getCommittees() {
        return committees;
    }

    public void setCommittees(ArrayList<String> committees) {
        this.committees = committees;
    }

    public ArrayList<String> getBills() {
        return bills;
    }

    public void setBills(ArrayList<String> bills) {
        this.bills = bills;
    }

    public Party getParty() {
        return party;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public String getRole() {
        return role;
    }

    public String getRoleAndName() {
        return String.format("%s %s", role, name);
    }

    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("ID", ID);
        bundle.putString("name", name);
        bundle.putString("role", role);
        bundle.putString("county", county);
        bundle.putString("state", state);
        bundle.putString("endOfTerm", endOfTerm);
        bundle.putString("website", website);
        bundle.putString("email", email);
        bundle.putString("tweet", tweet);
        bundle.putString("twitterHandle", twitterHandle);
        bundle.putString("party", party.toString());
        bundle.putDouble("obamaVotes", obamaVotes);
        bundle.putDouble("romneyVotes", romneyVotes);
        bundle.putStringArrayList("committees", committees);
        bundle.putStringArrayList("bills", bills);
        bundle.putByteArray("image", image);
        return bundle;
    }

    public static Representative fromBundle(Bundle representativeBundle) {
        String ID = representativeBundle.getString("ID");
        String name = representativeBundle.getString("name");
        String role = representativeBundle.getString("role");
        String county = representativeBundle.getString("county");
        String state = representativeBundle.getString("state");
        String endOfTerm = representativeBundle.getString("endOfTerm");
        String website = representativeBundle.getString("website");
        String email = representativeBundle.getString("email");
        String tweet = representativeBundle.getString("tweet");
        String twitterHandle = representativeBundle.getString("twitterHandle");
        Double romneyVotes = representativeBundle.getDouble("romneyVotes");
        Double obamaVotes = representativeBundle.getDouble("obamaVotes");
        String p = representativeBundle.getString("party");
        Party party = p.equals("democrat") ? Party.Democrat : Party.Republican;
        ArrayList<String> committees = representativeBundle.getStringArrayList("committees");
        ArrayList<String> bills = representativeBundle.getStringArrayList("bills");
        byte[] image = representativeBundle.getByteArray("image");
        return new Representative(name, county, state, endOfTerm, website, email, twitterHandle, party, ID, role, image, tweet, obamaVotes, romneyVotes, bills, committees);
    }
}