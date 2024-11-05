package com.example.courseproject.Service;

import com.example.courseproject.Model.Notification;

import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("Notification/user/{userId}")
    Call<List<Notification>> getNotifications(@Path("userId") int userId);
    @POST("Notification")
    Call<Unit> addNotification(@Body Notification notification);
    @DELETE("Notification/{id}")
    Call<Unit> deleteNotification(@Path("id") int id);
    @PUT("Notification/user/{userId}/markAllAsRead")
    Call<Unit> markAllAsRead(@Path("userId") int userId);
}
