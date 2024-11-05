package com.example.courseproject.Service;

import com.example.courseproject.Model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuestionService {
    @GET("Question/GetAllByQuizId/{quizId}")
    Call<List<Question>> getAllQuestionByQuizId(@Path("quizId") int quizId);
}
