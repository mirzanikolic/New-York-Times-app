package com.example.newyorktimesapp.view.activities;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newyorktimesapp.R;
import com.example.newyorktimesapp.adapter.NewsAdapter;
import com.example.newyorktimesapp.contracts.NewsListContract;
import com.example.newyorktimesapp.listener.ItemClickSupport;
import com.example.newyorktimesapp.models.Doc;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity implements NewsListContract.View {
    private static final String TAG = "NewsListActivity";
    public static final String EXTRA_URL = "newsUrl";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private NewsListPresenter newsListPresenter;
    private List<Doc> newsList;
    private int pageNo = 1;
    String query;

    //Constants for load more
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;

    // Constants for filter functionality
    private String fromReleaseFilter = "";
    private String toReleaseFilter = "";


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
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    private void setListeners(){
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
                    newsListPresenter.getMoreData(query, pageNo);
                    loading = true;
                }

            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                onNewsItemClick(position);
            }
        });
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
    protected void onDestroy(){
        super.onDestroy();
        newsListPresenter.onDestroy();
    }
}