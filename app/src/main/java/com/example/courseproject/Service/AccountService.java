package com.example.courseproject.Service;

import com.example.courseproject.Model.LoginRequest;
import com.example.courseproject.Model.SignUpRequest;
import com.example.courseproject.Model.TokenModel;

import kotlin.Unit;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountService {

    @POST("Account/Login")
    Call<TokenModel> signIn(@Body LoginRequest loginRequest);
    @POST("Account/Register")
    Call<Unit> signUp(@Body SignUpRequest signUpRequest);
    @POST("Account/VerifyCode")
    Call<Unit> verifyEmail(@Query("email") String email,@Query("code") String  code);
    @POST("Account/ResendCode")
    Call<Unit> resendEmail(@Query("email") String email);

    @POST("Account/ForgotPassword")
    Call<Unit> forgotPassword(@Query("email") String email);
    @POST("Account/ResetPassword")
    Call<Unit> resetPassword(@Query("email") String email,@Query("newPassword") String newPassword,@Query("code") String code);
    @POST("Account/VerifyForgotPasswordCode")
    Call<Unit> verifyForgotPassword(@Query("email") String email,@Query("code") String code);
}
