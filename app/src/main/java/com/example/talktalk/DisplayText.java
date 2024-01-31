package com.example.talktalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DisplayText extends AppCompatActivity {

    private TextView textView;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);

        // Get references to the UI elements
        textView = findViewById(R.id.convertedtext);
        exit = findViewById(R.id.exit);

        // Retrieve the passed text from the intent
        Intent intent = getIntent();
        String recognizedText = intent.getStringExtra("text");

        // Display the text
        textView.setText(recognizedText);

        // Set up the exit button listener
        exit.setOnClickListener(v -> {

            // Clear the displayed text
            textView.setText("");

            // Navigate back to ListeningActivity
            Intent newIntent = new Intent(this, ListeningActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
            startActivity(newIntent);
            });
    }

}
