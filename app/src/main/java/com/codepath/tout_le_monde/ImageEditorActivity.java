package com.codepath.tout_le_monde;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageEditorActivity extends AppCompatActivity {

    Button btPick;
    Button btSubmit;
    ImageView imageView;
    private Event event;
    private static final String TAG = "ImageEditor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        event = getIntent().getParcelableExtra("Event");
        Log.i(TAG, "Event is: " + event.getName());

        btPick = findViewById(R.id.bt_pick);
        imageView = findViewById(R.id.EventPhoto);
        btSubmit = findViewById(R.id.bt_submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEventTimeLineActivity();
            }
        });

        btPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {

        // Initialize permission

        int permission = ActivityCompat.checkSelfPermission(ImageEditorActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            pickImage();
        } else {

            if (permission != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ImageEditorActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {
                pickImage();
            }
        }
    }

    private void pickImage() {

        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_PICK);

        // Set type
        intent.setType("image/*");

        startActivityForResult(intent, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            pickImage();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri uri = data.getData();

            switch (requestCode) {
                case 100:

                    Intent intent = new Intent(ImageEditorActivity.this, DsPhotoEditorActivity.class);

                    intent.setData(uri);

                    // Set output directory name
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                            "Images");

                    // Set background for tool bar
                    intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
                            Color.parseColor("#FF6200EE"));

                    // Set background for main screen
                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
                            Color.parseColor("#000000"));

                    // Hide tools
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                            new int[] {DsPhotoEditorActivity.TOOL_WARMTH,
                            DsPhotoEditorActivity.TOOL_PIXELATE});

                    startActivityForResult(intent, 101);
                    break;

                case 101:

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        imageView.setImageBitmap(bitmap);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        ParseFile file = new ParseFile("image.png", byteArray);
                        Log.i(TAG, "File is: " + file);
                        event.put("Image",file);

                        event.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null){
                                    Log.i(TAG, "Something went wrong");
                                } else {
                                    Log.i(TAG, "Image saved");
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Photo saved", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    private void goEventTimeLineActivity() {

        Intent i = new Intent(ImageEditorActivity.this, EventTimelineActivity.class);
        startActivity(i);
    }

}