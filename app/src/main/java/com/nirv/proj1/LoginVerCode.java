package com.nirv.proj1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginVerCode extends AppCompatActivity {

    private EditText verificationCodeEditText;
    private Button verifyButton;
    private Button resendButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ver_code);

        firebaseAuth = FirebaseAuth.getInstance();

        verificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        verifyButton = findViewById(R.id.verifyButton);
        resendButton = findViewById(R.id.resendButton);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCodeEditText.getText().toString().trim();
                if (!code.isEmpty()) {
                    verifyCode(code);
                } else {
                    Toast.makeText(LoginVerCode.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode();
            }
        });
    }

    private void verifyCode(String code) {
        firebaseAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        // User is already verified
                        Toast.makeText(LoginVerCode.this, "User already verified", Toast.LENGTH_SHORT).show();
                        // Proceed to next activity or perform necessary actions
                    } else {
                        // Verify the code
                        firebaseAuth.getCurrentUser().updateEmail(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseAuth.getCurrentUser().reload();
                                    Toast.makeText(LoginVerCode.this, "Code verified successfully", Toast.LENGTH_SHORT).show();
                                    // Proceed to next activity or perform necessary actions
                                } else {
                                    Toast.makeText(LoginVerCode.this, "Failed to verify code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginVerCode.this, "Failed to verify code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resendVerificationCode() {
        // Disable the resend button to prevent spamming
        resendButton.setEnabled(false);

        // Set a delay time (e.g., 60 seconds) before enabling the resend button again
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Enable the resend button after the delay
                resendButton.setEnabled(true);
            }
        }, 60000); // 60 seconds delay

        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginVerCode.this, "Verification code resent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginVerCode.this, "Failed to resend verification code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
