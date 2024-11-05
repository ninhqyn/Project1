package com.example.courseproject.Service;
import com.example.courseproject.Model.Banner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BannerService {
    @GET("Banners")
    Call<List<Banner>> getBanners();
}
