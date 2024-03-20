package com.nirv.proj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpMailPassActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signUpEmail;
    private EditText signUpPassword;
    private EditText signUpRePassword;
    private Button signUpButton;

    private String signUpEmailStr;
    private String signUpPasswordStr;
    private String signUpRePasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_mail_pass);

        auth = FirebaseAuth.getInstance();

        // Find objects
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpRePassword = findViewById(R.id.signUpRePassword);
        signUpButton = findViewById(R.id.signUpButton);

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
                    sendVerificationCodeAndSignUp(signUpEmailStr, signUpPasswordStr);
                } else {
                    // Invalid password or passwords do not match
                    signUpPassword.setError("Password must be at least 8 characters long and contain at least 2 digits");
                    signUpRePassword.setError("Passwords do not match");
                    Toast.makeText(SignUpMailPassActivity.this, "Invalid password or passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        // Define a regular expression to match passwords with at least two digits and 8 characters long
        String regex = "^(?=.*[0-9].*[0-9])(?=.*[a-zA-Z]).{8,}$";
        return password.matches(regex);
    }

    private void sendVerificationCodeAndSignUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpMailPassActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, send verification email
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(SignUpMailPassActivity.this,
                                                    "Verification email sent to " + email,
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpMailPassActivity.this, Subscription.class));
                                            // Proceed with your desired UI update or navigate to the next activity
                                        } else {
                                            // Sending verification email failed
                                            Toast.makeText(SignUpMailPassActivity.this,
                                                    "Failed to send verification email.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Sign up failed
                        Toast.makeText(SignUpMailPassActivity.this,
                                "Sign up failed. Please try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
