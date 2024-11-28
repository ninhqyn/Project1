package com.example.courseproject.Activity.ui.coursedetail.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseproject.Activity.CertificateActivity;
import com.example.courseproject.Activity.CertificateDetailActivity;
import com.example.courseproject.Activity.QuizActivity;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.CalculateProgress;
import com.example.courseproject.Model.Certificate;
import com.example.courseproject.Model.CertificateTemplate;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.DateUtils;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.QuizResult;
import com.example.courseproject.R;
import com.example.courseproject.Service.CertificateService;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.Percentage;
import com.example.courseproject.Service.ProgressService;
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
    private TextView tvDateNow,tvDate;
    private double progress;
    private TextView tvCertificate;
    private TextView tvSeeCertificate;
    Enrollment enrollment;
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
                    if(progress == 100){
                        Intent intent = new Intent(getContext(), QuizActivity.class);
                        intent.putExtra("courseId",getArguments().getInt("courseId"));
                        startActivityForResult(intent, 1);
                    }else {
                        createDialog();
                    }
                }else {
                    Toast.makeText(getContext(),"Chưa đăng ký",Toast.LENGTH_SHORT).show();
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

        tvCertificate = v.findViewById(R.id.tv_certificate);
        tvSeeCertificate = v.findViewById(R.id.see_certificate);
        tvCertificate.setVisibility(View.GONE);
        tvSeeCertificate.setVisibility(View.GONE);


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
        tvSeeCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiCertificate();
            }
        });

        return v;
    }

    private void callApiCertificate() {
        Log.d("Certificate",enrollmentId+"");
        CertificateService certificateService = ApiClient.getClient(true).create(CertificateService.class);
        certificateService.getCertificateByEnrollmentId(enrollmentId).enqueue(new Callback<Certificate>() {
            @Override
            public void onResponse(Call<Certificate> call, Response<Certificate> response) {
                Log.d("Certificate",response.message());
                if(response.isSuccessful() && response.body()!=null){
                    Certificate certificate = response.body();
                    callApiCertifcateTemplate(certificate);
                }
            }

            @Override
            public void onFailure(Call<Certificate> call, Throwable t) {
                Log.d("Certificate",t.getMessage());
            }
        });
    }

    private void callApiCertifcateTemplate(Certificate certificate) {
        Log.d("Certificate","call temp");
        CertificateService certificateService = ApiClient.getClient(true).create(CertificateService.class);
        certificateService.getCertificateTemplateEnrollmentId(enrollmentId).enqueue(new Callback<CertificateTemplate>() {
            @Override
            public void onResponse(Call<CertificateTemplate> call, Response<CertificateTemplate> response) {
                Log.d("Certificate","call"+ response.message());
                if(response.body()!=null && response.isSuccessful()){
                    CertificateTemplate template = response.body();
                    Intent intent = new Intent(getContext(),CertificateDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("certificate",certificate);
                    bundle.putSerializable("template",template);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CertificateTemplate> call, Throwable t) {

            }
        });
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.5f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(R.drawable.background_loading);
        }
        dialog.setCancelable(false);
        TextView tvClose = dialog.findViewById(R.id.tv_close);
        ImageView imgCancel = dialog.findViewById(R.id.img_cancel);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
    private void callApiProgress(){
        ProgressService apiService = ApiClient.getClient(true).create(ProgressService.class);
        apiService.calculateProgress(enrollmentId).enqueue(new Callback<CalculateProgress>() {
            @Override
            public void onResponse(Call<CalculateProgress> call, Response<CalculateProgress> response) {

                if(response.isSuccessful() && response.body()!=null){
                    CalculateProgress calculateProgress = response.body();
                    progress = calculateProgress.getProgress();

                }
            }

            @Override
            public void onFailure(Call<CalculateProgress> call, Throwable t) {

            }
        });

    }


    private void callApiQuiz() {
        QuizResultService api = ApiClient.getClient(true).create(QuizResultService.class);
        api.getHighestQuizResult(getArguments().getInt("courseId"),enrollmentId).enqueue(new Callback<QuizResult>() {
            @Override
            public void onResponse(Call<QuizResult> call, Response<QuizResult> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        QuizResult quizResult = response.body();
                        String date = DateUtils.convertToDateTime(quizResult.getCreatedAt());
                        if(date!=null){
                            tvTimeEnd.setText(date);
                        }else {
                            tvTimeEnd.setText("26/09/2024");
                        }

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
                    if(percentageValue >= 80){
                        tvContentDanhGia.setText("Bạn đã nhận được chứng chỉ");
                        tvCertificate.setVisibility(View.VISIBLE);
                        tvSeeCertificate.setVisibility(View.VISIBLE);
                    }else {
                        tvContentDanhGia.setText("Để đạt được chứng chỉ điểm của bạn >= 80%");
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
                    Enrollment en = response.body();
                    enrollment = response.body();
                    enrollmentId =  en.getEnrollmentId();
                    callApiProgress();
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