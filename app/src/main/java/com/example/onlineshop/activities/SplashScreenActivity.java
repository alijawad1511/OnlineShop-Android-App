package com.example.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.onlineshop.LoginActivity;


public class SplashScreenActivity extends AppCompatActivity {

    // 4 seconds timeout
    private final int SPLASH_TIMEOUT = 4000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        },SPLASH_TIMEOUT);
    }
}
