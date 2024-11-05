package com.example.courseproject.Service;

import com.example.courseproject.Model.Quiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuizService {
    @GET("Quizz/GetByCourseId/{courseId}")
    Call<Quiz> getQuizByCourseId(@Path("courseId") int courseId);
}
