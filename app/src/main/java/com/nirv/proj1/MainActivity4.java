// MainActivity4.java
package com.nirv.proj1;

import static com.nirv.proj1.MainActivity2._userName;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity4 extends AppCompatActivity {

    private MenuHandler menuHandler;
    private Button voiceIPIbutton;
    private ImageView imgVoiceIPIbutton;
    private TextView userGreetingTV;
    private TextView userAnswerToQuestionTV;
    private VoiceAPIHandler voiceAPIHandler;

   //Comfirmation of corectness of recognized voice IPI
    public TextView userQuestionConfirmTextView;
    public Button userQuestionConfirmButtonYes;
    public Button userQuestionConfirmButtonNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        menuHandler = new MenuHandler(this); // Pass context to MenuHandler constructor

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        voiceIPIbutton = findViewById(R.id.voiceIPIbutton);
        imgVoiceIPIbutton = findViewById(R.id.voiceIPAimageView);
        userQuestionConfirmTextView = findViewById(R.id.userAnswerToQuestionTV);

        // Set greeting text and Answer
        userGreetingTV = findViewById(R.id.userGreetinTV);
        userGreetingTV.setText("Hello, " + _userName);
        userAnswerToQuestionTV = findViewById(R.id.userAnswerToQuestionTV);
        userQuestionConfirmTextView = findViewById(R.id.userQuestionConfirmTextView);
        userQuestionConfirmButtonYes = findViewById(R.id.userQuestionConfirmButtonYes);
        userQuestionConfirmButtonNo = findViewById(R.id.userQuestionConfirmButtonNo);

        // Create an instance of VoiceAPIHandler
        voiceAPIHandler = new VoiceAPIHandler(this, userGreetingTV, userQuestionConfirmTextView, userQuestionConfirmButtonYes, userQuestionConfirmButtonNo);

        // Set OnClickListener to voiceIPIbutton
        OnClickListenerVoiceIPIbutton clickListenerVoiceIPIbutton = new OnClickListenerVoiceIPIbutton(
                voiceIPIbutton,
                imgVoiceIPIbutton,
                userGreetingTV,
                userQuestionConfirmTextView,
                userQuestionConfirmButtonYes,
                userQuestionConfirmButtonNo
        );

        // Set the OnClickListener to the button
        voiceIPIbutton.setOnClickListener(clickListenerVoiceIPIbutton);

        OnClickListenerUserQuestionConfirmButtons userConfirmButtons = new OnClickListenerUserQuestionConfirmButtons(
                userQuestionConfirmButtonYes,
                userQuestionConfirmButtonNo,
                this

        );

        userQuestionConfirmButtonYes.setOnClickListener(userConfirmButtons);
        userQuestionConfirmButtonNo.setOnClickListener(userConfirmButtons);






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
