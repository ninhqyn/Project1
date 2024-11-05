package com.example.courseproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
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

public class SearchCourseActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ImageView btnBack;

    List<Course> courseListFiler;
    CourseSearchAdapter courseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.rcv_all_course);
        btnBack = findViewById(R.id.btn_back);
        searchView.requestFocus();
        //
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        courseListFiler = new ArrayList<>();
        courseAdapter = new CourseSearchAdapter(SearchCourseActivity.this, new IClickItemCourseListener() {
            @Override
            public void onClickItemCourse(Course course) {
                Intent intent = new Intent(SearchCourseActivity.this, CourseDetailActivity.class);
                Bundle bundle  = new Bundle();
                bundle.putSerializable("object_course",course);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(courseAdapter);
        callApiCourse();
        //
        btnBack.setOnClickListener(v -> finish());
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        
    }

    private void callApiCourse() {
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getAllCourse(true).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courseList = response.body();
                    courseListFiler = courseList;
                } else {
                    Toast.makeText(SearchCourseActivity.this, "No courses found", Toast.LENGTH_SHORT).show();
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
            List<Course> empty = new ArrayList<>();
            courseAdapter.setData(empty);
            courseAdapter.notifyDataSetChanged();
            return;
        }
        List<Course> filteredList = new ArrayList<>();
        for (Course course : courseListFiler) {
            if (course.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(course); // Chỉ thêm khóa học nếu tiêu đề chứa chuỗi tìm kiếm
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