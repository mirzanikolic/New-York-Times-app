package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.remote.APIRequest;
import com.example.newyorktimesapp.remote.ApiClient;
import com.example.newyorktimesapp.remote.ApiUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final String apiKey = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";
    NewsAdapter adapter;
    ArrayList<Doc> news;
    String q = "Biden";


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

    public void getNews()


}