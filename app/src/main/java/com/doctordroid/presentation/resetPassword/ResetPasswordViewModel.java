package com.doctordroid.presentation.resetPassword;

import androidx.lifecycle.ViewModel;

import com.doctordroid.data.repository.AuthRepository;
import com.google.android.gms.tasks.Task;

public class ResetPasswordViewModel extends ViewModel {

    private final AuthRepository authRepository = AuthRepository.getInstance();

    public Task<Void> sendResetPasswordEmail (String email) {
        return authRepository.sendResetPasswordEmail(email);
    }

}
