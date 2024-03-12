package com.nirv.proj1;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInGoogle {

    private Context context;

    public SignInGoogle(Context context) {
        this.context = context;
    }

    public GoogleSignInAccount getLastSignedInAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public boolean isUserLoggedIn() {
        return getLastSignedInAccount() != null;
    }

    public GoogleApiClient buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                // Add any additional scopes if needed
                .requestScopes(new Scope("profile")) // Example: requesting profile scope
                .build();

        // Get FirebaseUser

        return new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, (GoogleApiClient.OnConnectionFailedListener) context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}
