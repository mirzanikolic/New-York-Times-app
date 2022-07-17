package com.example.newyorktimesapp.view.activities;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.newyorktimesapp.contracts.NewsContract;
import com.example.newyorktimesapp.data.remote.ApiUtils;
import com.example.newyorktimesapp.models.Example;

import retrofit2.Call;
import retrofit2.Callback;

public class NewsModel implements NewsContract.NewsModel {

    public static final String apiKey = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";
    public static final String EXTRA_URL = "newsUrl";
    String oldString;
    NewsContract.NewsPresenter presenter;

    public NewsModel(NewsContract.NewsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getNewsFromApi(String oldString, int page) {
        ApiUtils.getApiRequest().getSearch(oldString, apiKey, page).enqueue(new Callback<Example>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Example> call, retrofit2.Response<Example> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    presenter.newsPrepared(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Example> call, @NonNull Throwable t) {
                presenter.newsPrepared(null);
            }
        });
    }
}