package com.example.courseproject.Service;

import com.example.courseproject.Model.UpLoadRequestModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUploadService {
    @Multipart
    @POST("Cloud/upload/image")
    Call<UpLoadRequestModel> uploadImage(@Part MultipartBody.Part file);
}
