package com.nirv.proj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
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


    //sign in with google manual

    SignInButton signInWithGoogle;
    ImageView logo;
    private GoogleSignInClient mGoogleSignInClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Activity1", "Entered");

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        //google
        signInWithGoogle = findViewById(R.id.googleSignInButton);
        logo = findViewById(R.id.logo);

        // Build GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Build GoogleApiClient
        buildGoogleApiClient();

        signIn();
        //startActivity(new Intent(MainActivity.this, SignUpMailPassActivity.class));


        // Add AuthStateListener to monitor authentication state changes
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //TODO
                //continue to get currenUser
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // You can perform additional actions here, such as updating UI elements or accessing authenticated resources
                    Log.d("user:", String.valueOf(user));
                    Log.d("user:", user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");



                    // You can perform additional actions here, such as navigating to a sign-in screen
                }
            }
        });


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
        Toast.makeText(MainActivity.this, "Google Play services connection failed", Toast.LENGTH_SHORT).show();
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        if (result.isSuccess()) {
            // Google sign-in successful
            GoogleSignInAccount acct = result.getSignInAccount();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.d("MainActivity:", "FirebaseUser:" + user);

            //String userUid = user.getUid();
            //Log.d("MainActivity", userUid);

            Log.d(TAG, "Google Sign-In successful. Account name: " + acct.getDisplayName());
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Subscription.class));
            // Start another activity or perform an action after successful sign-in
            // For example:
            // startActivity(new Intent(MainActivity.this, SomeActivity.class));
            System.out.println("You logged in");

            // Get FirebaseUser


        } else {
            // Google sign-in failed
            Log.e(TAG, "Sign-in failed. Status code: " + result.getStatus().getStatusCode());
            Toast.makeText(this, "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show();
            // Handle sign-in failure, such as displaying an error message to the user.
            logo.setVisibility(View.VISIBLE);
            signInWithGoogle.setVisibility(View.VISIBLE);


        }
    }

    private void showSignInButton() {
        // You need to instantiate OnClickListenerSignInGoogleButton again and set it as onClickListener for your signInWithGoogle button
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInWithGoogle.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);


        OnClickListenerSignInGoogleButton onClickListenerSignInGoogleButton = new OnClickListenerSignInGoogleButton(mGoogleSignInClient, this);
        signInWithGoogle.setOnClickListener(onClickListenerSignInGoogleButton);
    }


}
