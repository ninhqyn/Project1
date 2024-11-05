package com.example.courseproject.Activity.ui.coursedetail.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.courseproject.Activity.QuizActivity;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.QuizResult;
import com.example.courseproject.R;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.Percentage;
import com.example.courseproject.Service.QuizResultService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

    private boolean isEnroll = false;
    Button btnStart;

    private boolean isFirsTime = true;
    private int enrollmentId;
    private TextView tvGrade,tvDanhGia,tvContentDanhGia;
    private TextView tvDateEnd,tvTimeEnd;
    private TextView tvDateNow,tvDate;;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isEnroll = getArguments().getBoolean("isEnroll");
        if(isEnroll){
            callApiEnrollmentId();
        }
        // Inflate the layout for this
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        btnStart = v.findViewById(R.id.btn_submit);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnroll){
                    Intent intent = new Intent(getContext(), QuizActivity.class);
                    intent.putExtra("courseId",getArguments().getInt("courseId"));
                    startActivityForResult(intent, 1);
                }
            }
        });

        tvDateNow = v.findViewById(R.id.tv_date_time_now);
        tvDate = v.findViewById(R.id.tv_date);
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Date currentDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String formattedDate = formatter.format(currentDate);
                tvDateNow.setText(formattedDate);
                handler.postDelayed(this, 1000); // Update every 1 second
            }
        };
        handler.post(runnable);
        //

        tvDateEnd = v.findViewById(R.id.tv_time_end);
        tvTimeEnd = v.findViewById(R.id.time_end);


        tvGrade = v.findViewById(R.id.tv_grade);
        tvDanhGia = v.findViewById(R.id.textView9);
        tvContentDanhGia = v.findViewById(R.id.textView10);
        if(isFirsTime){

            tvDateEnd.setVisibility(View.GONE);
            tvTimeEnd.setVisibility(View.GONE);
            tvGrade.setText("_ _");
            tvDanhGia.setText("Đánh giá của bạn");
            tvContentDanhGia.setText("Bạn chưa làm lần nào chúng tôi sẽ giữ điểm cao nhất của bạn");
        }else {
            tvDateEnd.setVisibility(View.VISIBLE);
            tvTimeEnd.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.GONE);
            tvDateNow.setVisibility(View.GONE);
            btnStart.setText("Làm lại");
        }

        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // Call the API again to refresh the fragment data
                callApiEnrollmentId(); // Re-fetch enrollment and quiz results
            }
        }
    }

    private void callApiQuiz() {
        QuizResultService api = ApiClient.getClient(true).create(QuizResultService.class);
        api.getHighestQuizResult(getArguments().getInt("courseId"),enrollmentId).enqueue(new Callback<QuizResult>() {
            @Override
            public void onResponse(Call<QuizResult> call, Response<QuizResult> response) {
                Log.d("Response", "onResponse:"+response.message());
                Log.d("Response", "onResponse:"+getArguments().getInt("courseId")+" "+ enrollmentId + "");
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        QuizResult quizResult = response.body();
                         tvTimeEnd.setText(quizResult.getCreatedAt());
                         tvDateEnd.setVisibility(View.VISIBLE);
                         tvTimeEnd.setVisibility(View.VISIBLE);
                         callApiPercentage();
                    }
                }else {
                    Log.d("error","erorr");
                }
            }

            @Override
            public void onFailure(Call<QuizResult> call, Throwable t) {
                Log.d("Fail to call api quizresult",t.getMessage());
            }
        });
    }

    private void callApiPercentage() {
        QuizResultService api = ApiClient.getClient(true).create(QuizResultService.class);
        api.getPercentage(getArguments().getInt("courseId"),enrollmentId).enqueue(new Callback<Percentage>() {
            @Override
            public void onResponse(Call<Percentage> call, Response<Percentage> response) {
                Log.d("Response", "onResponse:"+response.message());
                if(response.isSuccessful() && response.body()!=null){
                    double percentageValue = response.body().grade(); // Get the grade
                    if(percentageValue > 80){
                        tvContentDanhGia.setText("Bạn đã đủ điều kiện để nhận chứng chỉ");
                    }else {
                        tvContentDanhGia.setText("Để đạt được chứng chỉ điểm của bạn > 80%");
                    }
                    // Format the output
                    String formattedPercentage;
                    if (percentageValue % 1 == 0) { // Check if it's a whole number
                        formattedPercentage = String.format("%.0f%%", percentageValue); // No decimal places
                    } else {
                        formattedPercentage = String.format("%.2f%%", percentageValue); // Two decimal places
                    }
                    tvGrade.setText(formattedPercentage); // Set formatted text
                    btnStart.setText("Làm lại");
                }
            }

            @Override
            public void onFailure(Call<Percentage> call, Throwable t) {

            }
        });

    }

    private void callApiEnrollmentId() {
        EnrollmentService apiService = ApiClient.getClient(true).create(EnrollmentService.class);
        apiService.getEnrollmentByUserIdAndCourseId(DataLocalManager.getUserId(),getArguments().getInt("courseId")).enqueue(new Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Enrollment enrollment = response.body();
                    enrollmentId =  enrollment.getEnrollmentId();
                    callApiQuiz();
                    Log.e("enrollmentID", enrollmentId+"1" );
                }
                else {
                    Log.e("enrollmentID", "Chua dang ky" );
                }
            }

            @Override
            public void onFailure(Call<Enrollment> call, Throwable t) {
                Log.e("enrollmentID", "eror" );
            }
        });
    }

    public void setEnrollStatus(boolean enroll) {
        this.isEnroll = enroll;
        callApiEnrollmentId();
    }
}