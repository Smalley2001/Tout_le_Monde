package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    private Button BtnSignUp;
    private Event event;
    private TextView EventName;
    private TextView EventHost;
    private TextView EventDate;
    private TextView EventLocation;
    private TextView EventDescription;
    private TextView EventCampaign;
    private TextView EventStart;
    private TextView EventEnd;
    private TextView EventMax;
    private TextView EventAvailable;
    private ImageView EventPhoto;
    private static final String TAG = "EventDetailsActivity";
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        BtnSignUp = findViewById(R.id.btnSignUp);
        EventName = findViewById(R.id.details_event_name);
        EventHost = findViewById(R.id.details_event_host);
        EventLocation = findViewById(R.id.details_event_location);
        EventDescription = findViewById(R.id.details_event_description);
        EventCampaign = findViewById(R.id.details_event_campaign);
        EventStart = findViewById(R.id.details_event_start);
        EventEnd = findViewById(R.id.details_event_end);
        EventMax = findViewById(R.id.details_event_max);
        EventDate = findViewById(R.id.details_event_date);
        EventAvailable = findViewById(R.id.details_event_available);
        EventPhoto = findViewById(R.id.details_event_image);

        event = Parcels.unwrap(getIntent().getParcelableExtra("X"));
        user = Parcels.unwrap(getIntent().getParcelableExtra("U"));
        Log.d(TAG, String.format("Showing details for '%s'", event.getName()));
        Log.d(TAG, "Username is: " + user.getUsername());

        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<ParseUser> relation = event.getRelation("Participants");
                relation.add(user);
                int count = event.getParticipantsCount();
                count += 1;
                event.setParticipantsCount(count);
                event.saveInBackground();
                Toast.makeText(getApplicationContext(),"Sign Up Successful", Toast.LENGTH_SHORT).show();
                goMainActivity();

            }
        });
        int maximum_participants = getMaxPeople();
        String available_spots = calculateAvailableSpots(maximum_participants);
        try {
            bindFields(available_spots);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void goEventTimelineActivity() {

        Intent intent = new Intent(getApplicationContext(), EventTimelineActivity.class);
        startActivity(intent);
        finish();
    }

    private void goMainActivity() {

        Intent i = new Intent(EventDetailsActivity.this, MainActivity.class);
        startActivity(i);
    }

    private int getMaxPeople() {

        String max = event.getMaxParticipants();
        Log.i(TAG, "Max is: " + max);
        int max_people = -1;
        try {
            max_people = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            max_people = 0;
            Log.i(TAG, "Could not convert!");
        }
        return max_people;
    }

    private String calculateAvailableSpots(int max_people) {

        String available_spots = "";

        if (max_people == 0) {
            available_spots = "No Limit";
        } else {

            int users_signed_up = event.getParticipantsCount();
            int available = max_people - users_signed_up;
            available_spots = String.valueOf(available);

        }

        Log.i(TAG, "Available spots are: " + available_spots);
        return available_spots;
    }

    private void bindFields(String available_spots) throws ParseException {

        String name = event.getName();
        String campaign = "Event Campaign: " + event.getCampaign();
        String description = event.getDescription();
        String max = "Apples";

        if (event.getMaxParticipants() != null) {
            max = "Maximum Participants: " + event.getMaxParticipants();
            Log.i(TAG, "Maximum is: " + max);
        } else {
            max = "Maximum Participants: N/A";
        }

        Log.i(TAG, "The max is: " + max);

        String date = "Event Date: " + event.getDate();
        String location = "Event Location: " + event.getLocation();
        String start = "Start Time: " + event.getStartTime();
        String end = "End Time: " + event.getEndTime();
        String host = "Event Host: " + event.getHostUsername();
        String available = "Remaining spots: " + available_spots;
        ParseFile file = event.getImage();


        EventName.setText(name);
        EventLocation.setText(location);
        EventDescription.setText(description);
        EventMax.setText(max);
        EventDate.setText(date);
        EventCampaign.setText(campaign);
        EventStart.setText(start);
        EventEnd.setText(end);
        EventAvailable.setText(available);
        EventHost.setText(host);

        loadImages(file, EventPhoto);
    }


    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    } else {
                        Log.i(TAG, "Image was not set: " + e.getMessage());
                    }
                }
            });
        } else {
            Log.i(TAG, "File did not upload");
            img.setImageResource(R.drawable.placeholder_image);
        }
    }// load image
}