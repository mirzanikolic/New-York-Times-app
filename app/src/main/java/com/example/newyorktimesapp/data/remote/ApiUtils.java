package com.example.newyorktimesapp.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    private static Retrofit retrofit = null;

    public static APIRequest getApiRequest() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIRequest.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(APIRequest.class);
    }
}