package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

public class CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

     private EditText etDescription;
     private EditText etDate;
     private EditText etEventName;
     private EditText etCampaign;
     private EditText etMaxParticipants;
     private TextView tvLocation;
     private Button btnSubmit;
     private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etEventName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etCampaign = findViewById(R.id.etCampaign);
        etMaxParticipants = findViewById(R.id.etMaxParticipants);
        tvLocation = findViewById(R.id.tvLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        spinner = findViewById(R.id.spin);

        //Set up Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.campaigns, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//
//        //Set listener for spinner
        spinner.setOnItemSelectedListener(this);

        //Grab location from google maps activity
        String location = getIntent().getStringExtra("Location");

        tvLocation.setText(location);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make event in Parse
                //parseCreateEvent();
            }
        });

    }

    private void parseCreateEvent() {

        String event_name = etEventName.getText().toString();
        String event_campaign = etCampaign.getText().toString();
        String location = tvLocation.getText().toString();
        String description = etDescription.getText().toString();
        String max_participants = etMaxParticipants.getText().toString();
        int max_people = 0;

        if (max_participants != "N/A") {

            max_people = Integer.parseInt(max_participants);

        }

        ParseObject new_Event = new ParseObject("Event");
        new_Event.put("Name", event_name);
        new_Event.put("Campaign", event_campaign);
        new_Event.put("Location", location);
        new_Event.put("Description", description);
        new_Event.put("Max_Participants", max_people);

        //Save new event
        new_Event.saveInBackground();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}