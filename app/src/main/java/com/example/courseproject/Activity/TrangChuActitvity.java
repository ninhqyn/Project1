package com.example.courseproject.Activity;

import static com.example.courseproject.R.id.*;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.courseproject.CustomViewPager.MyViewPager2Adapter;
import com.example.courseproject.R;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.example.courseproject.databinding.ActivityTrangChuActitvityBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class TrangChuActitvity extends AppCompatActivity {

    private ViewPager2 mviewPager2;
    private BottomNavigationView mbottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrangChuActitvityBinding binding = ActivityTrangChuActitvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mviewPager2 = binding.viewPager2; // Thay thế bằng ID thực tế trong layout
        mbottomNavigationView = binding.bottomNavigation; // Thay thế bằng ID thực tế trong layout


        MyViewPager2Adapter adapter = new MyViewPager2Adapter(this);
        mviewPager2.setAdapter(adapter);
        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mbottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        mbottomNavigationView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                        break;
                    case 2:
                        mbottomNavigationView.getMenu().findItem(navigation_notifications).setChecked(true);
                        break;
                    case 3:
                        mbottomNavigationView.getMenu().findItem(navigation_acount).setChecked(true);
                        break;
                }
            }
        });
        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.navigation_home){
                        mviewPager2.setCurrentItem(0);
                }
                if(item.getItemId()== navigation_dashboard){
                    mviewPager2.setCurrentItem(1);
                }
                if(item.getItemId()== navigation_acount){
                    mviewPager2.setCurrentItem(3);
                }
                if(item.getItemId()== navigation_notifications){
                    mviewPager2.setCurrentItem(2);
                }
                return true;
            }
        });

    }


}
