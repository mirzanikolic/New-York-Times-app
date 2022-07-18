package com.example.newyorktimesapp.view.activities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.newyorktimesapp.contracts.NewsListContract;
import com.example.newyorktimesapp.data.remote.APIService;
import com.example.newyorktimesapp.data.remote.ApiUtils;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Example;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListModel implements NewsListContract.Model {
    String query;
    private final String TAG = "NewsListModel";

    @Override
    public void getNewsList(OnFinishedListener onFinishedListener, String query, int page) {
        APIService apiService =
                ApiUtils.getClient().create(APIService.class);

        Call<Example> call = apiService.getNews(query, ApiUtils.API_KEY, page);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(@NonNull Call<Example> call, @NonNull Response<Example> response) {
                assert response.body() != null;
                List<Doc> news = response.body().getResponse().getDocs();
                onFinishedListener.onFinished(news);
            }

            @Override
            public void onFailure(@NonNull Call<Example> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
