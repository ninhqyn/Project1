package com.example.courseproject.Model;

import android.webkit.WebChromeClient;

import com.example.courseproject.Activity.LessonDetailActivity;
import com.example.courseproject.R;

import android.view.View;
import android.webkit.WebChromeClient;

public class MyWebChromeClient extends WebChromeClient {
    private final LessonDetailActivity activity;

    public MyWebChromeClient(LessonDetailActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        // Xử lý hiển thị chế độ toàn màn hình
        activity.setContentView(view);
        // Thêm callback vào các thành phần để xử lý khi thoát chế độ toàn màn hình
    }

    @Override
    public void onHideCustomView() {
        // Thoát chế độ toàn màn hình
        activity.setContentView(R.layout.activity_main);
        activity.recreate(); // Tái tạo lại Activity để khôi phục WebView
    }

}

