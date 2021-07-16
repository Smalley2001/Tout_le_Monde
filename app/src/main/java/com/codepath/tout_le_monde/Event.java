package com.codepath.tout_le_monde;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

// Refer to the entity in the Parse dashboard
@ParseClassName("Event")
public class Event extends ParseObject {

    //Don't forget to add a date attribute once we can save an event to Parse
    //look up more about the date object

    public static final String KEY_NAME = "Name";
    public static final String KEY_CAMPAIGN = "Campaign";
    public static final String KEY_DATE = "Date";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_MAX_PARTICIPANTS = "Max_Participants";
    public static final String KEY_HOST = "Host";
    public static final String KEY_START_TIME = "Start";
//    public static final String KEY_MERIDIEM = "Meridiem";
    public static final String KEY_END_TIME = "End";
    public static final String KEY_IMAGE = "Image";


    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) { put(KEY_NAME, name); }


    public String getDate() {
        return getString(KEY_DATE);
    }

    public void setDate(String date) { put(KEY_DATE, date); }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getHost() {
        return getParseUser(KEY_HOST);
    }

    public void setHost(ParseUser host) {
        put(KEY_HOST, host);
    }

    public String getCampaign() {
        return getString(KEY_CAMPAIGN);
    }

    public void setCampaign(String campaign) {
        put(KEY_CAMPAIGN, campaign);
    }


    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION, location);
    }


    public String getMaxParticipants() {
        return getString(KEY_MAX_PARTICIPANTS);
    }

    public void setMaxParticipants(String max_participants) {
        put(KEY_MAX_PARTICIPANTS, max_participants);
    }


    public String getStartTime() {
        return getString(KEY_START_TIME);
    }

    public void setStartTime(String time) {
        put(KEY_START_TIME, time);
    }


    public String getEndTime() {
        return getString(KEY_END_TIME);
    }

    public void setEndTime(String e_time) {
        put(KEY_END_TIME, e_time);
    }

//
//    public String getMeridiem() {
//        return getString(KEY_MERIDIEM);
//    }
//
//    public void setMeridiem(String meridiem) {
//        put(KEY_MERIDIEM, meridiem);
//    }

}
