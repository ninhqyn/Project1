package com.example.courseproject.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Adapter.CategoryAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Category;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.Service.CategoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoryActivity extends AppCompatActivity {
    RecyclerView rcvAllCategory;
    CategoryAdapter categoryAdapter;
    ProgressBar loadingCategory;
    ImageView btnBack;
    SearchView svCategory;
    private List<Category> categoryListFiler = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadingCategory = findViewById(R.id.loading_all_category);
        rcvAllCategory = findViewById(R.id.rcv_all_category);
        btnBack = findViewById(R.id.btn_back);
        svCategory = findViewById(R.id.searchView_category);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rcvAllCategory.setLayoutManager(gridLayoutManager);
        categoryAdapter = new CategoryAdapter(this);
        rcvAllCategory.setAdapter(categoryAdapter);
        getListCategory();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        svCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void filterList(String newText) {
        List<Category> filteredList = new ArrayList<>();
        for (Category category : categoryListFiler) {
            if (category.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(category); // Chỉ thêm khóa học nếu tiêu đề chứa chuỗi tìm kiếm
            }
        }
        if (filteredList.isEmpty()) {
            categoryAdapter.setData(filteredList);
            categoryAdapter.notifyDataSetChanged();
        } else {
            categoryAdapter.setData(filteredList);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void getListCategory() {
        CategoryService apiService = ApiClient.getClient(true).create(CategoryService.class);
        apiService.getAllCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body();
                    categoryListFiler = categoryList;
                    categoryAdapter.setData(categoryList);
                    categoryAdapter.notifyDataSetChanged();
                    loadingCategory.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                  Log.e("Error fail", t.getMessage());
            }
        });
    }
}