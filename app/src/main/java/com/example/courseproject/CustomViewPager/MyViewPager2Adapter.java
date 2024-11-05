package com.example.courseproject.CustomViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.courseproject.Activity.ui.account.AccountFragment;
import com.example.courseproject.Activity.ui.learning.MyLearningFragment;
import com.example.courseproject.Activity.ui.home.HomeFragment;
import com.example.courseproject.Activity.ui.favorite.FavoriteFragment;

public class MyViewPager2Adapter extends FragmentStateAdapter {

    public MyViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new MyLearningFragment();
            case 2:
                return new FavoriteFragment();
            case 3:
                return new AccountFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
