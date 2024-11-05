package com.example.courseproject.Service;

import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.FavoriteCourse;

import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CourseFavoriteService {
    @POST("FavoriteCourse")
    Call<Unit> addFavoriteCourse(@Body FavoriteCourse favoriteCourse);
    @DELETE("FavoriteCourse/user/{userId}/course/{courseId}")
    Call<Unit> deleteFavoriteCourse(@Path("userId") int userId,@Path("courseId") int courseId);
    @GET("FavoriteCourse/user/{userId}")
    Call<List<Course>> getFavoriteCourseByUserId(@Path("userId") int userId);
}
