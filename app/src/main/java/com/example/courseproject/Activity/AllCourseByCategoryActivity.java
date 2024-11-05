package com.example.courseproject.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Adapter.CourseAdapter;
import com.example.courseproject.Adapter.CourseByCategoryAdapter;
import com.example.courseproject.Adapter.CourseSearchAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickItemCourseListener;
import com.example.courseproject.Model.Category;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCourseByCategoryActivity extends AppCompatActivity {
    private TextView tvNameCategory2;
    private ImageView btnBack,btnInfo;
    private Category category;
    private ProgressBar loadingAllCourseByCategory;
    private RecyclerView rcvAllCourseByCategory;
    private CourseSearchAdapter courseByCategoryAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_course_by_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //
        tvNameCategory2 = findViewById(R.id.tv_name_category_2);

        btnBack = findViewById(R.id.btn_back);
        btnInfo = findViewById(R.id.img_info);
        loadingAllCourseByCategory = findViewById(R.id.loading_all_course_by_category);

        rcvAllCourseByCategory = findViewById(R.id.rcv_all_course_by_category);
        courseByCategoryAdapter = new CourseSearchAdapter(this, new IClickItemCourseListener() {
            @Override
            public void onClickItemCourse(Course course) {
                Intent intent = new Intent(AllCourseByCategoryActivity.this, CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_course",course);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //
       GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rcvAllCourseByCategory.setLayoutManager(gridLayoutManager);

        rcvAllCourseByCategory.setAdapter(courseByCategoryAdapter);
        //
        category = (Category) getIntent().getSerializableExtra("object_category");
        if(category!=null){
            tvNameCategory2.setText(category.getName());
            callApiGetCourseByCategory();
        }


        //action
        btnBack.setOnClickListener(v -> finish());
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCategoryInfor();
            }
        });


    }

    private void dialogCategoryInfor() {
        final Dialog dialog = new Dialog(AllCourseByCategoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_category_infor);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        //
        TextView tvNameCategory = dialog.findViewById(R.id.tv_dialog_title);
        TextView tvDescription = dialog.findViewById(R.id.tv_dialog_description);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        tvNameCategory.setText(category.getName());
        tvDescription.setText(category.getDescription());
        //
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callApiGetCourseByCategory() {
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getCourseByCategoryId(category.getCategoryId()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if(response.isSuccessful() && response.body()!=null){

                    loadingAllCourseByCategory.setVisibility(View.GONE);

                    List<Course> courseList = response.body();


                    courseByCategoryAdapter.setData(courseList);
                    courseByCategoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }
}