package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class EventTimelineActivity extends AppCompatActivity {

    public static final String TAG = "EventTimeineActivity";
    protected EventsAdapter adapter;
    protected List<Event> allEvents;
    private RecyclerView rvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_timeline);

        rvEvents = findViewById(R.id.rvEvents);

        // initialize the array that will hold events and create a EventsAdapter
        allEvents = new ArrayList<>();
        adapter = new EventsAdapter(this, allEvents);

        // set the adapter on the recycler view
        rvEvents.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parstagram
        queryEvents();
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

                // for debugging purposes let's print every post description to logcat
                for (Event event : events) {
                    Log.i(TAG, "Post: " + event.getDescription() + ", username: " + event.getHost().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allEvents.addAll(events);
                adapter.notifyDataSetChanged();

            }

        });
    }
}