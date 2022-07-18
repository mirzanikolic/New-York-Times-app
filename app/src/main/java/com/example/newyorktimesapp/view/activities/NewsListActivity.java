package com.example.newyorktimesapp.view.activities;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newyorktimesapp.R;
import com.example.newyorktimesapp.adapter.NewsAdapter;
import com.example.newyorktimesapp.contracts.NewsListContract;
import com.example.newyorktimesapp.listener.EndlessRecyclerViewScrollListener;
import com.example.newyorktimesapp.listener.ItemClickSupport;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity implements NewsListContract.View {
    private static final String TAG = "NewsListActivity";
    public static final String EXTRA_URL = "newsUrl";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private NewsListPresenter newsListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Doc> newsList;
    private int pageNo = 0;
    String query = "";

    //Constants for load more
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitUI();
        setListeners();

        //Initializing presenter
        newsListPresenter = new NewsListPresenter(this);
        newsListPresenter.requestDataFromServer();

    }

    private void InitUI() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.loading_bar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    private void setListeners() {
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                newsListPresenter.getMoreData(query, page);
            }
        });
        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    pageNo++;
                    newsListPresenter.getMoreData(query, pageNo);
                    loading = true;
                }
            }
        });*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsAdapter.clear();
                newsListPresenter.getMoreData(query, 0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                onNewsItemClick(position);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newQuery) {
                query = newQuery;
                newsAdapter.clear();
                newsListPresenter.getMoreData(query, pageNo);
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
                Util.hideKeyboard(NewsListActivity.this);
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

    @Override
    public void showProgress() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setDataToRecyclerView(List<Doc> newsArrayList) {
        newsList.addAll(newsArrayList);
        newsAdapter.notifyDataSetChanged();
        pageNo++;
    }

    @Override
    public void onResponseFail(Throwable throwable) {
        Log.e(TAG, throwable.getMessage());
        Toast.makeText(this, "Error with response", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNewsItemClick(int position) {
        if (position == -1) {
            return;
        }
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_URL, newsList.get(position).getWebUrl());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newsListPresenter.onDestroy();
    }
}