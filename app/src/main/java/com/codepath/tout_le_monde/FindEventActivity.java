package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindEventActivity extends AppCompatActivity {

    private Button btnFindEventCampaign;
    private Button btnFindEventLocation;
    private Button btnFindAllEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_event);

        btnFindEventCampaign = findViewById(R.id.btnFindEventCampaign);
        btnFindEventLocation = findViewById(R.id.btnFindEventLocation);
        btnFindAllEvents = findViewById(R.id.btnFindAllEvents);

        btnFindEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goDistanceActivity();

            }
        });

        btnFindAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnFindEventCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goFindEventCampaignActivity();

            }
        });
    }

    private void goFindEventCampaignActivity() {

        Intent i = new Intent(FindEventActivity.this, FindEventCampaignActivity.class);
        startActivity(i);
        //finish();
    }

    private void goDistanceActivity() {

        Intent i = new Intent(FindEventActivity.this, DistanceActivity.class);
        startActivity(i);
        //finish();
    }
}