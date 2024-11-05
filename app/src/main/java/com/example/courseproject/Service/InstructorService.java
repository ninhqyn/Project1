package com.example.courseproject.Service;

import com.example.courseproject.Model.Category;
import com.example.courseproject.Model.Instructor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InstructorService {

    @GET("Instructor/GetById/{id}")
    Call<Instructor> getInstructorById(@Path("id") int instructorId);
}
