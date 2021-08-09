package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnStart;
    SliderView sliderView;
    int [] images = {R.drawable.metoo,R.drawable.blackl, R.drawable.asianhate, R.drawable.lgbtq_slide,
            R.drawable.palestine, R.drawable.together};

    SliderAp sliderAp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnStart = findViewById(R.id.btnStart);
        sliderView = findViewById(R.id.slider_view);

        sliderAp = new SliderAp(images);
        sliderView.setSliderAdapter(sliderAp);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goLoginActivity();

            }
        });
    }

    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}