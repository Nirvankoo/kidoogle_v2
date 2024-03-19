package com.nirv.proj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Subscription extends AppCompatActivity {

    private ImageView subImg1;
    private ImageView subImg2;
    private Button subContinueButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            checkSubscriptionStatus(new SubscriptionStatusCallback() {
                @Override
                public void onStatusChecked(boolean hasSubscription) {
                    if (hasSubscription) {
                        // Transfer to next activity
                        startActivity(new Intent(Subscription.this, MainActivity2.class));
                        finish(); // Finish this activity to prevent going back
                    } else {

                        initializeViews();
                        subImg1.setVisibility(View.VISIBLE);
                        subImg2.setVisibility(View.VISIBLE);
                        subContinueButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            // User is not logged in, handle accordingly
        }
    }

    private void initializeViews() {
        subImg1 = findViewById(R.id.sub_img1);
        subImg2 = findViewById(R.id.sub_img2);
        subContinueButton = findViewById(R.id.sub_continue_button);

        subImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(subImg1);
            }
        });

        subImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(subImg2);
            }
        });

        subContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subImg1.isSelected() || subImg2.isSelected()) {
                    startActivity(new Intent(Subscription.this, MainActivity2.class));
                    finish(); // Finish this activity to prevent going back
                } else {
                    Toast.makeText(Subscription.this, "You have to choose a plan!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toggleSelection(ImageView imageView) {
        if (imageView.isSelected()) {
            imageView.setSelected(false);
            imageView.setImageResource(R.drawable.sub_option1);
        } else {
            imageView.setSelected(true);
            imageView.setImageResource(R.drawable.sub_option1_selected);

            if (imageView == subImg1) {
                subImg2.setSelected(false);
                subImg2.setImageResource(R.drawable.sub_option2);
            } else {
                subImg1.setSelected(false);
                subImg1.setImageResource(R.drawable.sub_option1);
            }
        }
    }

    private void checkSubscriptionStatus(final SubscriptionStatusCallback callback) {
        if (firebaseUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean hasSubscription = false;
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("sub")) {
                        String subValue = dataSnapshot.child("sub").getValue(String.class);
                        Log.d("sub:", subValue);

                        if (subValue != null && subValue.equals("true")) {
                            hasSubscription = true;
                        }
                    }
                    callback.onStatusChecked(hasSubscription);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    callback.onStatusChecked(false);
                }
            });
        } else {
            // User is not logged in, handle accordingly
            callback.onStatusChecked(false);
        }
    }

    interface SubscriptionStatusCallback {
        void onStatusChecked(boolean hasSubscription);
    }
}
