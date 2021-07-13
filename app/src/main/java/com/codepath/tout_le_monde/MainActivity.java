package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private Button btnGoMap;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoMap = findViewById(R.id.btnGoMap);
        btnLogout = findViewById(R.id.btnLogout);

        if (isServicesOK()) {
            goMapsActivity();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    public boolean isServicesOK() {

        //This function ensures that the User's client is compatible to
        //allow Google Play Services
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is Working");
            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it

            Log.d(TAG, "isServcesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }

        else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    private void goMapsActivity() {

        // This function redirects to the Google Map Activity
        // if button clicked

        btnGoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GoogleMapsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}