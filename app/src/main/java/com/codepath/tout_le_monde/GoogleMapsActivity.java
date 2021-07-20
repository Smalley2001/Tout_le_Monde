package com.codepath.tout_le_monde;


// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "GoogleMapsActivity";

    // variables
    private GoogleMap MyMap;
    private Boolean LocationPermissionsGranted = false;
    private FusedLocationProviderClient MFusedLocationProviderClient;
    private Place CurrentPlace;
    private Marker MyMarker;

    // widgets
    private EditText MSearchText;
    private ImageView MGps;
    private Button BtnSubmitLocation;
    private ImageView MInfo;

    /**
        mLocationCallback is a public variable for getting the location
        This is because we want to already have a location stored so when
        we call for location updates later, there won't be a bug because
        if our original location was null, it could cause issues

     */

    private LocationCallback MLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.i(TAG, "no location for update");
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    Log.i(TAG, location.toString());
                    moveCamera(new LatLng(location.getLatitude(), location.getLongitude()),
                            DEFAULT_ZOOM, "My Location");

                    // Must explicitly ask for permission in order to setMyLocationEnabled
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    // This sets a blue dot on the map to indicate where you are located
                    // Also adds a default button in upper right corner of UI that allows
                    // the user to return back to their location on the map if they scrolled away
                    MyMap.setMyLocationEnabled(true);
                    MyMap.getUiSettings().setMyLocationButtonEnabled(false);
                    init();

                    // Stops app from constantly asking for locations to save user's battery
                    LocationServices.getFusedLocationProviderClient(getApplicationContext()).removeLocationUpdates(MLocationCallback);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        MSearchText = findViewById(R.id.input_search);
        MGps = findViewById(R.id.ic_gps);
        MInfo = findViewById(R.id.place_info);
        BtnSubmitLocation = findViewById(R.id.btnSubmitLocation);
        getLocationPermission();

        // Initialize places
        Places.initialize(getApplicationContext(), getString(R.string.GOOGLE_MAPS_API_KEY));

        // Set EditText nonfocusable
        MSearchText.setFocusable(false);
        MSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                        Place.Field.NAME, Place.Field.PHONE_NUMBER, Place.Field.ID,
                        Place.Field.PHOTO_METADATAS, Place.Field.WEBSITE_URI);

                // Populate autocomplete results on part of the screen
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(GoogleMapsActivity.this);

                startActivityForResult(intent, 100);
            }
        });

        BtnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_location = MSearchText.getText().toString();
                goCreateEventActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            // When success
            // Initialize Place

            Place place = Autocomplete.getPlaceFromIntent(data);
            CurrentPlace = place;
            MSearchText.setText(place.getAddress());
            geoLocate();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // When error
            // Initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * <p>
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        MyMap = googleMap;

        if (LocationPermissionsGranted) {
            LocationRequest mLocationRequest = LocationRequest.create();
            // Longest our request will go is 1 minute
            mLocationRequest.setInterval(60000);
            // Shortest our request will go is 5 seconds
            mLocationRequest.setFastestInterval(5000);
            // We want the location with high accuracy
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Even though we already have location permissions
            // Android Studio requires that we explicitly ask for permissions
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Request location only once (no looper needed)
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, MLocationCallback, null);
            }
        }
    }

    private void getLocationPermission() {
        Log.d(TAG, "getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        //Check if we have permission to access the user's location

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationPermissionsGranted = true;
                initMap();
            } else {
                // If we don't have permission for COARSE Location then we must ask for it!
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // If we don't have permission for FINE Location then we must ask for it
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    /**
        This function overrides the enter key when the user tries to submit a query on the
        Google Map. Also has onclick listener for when user wants to return to their location
        on the Map
     */
    private void init() {

        Log.d(TAG, "init: intializiing");
        MSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    // Execute our method for searching
                    geoLocate();

                }
                hideSoftKeyboard();
                return false;
            }
        });

        // User wants to return to their current location on the map
        MGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onMapReady(MyMap);

            }
        });

        // User wants information on the selected location
        MInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked place info");
                try {
                    if (MyMarker.isInfoWindowShown()) {
                        MyMarker.hideInfoWindow();
                    }else {
                        Log.d(TAG, "onClick: place info: " + CurrentPlace.toString());
                        MyMarker.showInfoWindow();
                    }
                }catch (NullPointerException e) {
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });
        hideSoftKeyboard();
    }

    /**
     * Obtain the SupportMapFragment and get notified when the map is ready to be used.
     * The SupportMapFragment object manages the life cycle of the map and is the
     * parent element of the app's UI.
     */

    public void initMap() {


        Log.d(TAG, "initMap : initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(GoogleMapsActivity.this);
    }

    // This function is called after Permissions have been requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult : called");
        LocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            LocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "Permissions granted");
                    LocationPermissionsGranted = true;
                    initMap();

                }
            }
        }
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = MSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(GoogleMapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);

        }catch (IOException e) {
            Log.e(TAG, "geoLocate: IOexception: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM, address.getAddressLine(0) );

        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        MyMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        MyMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(GoogleMapsActivity.this));
        //W e don't want the marker to be at the same spot as the blue spot indicating our location
        if (!title.equals("My Location")) {

            if (CurrentPlace != null) {

                Log.i(TAG, CurrentPlace.getName() + " " + CurrentPlace.getWebsiteUri());

                try {

                    String snippet = "Address: " + CurrentPlace.getAddress() + "\n" +
                            "Phone Number: " + CurrentPlace.getPhoneNumber() + "\n" +
                            "Website: " + CurrentPlace.getWebsiteUri() + "\n";

                    MarkerOptions options = new MarkerOptions()
                            .position(latlng)
                            .title(title)
                            .snippet(snippet);

                    MyMarker = MyMap.addMarker(options);

                } catch (NullPointerException e) {
                    Log.e(TAG, "moveCamera: NullPointerException: " +  e.getMessage());
                }
            } else {
                MarkerOptions options = new MarkerOptions()
                        .position(latlng)
                        .title(title);

                MyMap.addMarker(options);

            }

        }

        hideSoftKeyboard();

    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void goCreateEventActivity() {

        String selected_location = MSearchText.getText().toString();
        Intent i = new Intent(GoogleMapsActivity.this, CreateEventActivity.class);
        i.putExtra("Location", selected_location);
        startActivity(i);
    }
}

