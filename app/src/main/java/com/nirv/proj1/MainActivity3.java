package com.nirv.proj1;

import static com.nirv.proj1.MainActivity2._userName;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {


    TextView greetingTextTextView;
    Button greetingTextNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        greetingTextTextView = findViewById(R.id.greetingText);
        greetingTextNextButton = findViewById(R.id.greetingTextNextButton);



        String username = getString(R.string.userName);
        greetingTextTextView.setText(getString(R.string.userName, "Hello, " + _userName));

        //dalay 3sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000); // 3000 milliseconds = 1 second

        startActivity(new Intent(MainActivity3.this, MainActivity4.class));

    }
}