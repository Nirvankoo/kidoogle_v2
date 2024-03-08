package com.nirv.proj1;

import android.view.View;

public class MyViewAnimation {

    private View view;

    public MyViewAnimation(View view) {
        this.view = view;
    }

    public void startAnimationVoiceIPIbutton() {
        // Example animation
        view.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .alpha(0.3f)
                .setDuration(300) // Set duration to 300 milliseconds
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        stopAnimationVoiceIPIbutton(); // Call stopAnimation() after the animation duration
                    }
                })
                .start();

        // You can add more animation here if needed
    }

    public void stopAnimationVoiceIPIbutton() {
        // Reset view properties to their original state
        view.animate()
                .scaleX(0f)  // Set original scale X
                .scaleY(0f)  // Set original scale Y
                .alpha(0f)   // Set original alpha
                .setDuration(0)  // No duration for resetting
                .start();
    }

    public void resetAnimationVoiceIPIbutton(){
        view.animate()
                .scaleX(1f)  // Set original scale X
                .scaleY(1f)  // Set original scale Y
                .alpha(1f)   // Set original alpha
                .setDuration(0)  // No duration for resetting
                .start();
    }
}
