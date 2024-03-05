package com.nirv.proj1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView; // Import ImageView

public class MainActivity4 extends AppCompatActivity {

    private boolean statusAnimation = false;
    private Handler handlerAnimation = new Handler();

    Button voiceAPIbutton;
    ImageView imgAnimation; // Declare ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        voiceAPIbutton = findViewById(R.id.voice_ipa_button);
        imgAnimation = findViewById(R.id.voice_ipa_imageview); // Initialize ImageView

        voiceAPIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle animation status
                statusAnimation = !statusAnimation;

                // Start or stop animation based on status
                if (statusAnimation) {
                    startAnimation();
                } else {
                    stopAnimation();
                }
            }
        });
    }

    private void startAnimation() {
        // Example animation
        imgAnimation.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .alpha(0.3f)
                .setDuration(300) // Set duration to 1000 milliseconds (1 second)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        stopAnimation(); // Call stopAnimation() after the animation duration
                    }
                })
                .start();

        // You can add more animation here if needed
    }

    private void stopAnimation() {
        // Reset image properties to their original state
        imgAnimation.animate()
                .scaleX(1f)  // Set original scale X
                .scaleY(1f)  // Set original scale Y
                .alpha(0f)   // Set original alpha
                .setDuration(0)  // No duration for resetting
                .start();
    }

}
