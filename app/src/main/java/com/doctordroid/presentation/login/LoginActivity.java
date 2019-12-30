package com.doctordroid.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.doctordroid.R;
import com.doctordroid.presentation.home.HomeActivity;
import com.doctordroid.presentation.register.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private EditText emailText, passwordText;
    private AppCompatButton loginButton;
    private AppCompatTextView goRegisterButton;
    private AppCompatImageButton loginGoogleButton, loginFacebookButton;

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
        loginGoogleButton = findViewById(R.id.login_with_google_button);
        loginFacebookButton = findViewById(R.id.login_with_facebook_button);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        if (viewModel.isAuthenticated() || GoogleSignIn.getLastSignedInAccount(this) != null) goHome();

        loginButton.setOnClickListener(this::login);
        goRegisterButton.setOnClickListener(this::goRegister);
        loginGoogleButton.setOnClickListener(this::loginGoogle);
        loginFacebookButton.setOnClickListener(this::loginFacebook);
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

    public void loginGoogle (View view) {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    public void loginFacebook (View view) {

    }

    public void goRegister (View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    // redirect to the home screen
    public void goHome () {
        Toast.makeText(getApplicationContext(), "Welcome home :)", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                goHome();
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
