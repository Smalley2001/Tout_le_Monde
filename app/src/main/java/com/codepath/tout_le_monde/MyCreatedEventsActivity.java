package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyCreatedEventsActivity extends EventTimelineActivity {

    private RecyclerView rvMyEvents;
    private EventsAdapter myadapter;
    private List<Event> myallEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_events);

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
        query.include(Event.KEY_HOST);
        // limit query to latest 50 items
        query.whereEqualTo(Event.KEY_HOST, ParseUser.getCurrentUser());

        query.setLimit(50);
        // order events by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for events
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, com.parse.ParseException e) {

                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every event description to logcat
                for (Event event : events) {
                    Log.i(TAG, "Event: " + event.getDescription() + ", username: " + event.getHost().getUsername());
                }

                // save received events to list and notify adapter of new data
                myallEvents.addAll(events);
                myadapter.notifyDataSetChanged();

            }

        });
    }
}
