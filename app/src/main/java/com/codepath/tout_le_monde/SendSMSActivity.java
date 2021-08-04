package com.codepath.tout_le_monde;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class SendSMSActivity extends AppCompatActivity {


    private static final String TAG = "SendSMSActivity";
    private EditText number;
    private EditText message;
    private Button send;
    private double lat;
    private double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_smsactivity);

        number = findViewById(R.id.number);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        Log.i(TAG, String.valueOf(lat));

//        queryUsers();
//        queryEvents();
//        contactUser(ParseUser.getCurrentUser());
//        double milli = convertHoursToMilliseconds(1);
//        Log.i(TAG, "1 Hour in Milliseconds is: " + milli);

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(SendSMSActivity.this, Reminder.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(SendSMSActivity.this, 0, intent, 0);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                        long timeAtButtonClick = System.currentTimeMillis();

                        long OneMinuteInMillis = 1000 *5;

                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                timeAtButtonClick + OneMinuteInMillis,
                                pendingIntent);

//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
//                               OneMinuteInMillis, pendingIntent );

//                        alarmManager.cancel(pendingIntent);

//                        sendSMS();
                    } else {
                        requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
                    }
                }

            }
        });
    }

    private void sendSMS() {
        String phoneNumber = number.getText().toString().trim();
        String SMS = message.getText().toString().trim();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, SMS, null, null);
            Toast.makeText(this, "Message is sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }

    }


//    private boolean isUserSignedUp() {
//
//        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
//        // include data referred by user key
//        query.include(Event.KEY_PARTICIPANTS);
//        query.whereEqualTo(Event.KEY_PARTICIPANTS, ParseUser.getCurrentUser());
//
//        query.getFirstInBackground();
//    }

}