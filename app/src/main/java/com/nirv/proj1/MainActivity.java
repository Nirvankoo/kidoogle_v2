package com.nirv.proj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Google auth
    private FirebaseAuth auth;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    public static FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Build GoogleApiClient
        buildGoogleApiClient();

        // Add AuthStateListener to monitor authentication state changes
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // You can perform additional actions here, such as updating UI elements or accessing authenticated resources
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    // You can perform additional actions here, such as navigating to a sign-in screen
                }
            }
        });

        signIn();
    }

    private void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Handle Google Play services connection failed
        Log.d(TAG, "Google Play services connection failed");
        // You can handle the connection failure here, such as displaying an error message to the user.
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Google sign-in successful
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "Google Sign-In successful. Account name: " + acct.getDisplayName());
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Subscription.class));
            // Start another activity or perform an action after successful sign-in
            // For example:
            // startActivity(new Intent(MainActivity.this, SomeActivity.class));
            System.out.println("You logged in");

            // Get FirebaseUser
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                // User is signed in
                // Do something with the current user, such as storing it in SharedPreferences or displaying user information
                firebaseUser = currentUser;
                Log.d("firebaseUser:", firebaseUser.toString().trim());
            } else {
                // Handle the case where currentUser is null, maybe show a message or retry sign-in
                Log.d(TAG, "FirebaseUser is null");
            }
        } else {
            // Google sign-in failed
            Log.e(TAG, "Sign-in failed. Status code: " + result.getStatus().getStatusCode());
            Toast.makeText(this, "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show();
            // Handle sign-in failure, such as displaying an error message to the user.
        }
    }
}
