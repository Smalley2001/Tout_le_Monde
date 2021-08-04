package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DistanceActivity extends AppCompatActivity {

    private Button btnTen;
    private Button btnTwentyFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        btnTen = findViewById(R.id.btnTen);
        btnTwentyFive = findViewById(R.id.btnTwentyFive);

        btnTwentyFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTwentyFiveMileFiltering();

            }
        });

        btnTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTenMileFiltering();
            }
        });
    }

    private void goTenMileFiltering() {

        Intent i = new Intent(DistanceActivity.this, TenMileFiltering.class);
        startActivity(i);
    }

    private void goTwentyFiveMileFiltering() {

        Intent i = new Intent(DistanceActivity.this, TwentyFiveMileFiltering.class);
        startActivity(i);
    }
}