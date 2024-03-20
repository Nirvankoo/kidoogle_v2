package com.nirv.proj1;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    private EditText nameTxt;
    private EditText ageTxt;
    private Button nextNameButton;
    private TextView greetingText;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public static String _userName;

    private TextView enterNameTxt;
    private TextView enterAgeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("Acitivity2", "Entered");

        nameTxt = findViewById(R.id.nameTxt);
        ageTxt = findViewById(R.id.ageTxt);
        nextNameButton = findViewById(R.id.next_name_button);
        greetingText = findViewById(R.id.greetingText);
        enterNameTxt = findViewById(R.id.enterNameTxt);
        enterAgeTxt = findViewById(R.id.enterAgeTxt);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        checkUserNameAgeStatus();
        showNameAgePrompt();


        nextNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTxt.getText().toString().trim();
                String ageString = ageTxt.getText().toString().trim();

                if (name.isEmpty() || ageString.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "Please enter name and age", Toast.LENGTH_SHORT).show();
                    enterAgeTxt.setVisibility(View.VISIBLE);
                    return;
                }else{
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

                    userRef.child("userName").setValue(name);

                    String userName = getString(R.string.userName, name);
                    //userRef.child("userAge").setValue(age);
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageString);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity2.this, "Age should be a number", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                saveUserNameAndAgeToDatabase(name, age);
                userRef.child("userAge").setValue(age);
                String userAge = getString(R.string.userAge, age);

                startActivity(new Intent(MainActivity2.this, MainActivity3.class));

            }

        });


    }

    private void showNameAgePrompt() {

        nameTxt.setVisibility(View.VISIBLE);
        ageTxt.setVisibility(View.VISIBLE);
        nextNameButton.setVisibility(View.VISIBLE);
        enterNameTxt.setVisibility(View.VISIBLE);
        enterAgeTxt.setVisibility(View.VISIBLE);
    }

    private void checkUserNameAgeStatus() {
        if (firebaseUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("userName").getValue(String.class);
                        Integer userAge = dataSnapshot.child("userAge").getValue(Integer.class);
                        Log.d("MainActivity2", "userName: " + userName);
                        Log.d("MainActivity2", "userAge: " + userAge);

                        if (userName != null  && userAge != null) {
                            // userName and userAge are not null and have non-zero length
                            greetingText.setText(getString(R.string.promtToAskQuestions) + " " +userName);
                            greetingText.setVisibility(View.VISIBLE);
                            _userName = userName;

                            //set stringValue
                            String userNameStr = getString(R.string.userName, userName);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(MainActivity2.this, MainActivity4.class));
                                }
                            }, 3000); // Delay for 3 seconds
                        } else {
                            // userName or userAge is null or empty
                            showNameAgePrompt();
                        }
                    } else {
                        showNameAgePrompt();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity2.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
           Log.d("firebase:", "firebase us null");
        }
    }


    private void saveUserNameAndAgeToDatabase(String name, int age) {
        if (firebaseUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

            userRef.child("userName").setValue(name);
            userRef.child("userAge").setValue(age);


        }
    }
}
