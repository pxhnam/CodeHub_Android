package com.example.codehub.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.codehub.databinding.ActivityMainBinding;
import com.example.codehub.fragments.HomeFragment;
import com.example.codehub.fragments.NotificationsFragment;
import com.example.codehub.fragments.TaskFragment;
import com.example.codehub.R;
import com.example.codehub.fragments.SearchFragment;
import com.example.codehub.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.navBottom.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                replaceFragment(new SearchFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_notifications) {
                replaceFragment(new NotificationsFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_task) {
                replaceFragment(new TaskFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                replaceFragment(new SettingsFragment());
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}