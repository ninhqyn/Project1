package com.example.courseproject.Service;
import com.example.courseproject.Model.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CourseService {
    @GET("Course/GetAllCourse/{descByEnrollment}")
    Call<List<Course>> getAllCourse(@Path("descByEnrollment") boolean descByEnrollment);
    @GET("Course/GetAllCourseByCategoryId/{id}")
    Call<List<Course>> getCourseByCategoryId(@Path("id") int categoryId);
    @GET("Course/GetAllCourseByUserId/{userId}/{status}")
    Call<List<Course>> getAllCourseByUserId(@Path("userId") int userId,@Path("status") String status);
    @GET("Course/GetCourseById/{courseId}")
    Call<Course> getCourseById(@Path("courseId") int courseId);
}
