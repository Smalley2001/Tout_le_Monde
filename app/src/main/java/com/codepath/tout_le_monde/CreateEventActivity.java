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

    //Constants
    private static final String TAG = "CreateEventActivity";

    //Widgets
     private EditText etDescription;
//     private EditText etDate;
     private EditText etEventName;
//     private EditText etCampaign;
     private EditText etMaxParticipants;
     private TextView tvLocation;
//     private EditText etStart;
//     private EditText etEnd;
//     private EditText etMeridiem;
     private Button btnSubmit;
     private Spinner campaign_spinner;
     private Spinner start_times_spinner;
    private Spinner end_times_spinner;
     private TextView mDisplayDate;
     private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etEventName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etDescription);
//        etDate = findViewById(R.id.etDate);
//        etCampaign = findViewById(R.id.etCampaign);
        etMaxParticipants = findViewById(R.id.etMaxParticipants);
        tvLocation = findViewById(R.id.tvLocation);
//        etStart = findViewById(R.id.etStart);
//        etEnd = findViewById(R.id.etEnd);
//        etMeridiem = findViewById(R.id.etMeridiem);
        btnSubmit = findViewById(R.id.btnSubmit);
        campaign_spinner = findViewById(R.id.campaign_spinner);
        start_times_spinner = findViewById(R.id.start_times_spinner);
        end_times_spinner = findViewById(R.id.end_times_spinner);
        mDisplayDate = findViewById(R.id.tvDate);

        //Set up onclick listener for Date
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        //Set up Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.campaigns, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campaign_spinner.setAdapter(adapter);
//
//        //Set listener for spinner
        campaign_spinner.setOnItemSelectedListener(this);


        //Set up Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_times_spinner.setAdapter(adapter2);
//
//        //Set listener for spinner
        start_times_spinner.setOnItemSelectedListener(this);


        //Set up Spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        end_times_spinner.setAdapter(adapter3);
//
//        //Set listener for spinner
        end_times_spinner.setOnItemSelectedListener(this);

        //Grab location from google maps activity
        String location = getIntent().getStringExtra("Location");

        tvLocation.setText(location);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get data and convert to appropriate data type
                String name = etEventName.getText().toString();
                String campaign = campaign_spinner.getSelectedItem().toString();
                String description = etDescription.getText().toString();
                String location = tvLocation.getText().toString();
                String start = start_times_spinner.getSelectedItem().toString();
                String end = end_times_spinner.getSelectedItem().toString();
//                String start = etStart.getText().toString();
//                String end = etEnd.getText().toString();
//                String meridiem = etMeridiem.getText().toString();
                String max_participants = etMaxParticipants.getText().toString();
                String date = mDisplayDate.getText().toString();
                //Make event in Parse
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

        //save to parse
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    //This means we got an error
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(CreateEventActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }

                else {
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
        //finish();
    }
}