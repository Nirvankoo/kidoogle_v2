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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Google auth
    private FirebaseAuth auth;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

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

            // Start another activity or perform an action after successful sign-in
            // For example:
            // startActivity(new Intent(MainActivity.this, SomeActivity.class));
            System.out.println("You logged in");
        } else {
            // Google sign-in failed
            Log.e(TAG, "Sign-in failed. Status code: " + result.getStatus().getStatusCode());
            Toast.makeText(this, "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show();
            // Handle sign-in failure, such as displaying an error message to the user.
        }
    }
}
