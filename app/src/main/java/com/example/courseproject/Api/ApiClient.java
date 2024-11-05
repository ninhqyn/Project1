package com.example.courseproject.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://luuninh.somee.com/api/";
    private static Retrofit retrofit;

    public static Retrofit getClient(boolean IsJson) {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL);

            if (IsJson) {
                builder.addConverterFactory(GsonConverterFactory.create());
            }

            retrofit = builder.build();
        }
        return retrofit;
    }
}
