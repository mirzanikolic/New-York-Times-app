package com.example.newyorktimesapp.view.activities;

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

import com.example.newyorktimesapp.contracts.NewsContract;
import com.example.newyorktimesapp.view.adapter.NewsAdapter;
import com.example.newyorktimesapp.R;

import com.example.newyorktimesapp.listener.EndlessRecyclerViewScrollListener;
import com.example.newyorktimesapp.listener.ItemClickSupport;
import com.example.newyorktimesapp.models.Doc;

import com.example.newyorktimesapp.utility.Util;
import com.google.gson.Gson;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NewsContract.NewsView {
    public final String apiKey = "tZ9QFoQl6FCLFr6IgrIKX8aSI2IYxt1Y";
    public static final String EXTRA_URL = "newsUrl";
    NewsAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    EndlessRecyclerViewScrollListener scrollListener;
    private Gson gson;
    NewsContract.NewsPresenter presenter;
    RecyclerView.LayoutManager layoutManager;
    String oldString;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.loading_bar);
        presenter = new NewsPresenter(this);
        presenter.getNews(oldString, 0);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NewsAdapter(null);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        if (Util.isOnline(this)) {
            presenter.getNews(oldString, 0);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "There is no internet connection!", Toast.LENGTH_LONG).show();
        }


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                /*Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(EXTRA_URL)
                startActivity(intent);*/
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipe_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getNews(oldString, 0);
                pullToRefresh.setRefreshing(false);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getNews(oldString, page);
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
                clearData();
                presenter.getNews(oldString, 0);
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

    /*
    public void getNews(int page) {
        ApiUtils.getApiRequest().getSearch(oldString, apiKey, page).enqueue(new Callback<Example>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Example> call, retrofit2.Response<Example> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    progressBar.setVisibility(View.INVISIBLE);
                    news.addAll(response.body().getResponse().getDocs());
                    //NewsDatabase.getInstance(getApplicationContext()).newsDao().saveLocalNews(response.body().getResponse().getDocs());
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
    }*/

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showData(ArrayList<Doc> news) {
        adapter.changeData(news);
    }

    public void clearData() {
        adapter.clearNews();
    }
}