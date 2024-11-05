package com.example.courseproject.Activity.ui.learning.ui;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.courseproject.Adapter.CourseCompletedAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedFragment extends Fragment {

    private CourseCompletedAdapter adapter;
    private RecyclerView rcvCourseCompleted;
    private NestedScrollView layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_completed, container, false);
        // Inflate the layout for this fragment
        rcvCourseCompleted = v.findViewById(R.id.rcv_course_completed);
        layout = v.findViewById(R.id.layout_completed);
        adapter = new CourseCompletedAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rcvCourseCompleted.setLayoutManager(linearLayoutManager);
        rcvCourseCompleted.setAdapter(adapter);
        callApiCourse();

        return v;
    }
    private void callApiCourse() {
        int userId = DataLocalManager.getUserId();
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getAllCourseByUserId(userId,"completed").enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                List<Course> courseList = response.body();
                if(courseList == null){
                    layout.setBackgroundResource(R.drawable.course_not_found);
                }
                if(response.isSuccessful() && response.body()!=null){


                    adapter.setData(courseList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getContext(),"Fail to call api",Toast.LENGTH_SHORT).show();
            }
        });
    }
}