package com.example.talktalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 8000; // 8 seconds in milliseconds

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler to delay and start new activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, ListeningActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}