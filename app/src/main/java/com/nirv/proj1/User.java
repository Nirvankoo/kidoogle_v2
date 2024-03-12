package com.nirv.proj1;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private String uid;
    private String email;
    private String displayName;
    private String photoUrl;

    public User(FirebaseAuth firebaseAuth, FirebaseUser firebaseUser) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = firebaseUser;

        try {
            if (firebaseUser != null) {
                uid = firebaseUser.getUid();
                email = firebaseUser.getEmail();
                Log.d("Uid:", uid + "!!!!!" );
                Log.d("email:", uid + "!!!!!!!!!!!!!" );

            } else {
                Log.d("firebaseUser:", "null!!!!!!!!!!!!!!!!" );
            }
        } catch (Exception e) {
            Log.e("User Constructor", "Exception: " + e.getMessage());
        }
    }

    // Getter methods
    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
}
