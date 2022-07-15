package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.newyorktimesapp.listener.ItemClickSupport;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Example;
import com.example.newyorktimesapp.remote.ApiUtils;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    public final String apiKey = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";
    public static final String EXTRA_URL = "newsUrl";
    NewsAdapter adapter;
    ArrayList<Doc> news;
    String oldString = "";
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler_view);
        news = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(news, this);
        recyclerView.setAdapter(adapter);

        getNews();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(EXTRA_URL, news.get(position).getWebUrl());
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
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
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                hideKeyboard();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getNews() {
        ApiUtils.getApiRequest().getSearch(oldString, apiKey).enqueue(new Callback<Example>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                if (response.isSuccessful()) {
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

    public void reSearch() {
        adapter.clearNews();
        getNews();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}