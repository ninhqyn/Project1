package com.example.courseproject.ZaloPay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.SharedPerferences.DataLocalManager;

public class ResultActivity extends AppCompatActivity {
    ImageButton btnBack;
    ImageView imgResult;
    TextView tvResult;
    Button btnGoToCourse;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        course = (Course) getIntent().getSerializableExtra("course");
        btnBack = findViewById(R.id.btn_back);
        imgResult = findViewById(R.id.img_result);
        tvResult = findViewById(R.id.tv_result);
        btnGoToCourse = findViewById(R.id.btn_go_to_course);
        String result = getIntent().getStringExtra("result");
        if(result.equals("success")){
            imgResult.setImageResource(R.drawable.payment_success);
            tvResult.setText("Thanh toán thành công");
            btnGoToCourse.setVisibility(View.VISIBLE);
        }
        else if(result.equals("canceled")){
            imgResult.setImageResource(R.drawable.payment_cancel);
            tvResult.setText("Thanh toán bị hủy");
        }

        else{
            imgResult.setImageResource(R.drawable.payment_error);
            tvResult.setText("Thanh toán thất bại");
        }
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnGoToCourse.setOnClickListener(v -> {
            DataLocalManager.setPayment(true);
            Intent intent = new Intent(ResultActivity.this, CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_course",course);
            intent.putExtras(bundle);
            startActivity(intent);
        });


    }
}