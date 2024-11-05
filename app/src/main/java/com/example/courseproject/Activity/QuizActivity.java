package com.example.courseproject.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Adapter.QuestionAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Answer;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.NonScrollableRecyclerView;
import com.example.courseproject.Model.Question;
import com.example.courseproject.Model.Quiz;
import com.example.courseproject.Model.QuizResult;
import com.example.courseproject.R;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.QuestionService;
import com.example.courseproject.Service.QuizResultService;
import com.example.courseproject.Service.QuizService;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvNameQuiz;
    private NonScrollableRecyclerView rcvQuestion;
    private QuestionAdapter questionAdapter;
    private Intent intent;
    private Button btnSubmit;
    private int currentQuestionIndex = 0;
    private ImageView rightArrow ,leftArrow;
    private TextView tvQuestionNumber;
    private LinearProgressIndicator progressIndicator;
    private TextView tvTimeQuiz;
    private int quizID = 0,enrollmentId = 0;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        intent = getIntent();


        //
        btnBack = findViewById(R.id.backBtn);
        rcvQuestion = findViewById(R.id.questionList);
        btnSubmit = findViewById(R.id.btn_submit);
        rightArrow = findViewById(R.id.rightArrow);
        leftArrow = findViewById(R.id.leftArrow);
        tvQuestionNumber = findViewById(R.id.questionNumber);
        progressIndicator = findViewById(R.id.progressBar);
        tvNameQuiz = findViewById(R.id.tv_name_quiz);
        tvTimeQuiz = findViewById(R.id.tv_time_quiz);
        layout = findViewById(R.id.main);

        //
        questionAdapter = new QuestionAdapter(this);
        rcvQuestion.setAdapter(questionAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        //
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //
        callApiQuiz();
        callApiEnrollmentId();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                HashMap<Question, Answer> hashMap = questionAdapter.getAnswerHashMap();
                for (HashMap.Entry<Question, Answer> entry : hashMap.entrySet()) {
                    Log.d("answer selected", entry.getKey().getContent() + "");
                    Log.e("answer selected", entry.getValue().getContent());
                }*/
                dialogConfirm();
            }
        });
        //
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questionAdapter.getItemCount() - 1) {
                    currentQuestionIndex++; // Tăng chỉ số câu hỏi hiện tại
                    int question = currentQuestionIndex +1;
                    tvQuestionNumber.setText("Question "+question +"/"+questionAdapter.getItemCount());
                    rcvQuestion.smoothScrollToPosition(currentQuestionIndex); // Cuộn đến câu hỏi tiếp theo
                    questionAdapter.notifyDataSetChanged();
                    //
                    progressIndicator.setMax(questionAdapter.getItemCount()-1);
                    progressIndicator.setProgress(currentQuestionIndex);
                }
            }
        });

        leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    int question = currentQuestionIndex +1;
                    // Giảm chỉ số câu hỏi hiện tại
                    tvQuestionNumber.setText("Question "+question+"/"+questionAdapter.getItemCount());
                    rcvQuestion.smoothScrollToPosition(currentQuestionIndex);
                    questionAdapter.notifyDataSetChanged();// Cuộn về câu hỏi trước đó
                    progressIndicator.setProgress(currentQuestionIndex);
                }
            }
        });

    }

    private void dialogConfirm() {
        final Dialog dialog = new Dialog(QuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_submit);
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

        TextView tvConfirm,tvCancel;
        tvConfirm = dialog.findViewById(R.id.tv_submit);
        tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();;
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                totalScore();
            }
        });
        dialog.show();
    }

    private void totalScore() {
        double score = 0;
        HashMap<Question, Answer> hashMap = questionAdapter.getAnswerHashMap();
        for (HashMap.Entry<Question, Answer> entry : hashMap.entrySet()) {
            if (entry.getValue().isCorrect()) {
                score = score + entry.getKey().getPoints() ;
            }
        }
        callApiQuizResult(score);
    }

    private void callApiQuizResult(double score) {
        LocalDateTime now = LocalDateTime.now();
        // Định dạng theo chuẩn ISO 8601
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");

        String formattedDate = now.format(formatter);
        QuizResult quizResult = new QuizResult(0,quizID,enrollmentId,(int)score,formattedDate);
        QuizResultService apiService = ApiClient.getClient(false).create(QuizResultService.class);
        apiService.addQuizResult(quizResult).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                 if (response.isSuccessful()){
                    dialogQuizResult(score);
                }

            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.d("quiz", "false");
            }
        });
    }

    private void dialogQuizResult(double score) {
        final Dialog dialog = new Dialog(QuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_result);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(R.drawable.transparent_background);
        }
        dialog.setCancelable(false);

        TextView tvPoint = dialog.findViewById(R.id.tvPoint);
        ImageView btnCancel = dialog.findViewById(R.id.btn_cancel);
        tvPoint.setText(score+"");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                dialog.dismiss();
                finish();
            }
        });
        layout.setVisibility(View.INVISIBLE);
        dialog.show();
    }

    private void callApiEnrollmentId() {
        EnrollmentService apiService = ApiClient.getClient(true).create(EnrollmentService.class);
        apiService.getEnrollmentByUserIdAndCourseId(DataLocalManager.getUserId(),intent.getIntExtra("courseId",0)).enqueue(new Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Enrollment enrollment = response.body();
                    enrollmentId =  enrollment.getEnrollmentId();
                    Log.e("enrollmentID", enrollmentId+"" );
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

    private void callApiQuiz(){
        int courseId = intent.getIntExtra("courseId",0);
        Log.e("courseID",courseId+"");
        QuizService apiService = ApiClient.getClient(true).create(QuizService.class);
        apiService.getQuizByCourseId(courseId).enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Quiz quiz = response.body();
                    quizID = quiz.getQuizId();
                    callApiQuestion(quiz.getQuizId());
                    tvNameQuiz.setText(quiz.getTitle());
                    tvTimeQuiz.setText(quiz.getTime()+ " min");
                }
            }

            @Override
            public void onFailure(Call<Quiz> call, Throwable t) {
                Log.e("Fail to call api quiz",t.getMessage());
            }
        });

    }
    private void callApiQuestion(int quizId) {
        QuestionService apiService = ApiClient.getClient(true).create(QuestionService.class);
        apiService.getAllQuestionByQuizId(quizId).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<Question> questionList = response.body();
                    questionAdapter.setData(questionList);
                    questionAdapter.notifyDataSetChanged();
                    tvQuestionNumber.setText("Question 1"+"/"+questionList.size());

                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("Fail to call api question",t.getMessage());
            }
        });
    }
}