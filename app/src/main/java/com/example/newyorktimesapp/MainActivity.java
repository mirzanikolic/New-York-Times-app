package com.example.newyorktimesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.example.newyorktimesapp.data.local.NewsDatabase;
import com.example.newyorktimesapp.data.local.NewsEntity;
import com.example.newyorktimesapp.listener.EndlessRecyclerViewScrollListener;
import com.example.newyorktimesapp.listener.ItemClickSupport;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Example;
import com.example.newyorktimesapp.data.remote.ApiUtils;
import com.example.newyorktimesapp.utility.Util;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    public final String apiKey = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";
    public static final String EXTRA_URL = "newsUrl";
    NewsAdapter adapter;
    ArrayList<Doc> news;
    ArrayList<NewsEntity> oldNews;
    String oldString;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    EndlessRecyclerViewScrollListener scrollListener;
    private Gson gson;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.loading_bar);
        recyclerView = findViewById(R.id.recycler_view);
        news = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(news, this);
        recyclerView.setAdapter(adapter);

        if (Util.isOnline(this)) {
            getNews(0);
            getNews(1);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "There is no internet connection!", Toast.LENGTH_LONG).show();
        }


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(EXTRA_URL, news.get(position).getWebUrl());
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipe_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reSearch();
                pullToRefresh.setRefreshing(false);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                oldString = query;
                reSearch();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.clearFocus();
                Util.hideKeyboard(MainActivity.this);
                return true;
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getNews(int page) {
        ApiUtils.getApiRequest().getSearch(oldString, apiKey, page).enqueue(new Callback<Example>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Example> call, retrofit2.Response<Example> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    progressBar.setVisibility(View.INVISIBLE);
                    news.addAll(response.body().getResponse().getDocs());
                    NewsDatabase.getInstance(getApplicationContext()).newsDao().saveLocalNews(response.body().getResponse().getDocs());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Example> call, @NonNull Throwable t) {
            }
        });
    }

    public void reSearch() {
        adapter.clearNews();
        scrollListener.resetState();
        progressBar.setVisibility(View.VISIBLE);
        getNews(0);
        getNews(1);
    }
}