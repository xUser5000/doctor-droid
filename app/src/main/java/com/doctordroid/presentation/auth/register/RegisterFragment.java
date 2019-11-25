package com.doctordroid.presentation.auth.register;

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
public class RegisterFragment extends Fragment implements OnRegisterListener {

    // ui
    private View parent;
    private EditText emailText, passwordText, confirmPasswordText;

    // data
    private RegisterViewModel viewModel;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_register, container, false);
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailText = parent.findViewById(R.id.register_email_text);
        passwordText = parent.findViewById(R.id.register_password_text);
        confirmPasswordText = parent.findViewById(R.id.register_confirm_password_text);

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
    }

    @Override
    public void register() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Your password must be at least 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "You must confirm your password correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.register(email, password).subscribe(new Observer<FirebaseUser>() {
            @Override
            public void onSubscribe(Disposable d) {
                Toast.makeText(getContext(), "Creating a new account...", Toast.LENGTH_SHORT).show();
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
