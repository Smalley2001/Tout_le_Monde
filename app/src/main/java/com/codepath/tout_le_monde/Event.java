package com.codepath.tout_le_monde;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

// Add Parcel library to make class serializable
@Parcel(analyze = Event.class)
// Refer to the entity in the Parse dashboard
@ParseClassName("Event")
public class Event extends ParseObject {

    // Access the names of the fields inside of the Parse object
    // and store them in variables

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_NAME = "Name";
    public static final String KEY_CAMPAIGN = "Campaign";
    public static final String KEY_DATE = "Date";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_MAX_PARTICIPANTS = "Max_Participants";
    public static final String KEY_HOST = "Host";
    public static final String KEY_START_TIME = "Start";
    public static final String KEY_END_TIME = "End";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_SIGNED_UP = "SignedUpUsers";
    public static final String KEY_PARTICIPANTS = "Participants";
    public static final String KEY_PARTICIPANTS_COUNT = "Participants_Count";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";

    // Initialize empty constructor for Parcel
    public Event() {

    }

    // Set getters and setters to retrieve from Parse or update Parse

    public String getId() {
        return getString(KEY_OBJECT_ID);
    }

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

    public String getHostUsername() throws ParseException {
        ParseUser user = getHost();
        String username = user.fetchIfNeeded().getUsername();
        return username;
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


    public double getLatitude() {
        return getDouble(KEY_LATITUDE);
    }

    public void setLatitude(double latitude) {
        put(KEY_LATITUDE, latitude);
    }

    public double getLongitude() {
        return getDouble(KEY_LONGITUDE);
    }

    public void setLongitude(double longitude) {
        put(KEY_LONGITUDE, longitude);
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

    public List<Object> getSignedUpUsers() {
        return getList(KEY_SIGNED_UP);
    }

    public ParseRelation<ParseUser> getParticipants() {
        return getRelation("Participants");
    }
    public int getParticipantsCount() { return getInt("Participants_Count"); }
    public void setParticipantsCount(int count) { put("Participants_Count", count); }
}
