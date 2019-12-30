package com.doctordroid.presentation.home;

import androidx.lifecycle.ViewModel;

import com.doctordroid.data.repository.AuthRepository;

public class HomeViewModel extends ViewModel {

    private AuthRepository authRepository = AuthRepository.getInstance();

    public void logout () {
        authRepository.logout();
    }

}
