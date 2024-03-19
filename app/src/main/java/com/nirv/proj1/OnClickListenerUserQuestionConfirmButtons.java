package com.nirv.proj1;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OnClickListenerUserQuestionConfirmButtons implements View.OnClickListener {

    private TextView userQuestionConfirmTextView;
    private Button userQuestionConfirmButtonYes;
    private Button userQuestionConfirmButtonNo;
    private TextView userGreetingTV;
    private String speech;
    private String answer;
    private TextView answerFromGPT;
    private Activity activity;

    public OnClickListenerUserQuestionConfirmButtons(Button userQuestionConfirmButtonYes, Button userQuestionConfirmButtonNo, Activity activity, TextView userGreetingTV,
                                                     TextView answerFromGPT) {
        this.userQuestionConfirmButtonYes = userQuestionConfirmButtonYes;
        this.userQuestionConfirmButtonNo = userQuestionConfirmButtonNo;
        this.activity = activity;
        this.userGreetingTV = userGreetingTV;
        this.answerFromGPT = answerFromGPT;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.userQuestionConfirmButtonYes) {
            // Show a Toast message when 'Yes' button is clicked
            Toast.makeText(activity, "Yes button clicked", Toast.LENGTH_SHORT).show();
            speech = userGreetingTV.getText().toString();
            SendRequestToGPT sendReqGPT = new SendRequestToGPT(speech);


            // Send question to GPT
            sendReqGPT.sendRequestToGPT(new SendRequestToGPT.GPTResponseListener() {
                @Override
                public void onResponse(String response) {
                    // Log the response
                    Log.d("GPT Response", response);

                    // Update answer TextView

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Log the text setting process
                                Log.d("Text Setting", "Setting text to: " + answer);

                                // Set the answer to the TextView
                                answerFromGPT.setText(response);
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
