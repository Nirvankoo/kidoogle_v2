package com.nirv.proj1;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OnClickListenerUserQuestionConfirmButtons implements View.OnClickListener {

    private TextView userQuestionConfirmTextView;
    private Button userQuestionConfirmButtonYes;
    private Button userQuestionConfirmButtonNo;

    Activity activity;

    public OnClickListenerUserQuestionConfirmButtons(Button userQuestionConfirmButtonYes, Button userQuestionConfirmButtonNo, Activity activity) {
        this.userQuestionConfirmButtonYes = userQuestionConfirmButtonYes;
        this.userQuestionConfirmButtonNo = userQuestionConfirmButtonNo;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        // Check which button is clicked
        if (v.getId() == R.id.userQuestionConfirmButtonYes) {
            // Show a Toast message when 'Yes' button is clicked
            Toast.makeText(activity, "Yes button clicked", Toast.LENGTH_SHORT).show();
            //send question to GPT
        } else if (v.getId() == R.id.userQuestionConfirmButtonNo) {
            // Show a Toast message when 'No' button is clicked
            Toast.makeText(activity, "No button clicked", Toast.LENGTH_SHORT).show();
            //invoke voice IPI
        } else {
            // Show a Toast message for any other action
            Toast.makeText(activity, "Button clicked", Toast.LENGTH_SHORT).show();
            //TODO
            //continue
        }
    }
}
