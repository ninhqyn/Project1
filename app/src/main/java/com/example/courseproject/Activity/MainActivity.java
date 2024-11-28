package com.example.courseproject.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.courseproject.CustomViewPager.ViewPager2Adapter;
import com.example.courseproject.R;
import com.example.courseproject.Receiver.NetWorkReceiver;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private CircleIndicator3 indicator;
    List<Integer> imageList = new ArrayList<>();
    private Handler handler;
    private Runnable runnable;
    private int currentIndex = 0;

    private NetWorkReceiver networkReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        networkReceiver = new NetWorkReceiver();
        viewPager2 = findViewById(R.id.imageView);
        indicator = findViewById(R.id.indicator);
        imageList.add(R.drawable.dashbord_img);
        imageList.add(R.drawable.on_board_1);
        imageList.add(R.drawable.on_board_2);

        ViewPager2Adapter adapter = new ViewPager2Adapter(this, imageList);
        viewPager2.setAdapter(adapter);

        // Setup the indicator if needed
        indicator.setViewPager(viewPager2);
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        // Automatic sliding
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex >= imageList.size()) {
                    currentIndex = 0; // Loop back to the start
                }
                viewPager2.setCurrentItem(currentIndex++, true);
                handler.postDelayed(this, 3000); // Schedule next slide
            }
        };
        handler.postDelayed(runnable, 3000); // Start sliding after 3000 milliseconds

        Button btnBatDau = findViewById(R.id.btnBatDau);
        btnBatDau.setOnClickListener(view -> {
            if (DataLocalManager.isUserLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, TrangChuActitvity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký BroadcastReceiver khi Activity được hiển thị
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký BroadcastReceiver khi Activity không còn hiển thị
        unregisterReceiver(networkReceiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to avoid memory leaks
        handler.removeCallbacks(runnable);
    }
}