package com.codepath.tout_le_monde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.tout_le_monde.unused.CameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    //Variables
    private Button btnLogout;
    private Button btnCreateEvent;
    private Button btnFindEvent;
    private Button btnMyCreatedEvents;
    private Button BtnMySignedUpEvents;
//    private Button BtnSMS;
    private TextView tvHelloUser;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean LocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHelloUser = findViewById(R.id.helloUser);
        btnLogout = findViewById(R.id.btnLogout);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btnFindEvent = findViewById(R.id.btnFindEvent);
        btnMyCreatedEvents = findViewById(R.id.btnMyEvents);
        BtnMySignedUpEvents = findViewById(R.id.btnSignedUpEvents);
//        BtnSMS = findViewById(R.id.btnSMS);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String text = "Hello " + currentUser.getUsername();
        tvHelloUser.setText(text);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        if (isServicesOK()) {

            Log.i("isServicesOK", "Everything is fine");
//            getLocationPermission();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goWelcomeActivity();

            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goCreateEventActivity();
                goMapsActivity();
            }
        });

        btnFindEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFindEventActivity();
            }
        });

        btnMyCreatedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMyCreatedEventsActivity();
            }
        });


        BtnMySignedUpEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMySignedUpEventsActivity();
            }
        });

    }

    public boolean isServicesOK() {

        // This function ensures that the User's client is compatible to
        // allow Google Play Services
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

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            double lat = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.i(TAG, "Location latitude is: " + location.getLatitude());
                            Log.i(TAG, "UserLat is: " + lat);
                            setAlarm(lat, longitude);

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };


    private void setAlarm(double lat, double longitude) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MainActivity.this, Reminder.class);
                intent.putExtra("Latitude", lat);
                intent.putExtra("Longitude", longitude);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

//                long OneMinuteInMillis = 1000 *5;

                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick,
                        pendingIntent);

            } else {
                requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
            }
        }
    }


    private void goMapsActivity() {

        Intent i = new Intent(MainActivity.this, GoogleMapsActivity.class);
        startActivity(i);
        //finish();
    }

    private void goFindEventActivity() {

        Intent i = new Intent(MainActivity.this, FindEventActivity.class);
        startActivity(i);
        //finish();
    }

    private void goMyCreatedEventsActivity() {

        Intent i = new Intent(MainActivity.this, MyCreatedEventsActivity.class);
        startActivity(i);
        //finish();
    }

    private void goWelcomeActivity() {

        Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(i);
        finish();
    }


    private void goMySignedUpEventsActivity() {

        Intent i = new Intent(MainActivity.this, MySignedUpEventsActivity.class);
        startActivity(i);
    }

    private void goCameraActivity() {

        Intent i = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(i);
    }

    private void goImageEditorActivity() {

        Intent i = new Intent(MainActivity.this, ImageEditorActivity.class);
        startActivity(i);
    }

    private void goSendSMSActivity() {

        Intent i = new Intent(MainActivity.this, SendSMSActivity.class);
        startActivity(i);
    }

    private void goTestActivity() {

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}