package com.doctordroid.presentation.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.doctordroid.R;
import com.doctordroid.presentation.home.About.AboutFragment;
import com.doctordroid.presentation.home.Chats.ChatsFragment;
import com.doctordroid.presentation.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import javax.annotation.Nonnull;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // UI
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton navTrigger;
    private ChatsFragment chatsFragment;
    private AboutFragment aboutFragment;
    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    // data
    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.home_nav);
        navTrigger = findViewById(R.id.nav_trigger);

        chatsFragment = new ChatsFragment();
        aboutFragment = new AboutFragment();

        fragmentManager = getSupportFragmentManager();
        displayFragment(chatsFragment);

        navigationView.setNavigationItemSelectedListener(this);

        navTrigger.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    private void displayFragment (Fragment fragment) {

        if (fragment == activeFragment) return;

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_frame, fragment).commit();
        activeFragment = fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@Nonnull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_home:
                displayFragment(chatsFragment);
                break;

            case R.id.action_about:
                displayFragment(aboutFragment);
                break;

            case R.id.action_logout:
                logout();
                break;
        }

        // Close the navigation drawer
        drawerLayout.closeDrawers();

        return true;
    }

    private void logout () {
        viewModel.logout();
        Toast.makeText(this, "Good Bye :)", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
