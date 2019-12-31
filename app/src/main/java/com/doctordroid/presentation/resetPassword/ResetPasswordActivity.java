package com.doctordroid.presentation.resetPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.doctordroid.R;

public class ResetPasswordActivity extends AppCompatActivity {

    // ui
    private AppCompatEditText emailText;
    private AppCompatButton sendEmailButton;

    // data
    private ResetPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailText = findViewById(R.id.reset_password_email_text);
        sendEmailButton = findViewById(R.id.reset_password_send_button);

        viewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel.class);

        sendEmailButton.setOnClickListener(this::sendResetPasswordEmail);
    }

    public void sendResetPasswordEmail (View view) {
        String email = emailText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You should provide your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.sendResetPasswordEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                task.getException().printStackTrace();
                Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
