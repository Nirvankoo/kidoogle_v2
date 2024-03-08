// OnClickListenerVoiceIPIbutton.java
package com.nirv.proj1;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OnClickListenerVoiceIPIbutton implements View.OnClickListener {
    private final Button voiceIPIbutton;
    private boolean voiceIPIbuttonFlag;
    private final ImageView imgVoiceIPIbutton;
    private final TextView userGreetingTV;
    private final VoiceAPIHandler voiceAPIHandler;
    private TextView userQuestionConfirmTextView;
    private Button userQuestionConfirmButtonYes;
    private Button userQuestionConfirmButtonNo;

    public OnClickListenerVoiceIPIbutton(Button voiceIPIbutton, ImageView imgVoiceIPIbutton, TextView userGreetingTV, TextView userQuestionConfirmTextView,
                                         Button userQuestionConfirmButtonYes, Button userQuestionConfirmButtonNo) {
        this.voiceIPIbutton = voiceIPIbutton;
        this.imgVoiceIPIbutton = imgVoiceIPIbutton;
        this.userGreetingTV = userGreetingTV;
        this.voiceAPIHandler = new VoiceAPIHandler((Activity) voiceIPIbutton.getContext(), userGreetingTV, userQuestionConfirmTextView, userQuestionConfirmButtonYes, userQuestionConfirmButtonNo);
        this.userQuestionConfirmTextView = userQuestionConfirmTextView;
        this.userQuestionConfirmButtonYes = userQuestionConfirmButtonYes;
        this.userQuestionConfirmButtonNo = userQuestionConfirmButtonNo;


    }

    @Override
    public void onClick(View v) {
        // Define what should happen when the button is clicked
        //double-click secure
        MyViewAnimation voiceIPIanimation = new MyViewAnimation(imgVoiceIPIbutton);
        if(!voiceIPIbuttonFlag) {
            voiceIPIbuttonFlag = true;
            voiceIPIbutton.setEnabled(false);

        }

        voiceIPIanimation.startAnimationVoiceIPIbutton();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Re-enable the button
                voiceIPIbutton.setEnabled(true);
                voiceIPIbuttonFlag = false;
                voiceAPIHandler.invokeVoiceAPI();
                voiceIPIanimation.resetAnimationVoiceIPIbutton();
            }
        }, 1000); // 500 milliseconds = 1 second

    }
}
