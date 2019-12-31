package com.doctordroid.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.doctordroid.R;
import com.doctordroid.presentation.home.HomeActivity;
import com.doctordroid.presentation.register.RegisterActivity;
import com.doctordroid.presentation.resetPassword.ResetPasswordActivity;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private EditText emailText, passwordText;
    private AppCompatButton loginButton;
    private AppCompatTextView goRegisterButton, resetPassword;

    // data
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.login_email_text);
        passwordText = findViewById(R.id.login_password_text);

        loginButton = findViewById(R.id.login_button);
        goRegisterButton = findViewById(R.id.login_go_to_register_button);
        resetPassword = findViewById(R.id.login_reset_password);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        if (viewModel.isAuthenticated()) goHome();

        loginButton.setOnClickListener(this::login);
        goRegisterButton.setOnClickListener(this::goRegister);
        resetPassword.setOnClickListener(this::goResetPassword);
    }

    public void login (View view) {

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.login(email, password).subscribe(new Observer<FirebaseUser>() {
            @Override
            public void onSubscribe(Disposable d) {
                Toast.makeText(getApplicationContext(), "Processing Request ...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(FirebaseUser user) {
                goHome();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {}
        });
    }

    public void goRegister (View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void goResetPassword (View view) {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    // redirect to the home screen
    public void goHome () {
        Toast.makeText(getApplicationContext(), "Welcome home :)", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

}
