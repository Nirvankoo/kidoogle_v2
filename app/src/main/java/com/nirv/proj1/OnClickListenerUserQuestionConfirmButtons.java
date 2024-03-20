package com.nirv.proj1;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class OnClickListenerUserQuestionConfirmButtons implements View.OnClickListener {

    private Button userQuestionConfirmButtonYes;
    private Button userQuestionConfirmButtonNo;
    private TextView userGreetingTV;
    private TextView answerFromGPT;
    private Activity activity;
    private GPTHandler gptHandler;

    public OnClickListenerUserQuestionConfirmButtons(Button userQuestionConfirmButtonYes, Button userQuestionConfirmButtonNo, Activity activity, TextView userGreetingTV,
                                                     TextView answerFromGPT, GPTHandler gptHandler) {
        this.userQuestionConfirmButtonYes = userQuestionConfirmButtonYes;
        this.userQuestionConfirmButtonNo = userQuestionConfirmButtonNo;
        this.activity = activity;
        this.userGreetingTV = userGreetingTV;
        this.answerFromGPT = answerFromGPT;
        this.gptHandler = gptHandler; // Receive the GPTHandler instance
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.userQuestionConfirmButtonYes) {
            // Show a Toast message when 'Yes' button is clicked
            Toast.makeText(activity, "Yes button clicked", Toast.LENGTH_SHORT).show();
            String speech = userGreetingTV.getText().toString();

            // Send question to GPT using the GPTHandler instance
            try {
                gptHandler.sendGPTRequest(speech, new GPTHandler.GPTResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        // Log the response
                        Log.d("GPT Response", response);

                        // Update answer TextView
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Set the answer to the TextView
                                    answerFromGPT.setVisibility(View.VISIBLE);
                                    answerFromGPT.setText(response);
                                    userGreetingTV.setVisibility(View.INVISIBLE);


                                } catch (Exception e) {
                                    Log.e("Error", "Error setting text: " + e.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error
                        Log.e("GPT Error", error);
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else if (v.getId() == R.id.userQuestionConfirmButtonNo) {
            // Show a Toast message when 'No' button is clicked
            Toast.makeText(activity, "No button clicked", Toast.LENGTH_SHORT).show();
            // Invoke voice IPI
        } else {
            // Show a Toast message for any other action
            Toast.makeText(activity, "Button clicked", Toast.LENGTH_SHORT).show();
            // Continue
        }
    }
}
