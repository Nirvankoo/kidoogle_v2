package com.nirv.proj1;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class OnClickListenerSignInGoogleButton implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity;
    private static final int RC_SIGN_IN = 123;

    public OnClickListenerSignInGoogleButton(GoogleSignInClient signInClient, Activity activity) {
        this.mGoogleSignInClient = signInClient;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.googleSignInButton) {
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            // You can get user details from the account object.
            // Example: String email = account.getEmail();

            // Start the next activity or perform any action after successful sign-in
            activity.startActivity(new Intent(activity, Subscription.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(activity, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
