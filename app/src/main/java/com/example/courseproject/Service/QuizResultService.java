package com.example.courseproject.Service;

import com.example.courseproject.Model.QuizResult;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface QuizResultService {
    @POST("QuizResult")
    Call<Unit> addQuizResult(@Body QuizResult quizResult);
    @GET("QuizResult/highest")
    Call<QuizResult> getHighestQuizResult(@Query("courseId") int courseId, @Query("enrollmentId") int enrollmentId);
    @GET("QuizResult/percentage")
    Call<Percentage> getPercentage(@Query("courseId") int courseId, @Query("enrollmentId") int enrollmentId);
}
