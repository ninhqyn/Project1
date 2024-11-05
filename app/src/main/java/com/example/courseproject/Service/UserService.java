package com.example.courseproject.Service;

import com.example.courseproject.Model.User;
import com.example.courseproject.Model.UserUpdateRequest;

import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("User/GetByEmail/{email}")
    Call<User> getUserByEmail(@Path("email") String email);
    @PUT("User/Update2/{id}")
    Call<Unit> updateUser(@Path("id") int id,@Body UserUpdateRequest user);
    @PUT("User/ChangePassword/{id}/{oldPassword}/{newPassword}")
    Call<Unit> changePassword(@Path("id") int id,@Path("oldPassword") String oldPassword,@Path("newPassword") String newPassword);

}
