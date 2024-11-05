package com.example.courseproject.Service;

import com.example.courseproject.Model.Answer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AnswerService {
    @GET("Answer/GetByQuestionId/{questionId}")
    Call<List<Answer>> getAllAnswerByQuestionId(@Path("questionId") int questionID);
}
