package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "CreateEventActivity";

     private EditText EtDescription;
     private EditText EtEventName;
     private EditText EtMaxParticipants;
     private TextView TvLocation;
     private Button BtnSubmit;
     private Spinner CampaignSpinner;
     private Spinner StartTimesSpinner;
    private Spinner EndTimesSpinner;
     private TextView MDisplayDate;
     private DatePickerDialog.OnDateSetListener MDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        EtEventName = findViewById(R.id.etEventName);
        EtDescription = findViewById(R.id.etDescription);
        EtMaxParticipants = findViewById(R.id.etMaxParticipants);
        TvLocation = findViewById(R.id.tvLocation);
        BtnSubmit = findViewById(R.id.btnSubmit);
        CampaignSpinner = findViewById(R.id.campaign_spinner);
        StartTimesSpinner = findViewById(R.id.start_times_spinner);
        EndTimesSpinner = findViewById(R.id.end_times_spinner);
        MDisplayDate = findViewById(R.id.tvDate);

        //Set up onclick listener for Date
        MDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        MDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        MDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                MDisplayDate.setText(date);
            }
        };

        // Set up Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.campaigns, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CampaignSpinner.setAdapter(adapter);
//
//        // Set listener for spinner
        CampaignSpinner.setOnItemSelectedListener(this);


        // Set up Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StartTimesSpinner.setAdapter(adapter2);
//
//        // Set listener for spinner
        StartTimesSpinner.setOnItemSelectedListener(this);


        // Set up Spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EndTimesSpinner.setAdapter(adapter3);
//
//        // Set listener for spinner
        EndTimesSpinner.setOnItemSelectedListener(this);

        // Grab location from google maps activity
        String location = getIntent().getStringExtra("Location");

        TvLocation.setText(location);

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = EtEventName.getText().toString();
                String campaign = CampaignSpinner.getSelectedItem().toString();
                String description = EtDescription.getText().toString();
                String location = TvLocation.getText().toString();
                String start = StartTimesSpinner.getSelectedItem().toString();
                String end = EndTimesSpinner.getSelectedItem().toString();
                String max_participants = EtMaxParticipants.getText().toString();
                String date = MDisplayDate.getText().toString();
                // Make event in Parse
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveEvent(name, campaign, date, location, max_participants, description, start, end, currentUser );
                goEventTimeLineActivity();
            }
        });

    }

    private void saveEvent(String name, String campaign, String date, String location, String max_participants, String description, String start, String end, ParseUser host) {

        Event event = new Event();
        event.setName(name);
        event.setCampaign(campaign);
        event.setDate(date);
        event.setDescription(description);
        event.setLocation(location);
        event.setStartTime(start);
        event.setEndTime(end);
        event.setMaxParticipants(max_participants);
        event.setHost(host);

        // Save to parse
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(CreateEventActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "Event creation was successful");
                    Toast.makeText(CreateEventActivity.this, "Event Created", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void goEventTimeLineActivity() {

        Intent i = new Intent(CreateEventActivity.this, EventTimelineActivity.class);
        startActivity(i);
    }
}