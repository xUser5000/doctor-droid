package com.doctordroid.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;

public class AuthRepository {

    private static AuthRepository instance;

    private FirebaseAuth auth;
    private Realm realm;

    private AuthRepository () {
        auth = FirebaseAuth.getInstance();
        realm = Realm.getDefaultInstance();
    }

    public static AuthRepository getInstance() {
        if (instance == null) instance = new AuthRepository();
        return instance;
    }

    public Task<AuthResult> register (String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> login (String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public boolean isAuthenticated () {
        return auth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser () {
        return auth.getCurrentUser();
    }

    public Task<Void> sendResetPasswordEmail (String email) {
        return auth.sendPasswordResetEmail(email);
    }

    public void logout () {
        auth.signOut();
        realm.executeTransaction(realm -> realm.deleteAll());
    }
}
