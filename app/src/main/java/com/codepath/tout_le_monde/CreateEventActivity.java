package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateEventActivity extends AppCompatActivity {

     private EditText etDescription;
     private EditText etDate;
     private EditText etEventName;
     private EditText etCampaign;
     private EditText etMaxParticipants;
     private TextView tvLocation;
     private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etEventName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etEventName = findViewById(R.id.etMaxParticipants);
        tvLocation = findViewById(R.id.tvLocation);

        String location = getIntent().getStringExtra("Location");

        tvLocation.setText(location);

    }
}