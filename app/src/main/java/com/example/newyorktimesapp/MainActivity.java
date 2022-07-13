package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Example;
import com.example.newyorktimesapp.remote.ApiUtils;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    public final String apiKey = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";
    NewsAdapter adapter;
    ArrayList<Doc> news;
    String q = "Trump";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        news = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(news, this);
        mRecyclerView.setAdapter(adapter);

        getNews();


    }

    public void getNews(){
        ApiUtils.getApiRequest().getSearch(q,apiKey).enqueue(new Callback<Example>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    news.addAll(response.body().getResponse().getDocs());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }
        });
    }

}