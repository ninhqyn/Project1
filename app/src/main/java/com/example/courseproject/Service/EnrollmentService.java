package com.example.courseproject.Service;

import com.example.courseproject.Model.Enrollment;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EnrollmentService {
    @POST("Enrollment/AddEnrollment")
    Call<Unit> enrollToCourse(@Body Enrollment enrollment);
    @GET("Enrollment/Get/{userId}/{courseId}")
    Call<Enrollment> getEnrollmentByUserIdAndCourseId(@Path("userId") int userId, @Path("courseId") int courseId);
    @PUT("Enrollment/Update/{id}")
    Call<Unit> updateEnrollment(@Path("id") int id, @Body Enrollment enrollment);
}
