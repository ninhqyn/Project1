package com.example.courseproject.CustomViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.courseproject.Activity.ui.learning.ui.CompletedFragment;
import com.example.courseproject.Activity.ui.learning.ui.ProgressFragment;

public class MyViewPagerLearningAdapter extends FragmentStateAdapter {
    public MyViewPagerLearningAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ProgressFragment();
            case 1:
                return new CompletedFragment();
            default:
                return new ProgressFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
