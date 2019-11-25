package com.doctordroid.presentation.auth.register;

import android.arch.lifecycle.ViewModel;

import com.doctordroid.data.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {

    private AuthRepository authRepository = AuthRepository.getInstance();

    Observable<FirebaseUser> register (String email, String password) {
        return Observable.create((ObservableOnSubscribe<FirebaseUser>) emitter -> authRepository.register(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) emitter.onNext(task.getResult().getUser());
            else emitter.onError(task.getException());
            emitter.onComplete();
        })).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
