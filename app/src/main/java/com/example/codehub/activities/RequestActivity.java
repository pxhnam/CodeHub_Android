package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.codehub.R;
import com.example.codehub.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class RequestActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        init();
        ViewPagerAdapter adtViewPager = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adtViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        btnBack.setOnClickListener(v -> finish());
    }

    private void init() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        btnBack = findViewById(R.id.btnBack);
    }
}