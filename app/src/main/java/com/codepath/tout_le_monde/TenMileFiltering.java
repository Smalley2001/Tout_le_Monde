package com.codepath.tout_le_monde;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TenMileFiltering extends AppCompatActivity {

    private static final String TAG = "TenMileFiltering";

    private EventsAdapter adapter;
    private List<Event> allEvents;
    private RecyclerView rvEvents;

    private EditText tenMileSearch;
    private Place userLocation;
    private double userLatitude;
    private double userLongitude;
    private Button btnTenSubmit;
    private String userAddress;
    private HashMap<String, Double> map = new HashMap<String, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_mile_filtering);

        tenMileSearch = findViewById(R.id.tenMileSearch);
        btnTenSubmit = findViewById(R.id.tenButtonSubmit);

        rvEvents = findViewById(R.id.tenRecycler);

        // initialize the array that will hold events and create a EventsAdapter
        allEvents = new ArrayList<>();
        adapter = new EventsAdapter(this, allEvents);

        // set the adapter on the recycler view
        rvEvents.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        // query events from EventTimeline
        queryEvents();

        // Initialize places
        Places.initialize(getApplicationContext(), getString(R.string.GOOGLE_MAPS_API_KEY));

        // Set EditText nonfocusable
        tenMileSearch.setFocusable(false);
        tenMileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                        Place.Field.NAME, Place.Field.PHONE_NUMBER, Place.Field.ID,
                        Place.Field.PHOTO_METADATAS, Place.Field.WEBSITE_URI);

                // Populate autocomplete results on part of the screen
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(TenMileFiltering.this);

                startActivityForResult(intent, 100);
            }
        });

        btnTenSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "my events size: " + allEvents.size());
//                queryEvents();
                filter();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            // When success
            // Initialize Place

            Place place = Autocomplete.getPlaceFromIntent(data);
            userLocation = place;
            tenMileSearch.setText(place.getAddress());
            userLatitude = Objects.requireNonNull(place.getLatLng()).latitude;
            userLongitude = Objects.requireNonNull(place.getLatLng()).longitude;

            Log.i(TAG, "User latitude is: " + userLatitude);
            Log.i(TAG, "User longitude is: " + userLongitude);

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // When error
            // Initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filter() {

        ArrayList<Event> events = new ArrayList<>();
        for (Event event: allEvents) {
            if (event.getLatitude() != 0.0 && event.getLongitude() != 0.0) {

                double eventLatitude = event.getLatitude();
                double eventLongitude = event.getLongitude();
                double distance = haversineDistance(userLatitude,userLongitude,eventLatitude,eventLongitude);
                double distanceInMiles = kmToMilesConverter(distance);

                if (isLessThan10MileDistance(distanceInMiles)) {
                    events.add(event);
                    Log.i(TAG, "Event name: " + event.getName() + " Address: " + event.getLocation());
                    Log.i(TAG, "Event Latitude: " + event.getLatitude());
                    Log.i(TAG, "Distance is: " + distanceInMiles);
                    map.put(event.getName(), distanceInMiles);
                }


            } else {
                continue;
            }
        }
//        queryEvents();
        insertionSort(events, 0, events.size()-1);
        Log.i(TAG, "my events size AFter filtering: " + events.size());
        Log.i(TAG, "New events lists after insertion sort: " + events.toString());
        adapter.filterList(events);
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

    private double kmToMilesConverter (double kmDistance) {

        double mileDistance = 0.621371 * kmDistance;

        return mileDistance;
    }

    private boolean isLessThan10MileDistance (double distance) {

        if (distance <= 10.0) {
            return true;
        } else {
            return false;
        }
    }

    private List<Event> insertionSort (List<Event> filteredEvents, int low, int high) {

        for (int i = low + 1; i <= high; i++) {

            for (int j = i -1; j >= low; j--) {

                String name1 = filteredEvents.get(j).getName();
                double distance1 = map.get(name1);
                String name2 = filteredEvents.get(j+1).getName();
                double distance2 = map.get(name2);

                if (distance1 == distance2) {

                    // If name 1 comes first alphabetically
                    if (name1.compareToIgnoreCase(name2) > 0) {

                        Event temp = filteredEvents.get(j);
                        filteredEvents.set(j, filteredEvents.get(j+1));
                        filteredEvents.set(j+1, temp);

                    }
                } else {
                    break;
                }

            }
        }
        return filteredEvents;
    }

    private void queryEvents() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        // include data referred by user key
        query.include(Event.KEY_HOST);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, com.parse.ParseException e) {

                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // save received posts to list and notify adapter of new data
                allEvents.addAll(events);
//                adapter.notifyDataSetChanged();

            }

        });
    }
}