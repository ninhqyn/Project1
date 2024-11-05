package com.example.courseproject.Activity.ui.learning;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.courseproject.CustomViewPager.MyViewPagerLearningAdapter;
import com.example.courseproject.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyLearningFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mylearning, container, false);
        tabLayout = v.findViewById(R.id.tab_layout_learning);
        viewPager2 = v.findViewById(R.id.view_pager_learning);

        viewPager2.setAdapter(new MyViewPagerLearningAdapter(getActivity()));

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position)->{
                switch (position){
                    case 0:
                        tab.setText("Đang tiến hành");
                        break;
                    case 1:
                        tab.setText("Hoàn thành");
                        break;
                }
            }
        ).attach();
        return v;
    }
}