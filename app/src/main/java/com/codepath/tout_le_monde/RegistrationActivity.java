package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegistrationActivity extends AppCompatActivity {

    public static final String TAG = "RegistrationActivity";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etMobile;
    private EditText etConfirmPassword;
    private Button btnSubmitUser;

    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Set member variables equal to the views in activity_registration
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSubmitUser = findViewById(R.id.btnSubmitUser);

        //Initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.etUsername, RegexTemplate.NOT_EMPTY,
                R.string.invalid_name);

        awesomeValidation.addValidation(this,R.id.etMobile, "[1-9]{1}[0-9]{9}$",
                R.string.invalid_mobile);

        awesomeValidation.addValidation(this,R.id.etEmail, RegexTemplate.NOT_EMPTY,
                R.string.invalid_email);

        awesomeValidation.addValidation(this,R.id.etConfirmPassword,R.id.etPassword,
                R.string.invalid_confirm_password);



        //Set onclick listener for button
        btnSubmitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCLick registration button");

                if(awesomeValidation.validate()) {

                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String email = etEmail.getText().toString();
                    String phoneNumber = etMobile.getText().toString();
                    SignUpUser(username, password, email, phoneNumber);
                } else {
                    Toast.makeText(getApplicationContext(), "Error with registration", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SignUpUser(String username, String password, String email, String phoneNumber) {

        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // User successfully signed up
                    user.put("PhoneNumber", phoneNumber);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i(TAG, "Phone Number saved!");
                        }
                    });
                    goMainActivity();

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.i(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


}
