package com.nirv.proj1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthUserStateViewModel extends ViewModel {
    private LiveData<FirebaseAuthUserState> firebaseAuthUserStateLiveData;

    public FirebaseAuthUserStateViewModel() {
        firebaseAuthUserStateLiveData = FirebaseAuthUtils.newFirebaseAuthStateLiveData(FirebaseAuth.getInstance());

    }

    public LiveData<FirebaseAuthUserState> getFirebaseAuthUserStateLiveData() {
        return firebaseAuthUserStateLiveData;
    }
}
