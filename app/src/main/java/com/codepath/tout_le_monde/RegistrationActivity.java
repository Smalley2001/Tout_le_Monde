package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegistrationActivity extends AppCompatActivity {

    private TextView login;
    public static final String TAG = "TestReg";
    private AwesomeValidation awesomeValidation;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etMobile;
    private EditText etConfirmPassword;
    private Button btnSubmitUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUsername = findViewById(R.id.inputRegUsername);
        etPassword = findViewById(R.id.inputRegPassword);
        etEmail = findViewById(R.id.inputRegEmail);
        etMobile = findViewById(R.id.inputRegPhoneNumber);
        etConfirmPassword = findViewById(R.id.inputRegConfirmPassword);
        btnSubmitUser = findViewById(R.id.btnRegister);

        // Initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

        awesomeValidation.addValidation(this,R.id.inputRegUsername, RegexTemplate.NOT_EMPTY,
                R.string.invalid_name);

        awesomeValidation.addValidation(this,R.id.inputRegPhoneNumber, "[1-9]{1}[0-9]{9}$",
                R.string.invalid_mobile);

        awesomeValidation.addValidation(this,R.id.inputRegEmail, Patterns.EMAIL_ADDRESS,
                R.string.invalid_email);

        awesomeValidation.addValidation(this,R.id.inputRegPassword, RegexTemplate.NOT_EMPTY,
                R.string.invalid_password);

        awesomeValidation.addValidation(this,R.id.inputRegConfirmPassword,R.id.inputRegPassword,
                R.string.invalid_confirm_password);


        btnSubmitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(awesomeValidation.validate()) {
                    Log.i(TAG, "hi there");
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


        login = findViewById(R.id.alreadyHaveAccount);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
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
                    goYouTubeActivity();

                } else {
                    // Sign up failed
                    Log.i(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void goYouTubeActivity() {
        Intent i = new Intent(this, YouTubeActivity.class);
        startActivity(i);
        finish();
    }
}