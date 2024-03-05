package com.nirv.proj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    private TextView enterNameTxt;
    private EditText nameTxt;
    private Button nextNameButton;
    private TextView enterAgeTxt;
    private EditText ageTxt;

    public static String _userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        enterNameTxt = findViewById(R.id.enterNameTxt);
        nameTxt = findViewById(R.id.nameTxt);
        nextNameButton = findViewById(R.id.next_name_button);
        enterAgeTxt = findViewById(R.id.enterAgeTxt);
        ageTxt = findViewById(R.id.ageTxt);



        nextNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTxt.getText().toString().trim();
                String ageString = ageTxt.getText().toString().trim();

                _userName = name;

                // Convert age input to an integer
                int age = 0;
                try {
                    age = Integer.parseInt(ageString);
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    // Optionally, show a message to the user indicating that the age should be a number
                    Toast.makeText(MainActivity2.this, "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Retrieve the string resource with the placeholder for age
                String userAgeTemplate = getString(R.string.userAge);

                // Format the age using the placeholder
                String userAge = String.format(userAgeTemplate, age);

                // Now userAge contains the formatted user's age which you can use further
                // For example, you can display it in a TextView or use it in any other way.

                startActivity(new Intent(MainActivity2.this, MainActivity3.class));
            }
        });








    }
}