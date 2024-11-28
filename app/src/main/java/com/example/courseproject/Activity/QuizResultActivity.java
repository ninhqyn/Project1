package com.example.courseproject.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Certificate;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.QuizResult;
import com.example.courseproject.R;
import com.example.courseproject.Service.CertificateService;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.Percentage;
import com.example.courseproject.Service.QuizResultService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizResultActivity extends AppCompatActivity {
    Enrollment enrollment;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        enrollment = (Enrollment) getIntent().getSerializableExtra("enrollment");
        intent = getIntent();

        showResult(getIntent().getDoubleExtra("score",0));
    }

    private void showResult(double score) {

        TextView tvPoint = findViewById(R.id.tvPoint);
        ImageView btnCancel = findViewById(R.id.btn_cancel);
        tvPoint.setText(score+"");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        callApiPercentage();

    }
    private void callApiPercentage() {
        QuizResultService api = ApiClient.getClient(true).create(QuizResultService.class);
        api.getPercentage(enrollment.getCourseId(),enrollment.getEnrollmentId()).enqueue(new Callback<Percentage>() {
            @Override
            public void onResponse(Call<Percentage> call, Response<Percentage> response) {
                if(response.isSuccessful() && response.body()!=null){
                    double percentageValue = response.body().grade(); // Get the grade
                    if(percentageValue >= 80){
                        UpdateEnrollment(percentageValue);
                    }else {
                        Log.d("Response","fail");

                    }
                }
            }

            @Override
            public void onFailure(Call<Percentage> call, Throwable t) {

            }
        });

    }
    private void UpdateEnrollment(double percentage){
        Enrollment update = enrollment;
        update.setStatus("completed");
        update.setHasCertificate(true);
        Log.d("Response",update.getEnrollmentId()+"");
        Log.d("Response",update.getCourseId()+"");
        Log.d("Response",update.getUserId()+"");
        Log.d("Response",update.getStatus()+"");
        Log.d("Response",update.getEnrolledAt()+"");
        Log.d("Response",update.isHasCertificate()+"");



        EnrollmentService apiService = ApiClient.getClient(false).create(EnrollmentService.class);
        apiService.updateEnrollment(enrollment.getEnrollmentId(),update).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                Log.d("Response","call");
                if(response.isSuccessful()){
                    callApiAddCertificate(percentage);
                    Log.d("Response","call success");
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.d("Response","fail to call api");
                t.printStackTrace();
                Log.e("Response", "Error message: " + t.getMessage());
            }
        });

    }
    private void callApiAddCertificate(double percentage) {
        Certificate certificate = new Certificate(0,enrollment.getEnrollmentId(),percentage,"2024-11-27T17:56:30.046Z");
        CertificateService apiService = ApiClient.getClient(true).create(CertificateService.class);
        apiService.addCertificate(certificate).enqueue(new Callback<Certificate>() {
            @Override
            public void onResponse(Call<Certificate> call, Response<Certificate> response) {
                if(response.isSuccessful()){
                    Log.d("addCertificate","success");
                }
                else {
                    Log.d("addCertificate","fail");
                }
            }

            @Override
            public void onFailure(Call<Certificate> call, Throwable t) {

            }
        });
    }

}