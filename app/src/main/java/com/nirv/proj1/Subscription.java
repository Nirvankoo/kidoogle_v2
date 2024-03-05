package com.nirv.proj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Subscription extends AppCompatActivity {

    private ImageView subImg1;
    private ImageView subImg2;
    private Button subContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        subImg1 = findViewById(R.id.sub_img1);
        subImg2 = findViewById(R.id.sub_img2);
        subContinueButton = findViewById(R.id.sub_continue_button);

        subImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subImg1.isSelected()) {
                    // If subImg1 is already selected, deselect it
                    subImg1.setSelected(false);
                    // Change foreground image to indicate deselection
                    subImg1.setImageResource(R.drawable.sub_option1);
                } else {
                    // If subImg1 is not selected, select it and deselect subImg2
                    subImg1.setSelected(true);
                    subImg2.setSelected(false);
                    // Change foreground images to indicate selection
                    subImg1.setImageResource(R.drawable.sub_option1_selected);
                    subImg2.setImageResource(R.drawable.sub_option2);
                }
            }
        });

        subImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subImg2.isSelected()) {
                    // If subImg2 is already selected, deselect it
                    subImg2.setSelected(false);
                    // Change foreground image to indicate deselection
                    subImg2.setImageResource(R.drawable.sub_option2);
                } else {
                    // If subImg2 is not selected, select it and deselect subImg1
                    subImg2.setSelected(true);
                    subImg1.setSelected(false);
                    // Change foreground images to indicate selection
                    subImg2.setImageResource(R.drawable.sub_option2_selected);
                    subImg1.setImageResource(R.drawable.sub_option1);
                }
            }
        });



        subContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check which image is selected
                if (subImg1.isSelected()) {
                    // Option 1 (subImg1) is selected, proceed accordingly
                    startActivity(new Intent(Subscription.this, MainActivity2.class));
                } else if (subImg2.isSelected()) {
                    // Option 2 (subImg2) is selected, proceed accordingly
                    startActivity(new Intent(Subscription.this, MainActivity2.class));
                } else {
                    // No option is selected
                    Toast.makeText(Subscription.this, "You have to chose a plan!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}