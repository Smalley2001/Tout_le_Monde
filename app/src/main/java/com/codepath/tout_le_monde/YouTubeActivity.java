package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubeActivity extends YouTubeBaseActivity {

    YouTubePlayerView mYouTubePlayerView;
    Button btnContinue;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    private static final String TAG = "YouTubeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);


        btnContinue = findViewById(R.id.btnContinue);
        mYouTubePlayerView =  (YouTubePlayerView) findViewById(R.id.youtubePlay);

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "Done Initializing");
                youTubePlayer.loadVideo("rZn0f0qD7QY");

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Failed Initializing");

            }
        };

        mYouTubePlayerView.initialize(getString(R.string.GOOGLE_YOUTUBE_API_KEY), mOnInitializedListener);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(YouTubeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}