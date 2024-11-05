package com.example.courseproject.Service;

import com.example.courseproject.Model.Lesson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LessonService {
    @GET("Lesson/GetLessonByCourseId/{courseId}")
    Call<List<Lesson>> getListLessonByCourseId(@Path("courseId") int courseId);
}
