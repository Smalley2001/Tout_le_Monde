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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "TestActivity";

    private Button btnSubmitEvent;
    private EditText etName;
    private TextView tvCampaign;
    private TextView tvLocation;
    private TextView tvDate;
    private Spinner startSpin;
    private Spinner endSpin;
    private EditText etMax;
    private EditText etDescription;

    private AwesomeValidation awesomeValidation;
    private DatePickerDialog.OnDateSetListener MDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnSubmitEvent = findViewById(R.id.btn_Submit_Event);
        etName = findViewById(R.id.et_name);
        tvCampaign = findViewById(R.id.tv_campaign);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        startSpin = findViewById(R.id.starting_spinner);
        endSpin = findViewById(R.id.ending_spinner);
        etMax = findViewById(R.id.et_max);
        etDescription = findViewById(R.id.et_description);

        // Initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.et_name, RegexTemplate.NOT_EMPTY,
                R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.et_max, RegexTemplate.NOT_EMPTY,
                R.string.invalid_max);

        awesomeValidation.addValidation(this,R.id.et_description, RegexTemplate.NOT_EMPTY,
                R.string.invalid_description);

        btnSubmitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {
                    // On Success
                    // Make event in Parse
                    String name = etName.getText().toString();
                    String campaign = tvCampaign.getText().toString();
                    String description = etDescription.getText().toString();
                    String location = tvLocation.getText().toString();
                    String start = startSpin.getSelectedItem().toString();
                    String end = endSpin.getSelectedItem().toString();
                    String max_participants = etMax.getText().toString();
                    String date = tvDate.getText().toString();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    saveEvent(name, campaign, date, location, max_participants, description, start, end, currentUser);
                    goEventTimeLineActivity();
                    Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Event Creation Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up onclick listener for Date
        tvDate.setOnClickListener(new View.OnClickListener() {
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
                tvDate.setText(date);
            }
        };

        // Set up Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpin.setAdapter(adapter2);
//
//        // Set listener for spinner
        startSpin.setOnItemSelectedListener(this);


        // Set up Spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpin.setAdapter(adapter3);
//
//        // Set listener for spinner
        endSpin.setOnItemSelectedListener(this);

        // Grab location from google maps activity
        String location = getIntent().getStringExtra("MapsLocation");
        String campaign = getIntent().getStringExtra("Campaign");
        Log.i(TAG, "  Received campaign: " + campaign);

        tvLocation.setText(location);
        tvCampaign.setText(campaign);

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

}