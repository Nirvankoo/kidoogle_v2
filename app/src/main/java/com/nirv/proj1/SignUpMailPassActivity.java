package com.nirv.proj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpMailPassActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signUpEmail;
    private EditText signUpPassword;
    private EditText signUpRePassword;
    private Button signUpButton;

    private String signUpEmailStr;
    private String signUpPasswordStr;
    private String signUpRePasswordStr;

    private TextView signUpRedirectText;

    private ActionCodeSettings actionCodeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_mail_pass);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Firebase Dynamic Links
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        // Handle the deep link
                        handleDynamicLink(deepLink);
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle error
                    Toast.makeText(this, "Failed to retrieve dynamic link", Toast.LENGTH_SHORT).show();
                });

        // Set up action code settings
        String deepLink = "https://proj1.page.link/kidoogle"; // Replace with your dynamic link URL
        actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(deepLink)
                .setHandleCodeInApp(true)
                .setIOSBundleId("com.nirv.proj1")
                .setAndroidPackageName(
                        getApplicationContext().getPackageName(),
                        true, /* installIfNotAvailable */
                        null /* minimumVersion */)
                .build();

        // Find objects
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpRePassword = findViewById(R.id.signUpRePassword);
        signUpButton = findViewById(R.id.signUpButton);
        signUpRedirectText = findViewById(R.id.signUpRedirectText);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpEmailStr = signUpEmail.getText().toString();
                signUpPasswordStr = signUpPassword.getText().toString();
                signUpRePasswordStr = signUpRePassword.getText().toString();

                // Validate passwords
                if (isPasswordValid(signUpPasswordStr) && signUpPasswordStr.equals(signUpRePasswordStr)) {
                    // Password is valid and matches the confirmation password
                    // Proceed with sending verification code and sign up
                    sendVerificationLink(signUpEmailStr); //1
                } else {
                    // Invalid password or passwords do not match
                    signUpPassword.setError("Password must be at least 8 characters long and contain at least 2 digits");
                    signUpRePassword.setError("Passwords do not match");
                    Toast.makeText(SignUpMailPassActivity.this, "Invalid password or passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpMailPassActivity.this, LoginMailPassActivity.class));
                finish();
            }
        });
    }

    // Handle dynamic link
    private void handleDynamicLink(Uri deepLink) {
        // Handle the dynamic link here
        // For example, extract parameters and take appropriate actions
    }

    // Method to validate password
    private boolean isPasswordValid(String password) {
        // Define a regular expression to match passwords with at least two digits and 8 characters long
        String regex = "^(?=.*[0-9].*[0-9])(?=.*[a-zA-Z]).{8,}$";
        return password.matches(regex);
    }

    // Method to send verification link
    private void sendVerificationLink(String email) {
        // Send verification link to the provided email
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email sent successfully
                            Toast.makeText(SignUpMailPassActivity.this, "Verification link sent to your email", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to send verification link
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(SignUpMailPassActivity.this, "Failed to send verification link: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}
