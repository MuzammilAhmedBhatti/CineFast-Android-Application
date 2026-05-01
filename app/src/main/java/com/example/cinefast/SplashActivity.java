package com.example.cinefast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        // 🔹 Fade animation
        Animation fade = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        logo.startAnimation(fade);

        // 🔹 360 Rotation Animation
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotate.setDuration(2000);                 // 2 sec per rotation
        rotate.setRepeatCount(Animation.INFINITE); // infinite loop
        rotate.setInterpolator(new LinearInterpolator()); // smooth spin

        logo.startAnimation(rotate);

        // 🔹 Check session and navigate accordingly
        SessionManager sessionManager = new SessionManager(this);

        new Handler().postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                // User already logged in, skip to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Go to Onboarding
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            }
            finish();
        }, 5000);
    }
}
