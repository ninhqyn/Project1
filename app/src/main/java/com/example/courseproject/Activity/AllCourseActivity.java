package com.example.courseproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Adapter.CourseAdapter;
import com.example.courseproject.Adapter.CourseSearchAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickItemCourseListener;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCourseActivity extends AppCompatActivity {
    ImageView btnBack;
    SearchView searchView;
    ProgressBar loadingAllCourse;
    RecyclerView rcvAllCourse;
    CourseSearchAdapter courseAdapter;
    List<Course> courseListFiler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createWidget();
        addAction();
    }

    private void addAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
    }


    private void createWidget() {
        btnBack = findViewById(R.id.btn_back);
        searchView = findViewById(R.id.searchView_course);
        loadingAllCourse = findViewById(R.id.loading_all_course);
        rcvAllCourse = findViewById(R.id.rcv_all_course);
        courseAdapter = new CourseSearchAdapter(AllCourseActivity.this, new IClickItemCourseListener() {
            @Override
            public void onClickItemCourse(Course course) {
                startCourseDetail(course);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AllCourseActivity.this, 2);
        rcvAllCourse.setLayoutManager(gridLayoutManager);
        rcvAllCourse.setAdapter(courseAdapter);

        getListCourse();


    }

    private void startCourseDetail(Course course) {
        Intent intent = new Intent(AllCourseActivity.this, CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_course",course);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void getListCourse() {
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getAllCourse(true).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                loadingAllCourse.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    courseListFiler = response.body();
                    courseAdapter.setData(courseListFiler);
                    courseAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AllCourseActivity.this, "No courses found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e("Error fail", t.getMessage());


            }
        });
    }
    private void filterList(String newText) {

        if (newText.isEmpty()) {
            //List<Course> empty = new ArrayList<>();
            courseAdapter.setData(courseListFiler);
            courseAdapter.notifyDataSetChanged();
            return;
        }
        List<Course> filteredList = new ArrayList<>();
        for (Course course : courseListFiler) {
            if (course.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(course);
            }
        }
        if (filteredList.isEmpty()) {
            List<Course> emptyList = new ArrayList<>();
            courseAdapter.setData(emptyList);
            courseAdapter.notifyDataSetChanged();
        } else {
            courseAdapter.setData(filteredList);
            courseAdapter.notifyDataSetChanged();
        }
    }

}