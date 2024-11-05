package com.example.courseproject.Service;

import com.example.courseproject.Model.CalculateProgress;
import com.example.courseproject.Model.Progress;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProgressService {
    @GET("Progress/GetById/{progressId}")
    Call<Process> getProgressById(@Path("progressId") int progressId);
    @POST("Progress")
    Call<Unit> createProgress(@Body Progress progress);
    @GET("Progress/CalculateProgress/{enrollmentId}")
    Call<CalculateProgress> calculateProgress(@Path("enrollmentId") int enrollmentId);
}
