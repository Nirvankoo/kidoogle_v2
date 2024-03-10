// VoiceAPIHandler.java
package com.nirv.proj1;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class VoiceAPIHandler {
    private static final int REQUEST_CODE_VOICE_INPUT = 100;
    private static final int RECORD_AUDIO_PERMISSION_CODE = 101;
    private final Activity activity;
    private final TextView userGreetingTV;

    private TextView userQuestionConfirmTextView;
    private Button userQuestionConfirmButtonYes;
    private Button userQuestionConfirmButtonNo;

    public VoiceAPIHandler(Activity activity, TextView userGreetingTV, TextView userQuestionConfirmTextView, Button userQuestionConfirmButtonYes,
                           Button userQuestionConfirmButtonNo) {
        this.activity = activity;
        this.userGreetingTV = userGreetingTV;
        this.userQuestionConfirmTextView = userQuestionConfirmTextView;
        this.userQuestionConfirmButtonYes = userQuestionConfirmButtonYes;
        this.userQuestionConfirmButtonNo = userQuestionConfirmButtonNo;
    }

    public void invokeVoiceAPI() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
        } else {
            // Permission already granted, proceed with voice recognition
            startVoiceRecognition();
        }
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        activity.startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            // Voice input successful
            // Handle the result here or return it to the calling activity
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String recognizedSpeech = matches.get(0);
                if (userGreetingTV != null) {
                    userGreetingTV.setText(recognizedSpeech + "?");
                    userQuestionConfirmTextView.setVisibility(View.VISIBLE);
                    userQuestionConfirmButtonYes.setVisibility(View.VISIBLE);
                    userQuestionConfirmButtonNo.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
