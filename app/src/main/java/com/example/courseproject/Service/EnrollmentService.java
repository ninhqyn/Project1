package com.example.courseproject.Service;

import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.SignUpRequest;
import com.example.courseproject.Model.User;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EnrollmentService {
    @POST("Enrollment/AddEnrollment")
    Call<Unit> enrollToCourse(@Body Enrollment enrollment);
    @GET("Enrollment/Get/{userId}/{courseId}")
    Call<Enrollment> getEnrollmentByUserIdAndCourseId(@Path("userId") int userId, @Path("courseId") int courseId);
}
