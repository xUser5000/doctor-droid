package com.doctordroid.presentation.auth;

import android.arch.lifecycle.ViewModel;

import com.doctordroid.data.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private AuthRepository authRepository = AuthRepository.getInstance();

    public boolean isAuthenticated () {
        return authRepository.isAuthenticated();
    }

}
