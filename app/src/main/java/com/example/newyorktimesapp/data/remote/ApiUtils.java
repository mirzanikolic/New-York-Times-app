package com.example.newyorktimesapp.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    public static final String API_IMAGE_BASE_URL = "https://www.nytimes.com/";
    public static final String BASE_URL = "https://api.nytimes.com/svc/";
    public static final String API_KEY = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}