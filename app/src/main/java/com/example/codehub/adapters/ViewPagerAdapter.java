package com.example.codehub.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.codehub.pager.CanceledFragment;
import com.example.codehub.pager.ConfirmationFragment;
import com.example.codehub.pager.DoneFragment;
import com.example.codehub.pager.ProgressFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ConfirmationFragment();
            case 1:
                return new ProgressFragment();
            case 2:
                return new DoneFragment();
            case 3:
                return new CanceledFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Đang tiến hành";
                break;
            case 2:
                title = "Đã xong";
                break;
            case 3:
                title = "Bị từ chối";
                break;
            default:
                title = "";
                break;
        }
        return title;
    }
}
