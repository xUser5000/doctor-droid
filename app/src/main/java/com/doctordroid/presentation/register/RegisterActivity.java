package com.doctordroid.presentation.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProviders;

import com.doctordroid.R;
import com.doctordroid.presentation.home.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends AppCompatActivity {

    // ui
    private EditText usernameText, emailText, passwordText, confirmPasswordText;
    private AppCompatButton registerButton;

    // data
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.register_username_text);
        emailText = findViewById(R.id.register_email_text);
        passwordText = findViewById(R.id.register_password_text);
        confirmPasswordText = findViewById(R.id.register_confirm_password_text);

        registerButton = findViewById(R.id.register_button);

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        registerButton.setOnClickListener(this::register);
    }

    public void register (View view) {
        String username = usernameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();

        if (
                TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Your password must be at least 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "You must confirm your password correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.register(email, password).subscribe(new Observer<FirebaseUser>() {
            @Override
            public void onSubscribe(Disposable d) {
                Toast.makeText(getApplicationContext(), "Creating a new account...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(FirebaseUser user) {
                Toast.makeText(getApplicationContext(), "Welcome home :)", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {}
        });
    }
}
