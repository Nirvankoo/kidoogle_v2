package com.nirv.proj1;

import static com.nirv.proj1.MainActivity2._userName;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity4 extends AppCompatActivity {

    private MenuHandler menuHandler;
    private Button voiceIPIbutton;
    private ImageView imgVoiceIPIimgButton;
    private TextView userGreetingTV;
    private TextView userAnswerToQuestionTV;
    private VoiceAPIHandler voiceAPIHandler;

    // Confirmation of correctness of recognized voice IPI
    public TextView userQuestionConfirmTextView;
    public Button userQuestionConfirmButtonYes;
    public Button userQuestionConfirmButtonNo;
    private String answer;

    private TextView answerFromGPT;

    // FirebaseUser
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private GPTHandler gptHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        menuHandler = new MenuHandler(this); // Pass context to MenuHandler constructor

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        voiceIPIbutton = findViewById(R.id.voiceIPIbutton);
        imgVoiceIPIimgButton = findViewById(R.id.voiceIPAimageView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            // User is authenticated, create a User object
            User user = new User(firebaseAuth, firebaseUser);
        } else {
            // User is not authenticated
            Log.d("User", "User is not authenticated");
        }

        User user = new User(firebaseAuth, firebaseUser);

        // Set greeting text
        userGreetingTV = findViewById(R.id.userGreetingTV);
        userGreetingTV.setText("Good day, " + _userName + " now you can ask me anything! :)");

        userQuestionConfirmTextView = findViewById(R.id.userQuestionConfirmTextView);
        userQuestionConfirmButtonYes = findViewById(R.id.userQuestionConfirmButtonYes);
        userQuestionConfirmButtonNo = findViewById(R.id.userQuestionConfirmButtonNo);
        answerFromGPT = findViewById(R.id.answerFromGPT);

        // Create an instance of VoiceAPIHandler
        voiceAPIHandler = new VoiceAPIHandler(this,
                userGreetingTV,
                userQuestionConfirmTextView,
                userQuestionConfirmButtonYes,
                userQuestionConfirmButtonNo,
                answerFromGPT,
                voiceIPIbutton,
                imgVoiceIPIimgButton);

        // Set OnClickListener to voiceIPIbutton
        OnClickListenerVoiceIPIbutton clickListenerVoiceIPIbutton = new OnClickListenerVoiceIPIbutton(
                voiceIPIbutton,
                imgVoiceIPIimgButton,
                userGreetingTV,
                userQuestionConfirmTextView,
                userQuestionConfirmButtonYes,
                userQuestionConfirmButtonNo,
                answerFromGPT
        );

        // Set the OnClickListener to the button
        voiceIPIbutton.setOnClickListener(clickListenerVoiceIPIbutton);

        gptHandler = new GPTHandler("",
                userQuestionConfirmTextView,
                userQuestionConfirmButtonYes,
                userQuestionConfirmButtonNo,
                this,
                answerFromGPT); // Initialize GPTHandler

        OnClickListenerUserQuestionConfirmButtons userQuestionConfirmButtons = new OnClickListenerUserQuestionConfirmButtons(
                userQuestionConfirmButtonYes,
                userQuestionConfirmButtonNo,
                this,
                userGreetingTV,
                answerFromGPT,
                gptHandler // Pass GPTHandler instance
        );

        userQuestionConfirmButtonYes.setOnClickListener(userQuestionConfirmButtons);
        userQuestionConfirmButtonNo.setOnClickListener(userQuestionConfirmButtons);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return menuHandler.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return menuHandler.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the result to the VoiceAPIHandler instance
        voiceAPIHandler.handleActivityResult(requestCode, resultCode, data);

    }
}
