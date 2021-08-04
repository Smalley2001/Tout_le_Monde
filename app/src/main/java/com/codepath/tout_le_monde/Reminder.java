package com.codepath.tout_le_monde;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class Reminder extends BroadcastReceiver {

    private static final String TAG = "Reminder";
    private List<ParseUser> users;
    private List<Event> notSignedUpEvents = new ArrayList<>();
    private List<Event> eventsLessThan20Miles = new ArrayList<>();
    double userLat;
    double userLong;

    // I needed to called the alarm manager in the onreceive instead of doing the
    // alarm set Repeating because you can't set a repeating alarm for less than
    // one minute after API 19

    /**
     * Algo
     * Set alarm in MainActivity and send the user coordinates to this class
     * get the user coordinates and store them locally in onRecieve
     * query the Events and store in list
     * iterate through list and check if user signed up for event
     * if yes continue
     * if no, compute distance between user and event
     * if distance less than 20 miles, add to new list
     * if no, continue
     * Iterate through new list of events and call sendSMS function
     * After iteration, set new alarm with intent back to Broadcast Receiver (models recursive functionality)
     *
     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive is called");

        userLat = intent.getDoubleExtra("Latitude", 0.0);
        userLong = intent.getDoubleExtra("Longitude", 0.0);

        Log.i(TAG, "received lat is: " + userLat);
        Log.i(TAG, "received long is: " + userLong);


        queryNotSignedUpEvents();

    }

    private void sendSMS(String number, String message) {
        String phoneNumber = number;
        String SMS = message;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, SMS, null, null);
            Log.i(TAG, "Message is sent");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Message failed");
        }

    }


    private void queryNotSignedUpEvents() {

        // specify what type of data we want to query - Event.class
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        // include data referred by user key
        query.include(Event.KEY_PARTICIPANTS);
        query.whereNotEqualTo(Event.KEY_PARTICIPANTS, ParseUser.getCurrentUser());

        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                notSignedUpEvents.addAll(events);

                // Check which events are less than 20 miles away

                for (Event event: notSignedUpEvents) {

                    double distance = haversineDistance(userLat,userLong, event.getLatitude(), event.getLongitude());
                    double mileDistance = kmToMilesConverter(distance);

                    if (isLessThan20MileDistance(mileDistance)) {
                        eventsLessThan20Miles.add(event);
                    }
                }

                for (Event event: eventsLessThan20Miles) {
                    Log.i(TAG, "Event less than 20 miles away: " + event.getLocation());
                }

                // Send SMS messages for the event's nearby

                for (Event mEvent: eventsLessThan20Miles) {

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String username = currentUser.getUsername();
                    String userNumber = currentUser.getString("PhoneNumber");
                    Log.i(TAG, "User's Phone Number is: " + userNumber);
                    String eventName = mEvent.getName();
                    String eventLocation = mEvent.getLocation();
                    String eventDate = mEvent.getDate();

                    String message = "Hi " + username + ". There is an event called: "
                            + eventName + " that is located at: " + eventLocation +
                            " on " + eventDate;

                    sendSMS(userNumber, message);
                    break;

                }

            }
        });

    }


    private double haversineDistance (double userlat, double userlong, double eventlat, double eventlong) {

        // distance between latitudes and longitudes
        double dLat = Math.toRadians(eventlat - userlat);
        double dLon = Math.toRadians(eventlong - userlong);

        // convert to radians
        double radUserLat = Math.toRadians(userlat);
        double radEventLat = Math.toRadians(eventlat);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(radUserLat) *
                        Math.cos(radEventLat);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }


    private boolean isLessThan20MileDistance (double distance) {

        if (distance <= 20.0) {
            return true;
        } else {
            return false;
        }
    }

    private double kmToMilesConverter (double kmDistance) {

        double mileDistance = 0.621371 * kmDistance;

        return mileDistance;
    }


    private double convertHoursToMilliseconds (double hours) {

        double minutes = hours * 60;
        double seconds = minutes * 60;
        double milliseconds = seconds * 1000;
        return milliseconds;

    }

    private double convertMinutesToMilliseconds (double minutes) {

        double seconds = minutes * 60;
        double milliseconds = seconds * 1000;
        return milliseconds;

    }

    private void contactUser(ParseUser user) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", user.getObjectId());
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {

                if (e != null) {
                    Log.i(TAG, "Could not find User");
                } else {
                    String phoneNumber = object.getString("PhoneNumber");
                    Log.i(TAG, "Username is: " + object.getUsername() + " Phone Number: " + phoneNumber);
                }
            }
        });

    }

    private void queryUsers() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (ParseUser object: objects) {
                    users = objects;
                    Log.i(TAG, "Username is: " + object.getUsername());
                }
            }
        });
    }

}
