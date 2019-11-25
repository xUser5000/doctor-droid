package com.doctordroid.presentation.auth.login;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.doctordroid.R;
import com.doctordroid.presentation.home.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements OnLoginListener {

    // ui
    private View parent;

    private EditText emailText, passwordText;

    // data
    private LoginViewModel viewModel;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_login, container, false);
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailText = parent.findViewById(R.id.login_email_text);
        passwordText = parent.findViewById(R.id.login_password_text);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public void login() {

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.login(email, password).subscribe(new Observer<FirebaseUser>() {
            @Override
            public void onSubscribe(Disposable d) {
                Toast.makeText(getContext(), "Processing Request ...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(FirebaseUser user) {
                Toast.makeText(getContext(), "Welcome home :)", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), HomeActivity.class));
                getActivity().finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
