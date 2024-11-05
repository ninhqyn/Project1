package com.example.courseproject.Activity.ui.coursedetail.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.courseproject.Activity.LessonDetailActivity;
import com.example.courseproject.Adapter.LessonAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickItemLessonListener;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.Lesson;
import com.example.courseproject.R;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.LessonService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar loadingLesson;
    private LessonAdapter lessonAdapter;
    private TextView textCountLesson ;
    private boolean isEnroll = false;
    int enrollmentId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isEnroll = getArguments().getBoolean("isEnroll");
        View v = inflater.inflate(R.layout.fragment_lesson, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        loadingLesson = v.findViewById(R.id.loading_lesson);
        textCountLesson = v.findViewById(R.id.tv_count_lesson);
        callApiEnrollment();
        lessonAdapter = new LessonAdapter(getContext(), new IClickItemLessonListener() {
            @Override
            public void onClickItemLesson(Lesson lesson) {
                Log.d("status", "onClickItemLesson: " + isEnroll+"");
                if(isEnroll){
                    Intent intent = new Intent(getContext(), LessonDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object_lesson",lesson);
                    bundle.putInt("enrollmentId",enrollmentId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(lessonAdapter);
        recyclerView.setHasFixedSize(true);
        loadingLesson.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        int courseId = getArguments().getInt("courseId");

        getListLessonByCourseId(courseId);
        return v;
    }

    private void callApiEnrollment() {
        EnrollmentService apiService = ApiClient.getClient(true).create(EnrollmentService.class);
        apiService.getEnrollmentByUserIdAndCourseId(DataLocalManager.getUserId(),getArguments().getInt("courseId")).enqueue(new Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                if(response.isSuccessful() && response.body()!=null){
                    enrollmentId = response.body().getEnrollmentId();
                }
            }

            @Override
            public void onFailure(Call<Enrollment> call, Throwable t) {

            }
        });
    }

    private void getListLessonByCourseId(int courseId) {
        LessonService apiService = ApiClient.getClient(true).create(LessonService.class);
        apiService.getListLessonByCourseId(courseId).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                loadingLesson.setVisibility(View.VISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    List<Lesson> lessonList = response.body();
                    textCountLesson.setText(lessonList.size()+" Bài học");
                    lessonAdapter.setData(lessonList);
                    lessonAdapter.notifyDataSetChanged();
                    loadingLesson.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                Log.e("Error Lesson", t.getMessage());
            }
        });

    }
    public void setEnrollStatus(boolean enroll) {
        Log.d("isEnroll","true call");
        this.isEnroll = enroll;
    }


}