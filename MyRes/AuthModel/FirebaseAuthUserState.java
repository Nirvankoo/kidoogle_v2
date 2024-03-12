package com.nirv.proj1;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.MainThread;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class FirebaseAuthUserState {}

class UserSignedIn extends FirebaseAuthUserState {
    private FirebaseUser user;
    public UserSignedIn(FirebaseUser user) {
        this.user = user;
    }
    public FirebaseUser getUser() {
        return user;
    }
}

class UserSignedOut extends FirebaseAuthUserState {}

class UserUnknown extends FirebaseAuthUserState {}

class FirebaseAuthStateLiveData extends MediatorLiveData<FirebaseAuthUserState> {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener = new MyAuthStateListener();

    public FirebaseAuthStateLiveData(FirebaseAuth auth) {
        this.auth = auth;
        setValue(new UserUnknown());
    }

    @Override
    protected void onActive() {
        super.onActive();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        auth.removeAuthStateListener(authStateListener);
    }

    private class MyAuthStateListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                setValue(new UserSignedIn(user));
            } else {
                setValue(new UserSignedOut());
            }
        }
    }
}

@MainThread
class FirebaseAuthUtils {
    public static LiveData<FirebaseAuthUserState> newFirebaseAuthStateLiveData(FirebaseAuth auth) {
        FirebaseAuthStateLiveData ld = new FirebaseAuthStateLiveData(auth);
        MediatorLiveData<FirebaseAuthUserState> liveData = new MediatorLiveData<>();
        liveData.addSource(ld, liveData::setValue);
        return liveData;
    }
}
