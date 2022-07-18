package com.example.newyorktimesapp.data.remote;

import com.example.newyorktimesapp.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("search/v2/articlesearch.json")
    Call<Example> getNews(
            @Query("q") String keyword,
            @Query("api-key") String apiKey,
            @Query("page") Integer page
    );
}
