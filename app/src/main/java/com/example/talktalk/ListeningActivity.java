package com.example.talktalk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import java.util.ArrayList;
import java.util.Locale;

public class ListeningActivity extends AppCompatActivity implements RecognitionListener {

    private SpeechRecognizer speechRecognizer;
    private Button record;
    private Button finish;
    String recognizedText = ""; // Store recognized text here
    private TextView listeningTextView;
    private Handler mHandler = new Handler();
    private boolean isVisible = true;

    private final Runnable updateListeningText = new Runnable() {
        @Override
        public void run() {
            listeningTextView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            isVisible = !isVisible;
            mHandler.postDelayed(this, 500); // Adjust delay for desired flicker speed
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        //Clear recognizedText variable
        recognizedText = "";

        // Find UI elements
        record = findViewById(R.id.record);
        finish = findViewById(R.id.finish);
        listeningTextView = findViewById(R.id.listeningTextView);

        // Initially hide the text view
        listeningTextView.setVisibility(View.INVISIBLE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);


        // Set recognition intent
        Intent recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
           recognitionIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 30000); // Adjust silence timeout to 30 seconds
           recognitionIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000); // Encourage continuous speech

            // Add click listener to the finish button
        finish.setOnClickListener(v -> {
            speechRecognizer.stopListening(); // Stop listening
            mHandler.removeCallbacks(updateListeningText); // Stop flickering animation

            // Navigate to DisplayText activity with recognized text
            Intent intent = new Intent(this, DisplayText.class);
            intent.putExtra("text", recognizedText);
           // Log.i("ListeningActivity", "Text added to intent: " + recognizedText);

            startActivity(intent);

            //Clear recognizedText variable
        });

        // Add click listener to the record button
        record.setOnClickListener(v -> {
            try {
                listeningTextView.setText("Listening...");
                listeningTextView.setVisibility(View.VISIBLE); // Make sure the text view is visible
                mHandler.post(updateListeningText); // Start flickering animation
                speechRecognizer.startListening(recognitionIntent); //Start listening

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error starting speech recognition", Toast.LENGTH_SHORT).show();
            }
        });
    }}

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error); // Get a user-friendly error message
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
    private String getErrorText(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                return "Audio recording error";
            case SpeechRecognizer.ERROR_CLIENT:
                return "Client side error";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "Insufficient permissions";
            case SpeechRecognizer.ERROR_NETWORK:
                return "Network error";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "Network timeout";
            case SpeechRecognizer.ERROR_NO_MATCH:
                return "No recognition result matched";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "Recognition service busy";
            case SpeechRecognizer.ERROR_SERVER:
                return "Server error";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "No speech input";
            default:
                return "Unknown error";
        }}

    @Override
    public void onResults(Bundle bundle) {

        ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        recognizedText = results.get(0); // Get the first (and likely best) result
        recognizedText = Character.toUpperCase(recognizedText.charAt(0)) + recognizedText.substring(1);

    }


    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy(); // Release the SpeechRecognizer
    }

}

