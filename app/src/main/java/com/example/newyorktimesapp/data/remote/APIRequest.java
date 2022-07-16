package com.example.newyorktimesapp.data.remote;

import com.example.newyorktimesapp.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRequest {
    String BASE_URL = "https://api.nytimes.com/svc/";
    String API_IMAGE_BASE_URL = "https://www.nytimes.com/";

    @GET("search/v2/articlesearch.json")
    Call<Example> getSearch(
            @Query("q") String keyword,
            @Query("api-key") String apiKey
    );
}
