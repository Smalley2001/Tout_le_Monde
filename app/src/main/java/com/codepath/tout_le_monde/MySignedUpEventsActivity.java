package com.codepath.tout_le_monde;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MySignedUpEventsActivity extends EventTimelineActivity {

    private RecyclerView rvMyEvents;
    private EventsAdapter myadapter;
    private List<Event> myallEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_signed_up_events);

        rvMyEvents = findViewById(R.id.rvMyEvents);

        // initialize the array that will hold events and create a EventsAdapter
        myallEvents = new ArrayList<>();
        myadapter = new EventsAdapter(this, myallEvents);

        // set the adapter on the recycler view
        rvMyEvents.setAdapter(myadapter);
        // set the layout manager on the recycler view
        rvMyEvents.setLayoutManager(new LinearLayoutManager(this));
        // query events from EventTimeline
        queryEvents();
    }

    private void queryEvents() {

        // specify what type of data we want to query - Event.class
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        // include data referred by user key
        query.include(Event.KEY_PARTICIPANTS);
        query.whereEqualTo(Event.KEY_PARTICIPANTS, ParseUser.getCurrentUser());

        query.setLimit(50);
        // order events by creation date (newest first)
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Event event : events) {
                    Log.i(TAG, "Apples: " + event.getDescription());
                }

                // save received events to list and notify adapter of new data
                myallEvents.addAll(events);
                myadapter.notifyDataSetChanged();
            }
        });

    }
}