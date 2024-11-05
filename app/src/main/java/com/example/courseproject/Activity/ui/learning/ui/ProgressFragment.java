package com.example.courseproject.Activity.ui.learning.ui;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.courseproject.Adapter.CourseProgressAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressFragment extends Fragment {
    private CourseProgressAdapter adapter ;
    private RecyclerView rcvCourse;
    private NestedScrollView layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        rcvCourse = v.findViewById(R.id.rcv_course_progress);
        adapter = new CourseProgressAdapter(getContext());
        layout = v.findViewById(R.id.layout_progress);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcvCourse.setLayoutManager(linearLayoutManager);
        
        callApiCourse();
        rcvCourse.setAdapter(adapter);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        callApiCourse();
    }

    private void callApiCourse() {
        int userId = DataLocalManager.getUserId();
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getAllCourseByUserId(userId,"enrolled").enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                List<Course> courseList = response.body();
                if(courseList == null){
                    layout.setBackgroundResource(R.drawable.course_not_found);
                }else{
                    layout.setBackgroundResource(R.color.white);
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