package com.example.courseproject.Service;

import com.example.courseproject.Model.Banner;
import com.example.courseproject.Model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("Category/{id}")
    Call<Category> getCategoryById();
    @GET("Category/GetAllCategory")
    Call<List<Category>> getAllCategory();

}
