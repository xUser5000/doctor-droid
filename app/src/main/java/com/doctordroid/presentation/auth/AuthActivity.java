package com.doctordroid.presentation.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctordroid.R;
import com.doctordroid.databinding.ActivityAuthBinding;
import com.doctordroid.presentation.auth.login.LoginFragment;
import com.doctordroid.presentation.auth.register.RegisterFragment;
import com.doctordroid.presentation.home.HomeActivity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.doctordroid.presentation.auth.FlexibleFrameLayout.ORDER_LOGIN_STATE;
import static com.doctordroid.presentation.auth.FlexibleFrameLayout.ORDER_SIGN_UP_STATE;

public class AuthActivity extends AppCompatActivity {

    /// ui
    private static final String TAG = "MainActivity";
    private ActivityAuthBinding binding;
    private boolean isLogin = true;

    // data
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);

        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        if (viewModel.isAuthenticated()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment, loginFragment)
                .replace(R.id.sign_up_fragment, registerFragment)
                .commit();

        binding.loginFragment.setRotation(-90);

        binding.button.setOnRegisterListener(registerFragment);
        binding.button.setOnLoginListener(loginFragment);

        binding.button.setOnButtonSwitched(isLogin -> binding.getRoot()
                .setBackgroundColor(ContextCompat.getColor(
                        this,
                        isLogin ? R.color.colorPrimary : R.color.colorAccent)));

        binding.loginFragment.setVisibility(INVISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.loginFragment.setPivotX(binding.loginFragment.getWidth() / 2);
        binding.loginFragment.setPivotY(binding.loginFragment.getHeight());
        binding.signUpFragment.setPivotX(binding.signUpFragment.getWidth() / 2);
        binding.signUpFragment.setPivotY(binding.signUpFragment.getHeight());
    }

    public void switchFragment(View v) {
        if (isLogin) {
            binding.loginFragment.setVisibility(VISIBLE);
            binding.loginFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.signUpFragment.setVisibility(INVISIBLE);
                    binding.signUpFragment.setRotation(90);
                    binding.wrapper.setDrawOrder(ORDER_LOGIN_STATE);
                }
            });
        } else {
            binding.signUpFragment.setVisibility(VISIBLE);
            binding.signUpFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.loginFragment.setVisibility(INVISIBLE);
                    binding.loginFragment.setRotation(-90);
                    binding.wrapper.setDrawOrder(ORDER_SIGN_UP_STATE);
                }
            });
        }

        isLogin = !isLogin;
        binding.button.startAnimation();
    }
}
