package com.nirv.proj1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginMailPassActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private Button resendVerificationButton;

    private String email;
    private String password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mail_pass);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = loginEmail.getText().toString();
                password = loginPassword.getText().toString();

                if (isPasswordValid(password)) {
                    startActivity(new Intent(LoginMailPassActivity.this, LoginVerCode.class));
                } else {
                    loginPassword.setError("Password must contain at least 2 numbers");
                    Toast.makeText(LoginMailPassActivity.this, "Password must contain at least 2 numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean isPasswordValid(String password) {
        // Define a regular expression to match passwords with at least two numbers
        String regex = "^(?=.*[0-9].*[0-9]).{6,}$";
        return password.matches(regex);
    }
}
