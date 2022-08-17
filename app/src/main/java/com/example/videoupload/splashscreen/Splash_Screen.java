package com.example.videoupload.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.videoupload.R;
import com.example.videoupload.dashBoard;

public class Splash_Screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lottieAnimationView.setSpeed((float) 2.0);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(Splash_Screen.this, dashBoard.class));
                lottieAnimationView.cancelAnimation();
                finish();
            }
        }, secondsDelayed * 2000);

    }
}