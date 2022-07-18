package com.example.newyorktimesapp.view.activities;

import com.example.newyorktimesapp.contracts.NewsListContract;
import com.example.newyorktimesapp.models.Doc;

import java.util.List;

public class NewsListPresenter implements NewsListContract.Presenter, NewsListContract.Model.OnFinishedListener {

    private NewsListContract.Model newsListModel;
    private NewsListContract.View newsListView;

    public NewsListPresenter(NewsListContract.View newsListView) {
        this.newsListModel = new NewsListModel();
        this.newsListView = newsListView;
    }

    @Override
    public void onFinished(List<Doc> newsList) {
        newsListView.setDataToRecyclerView(newsList);
        if (newsListView != null) {
            newsListView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        newsListView.onResponseFail(throwable);
        if (newsListView != null) {
            newsListView.hideProgress();
        }
    }

    @Override
    public void onDestroy() {
        this.newsListView = null;
    }

    @Override
    public void getMoreData(String query, int page) {
        if (newsListView != null) {
            newsListView.showProgress();
        }
        newsListModel.getNewsList(this, query, page);
    }

    @Override
    public void requestDataFromServer() {
        if (newsListView != null) {
            newsListView.showProgress();
        }
        newsListModel.getNewsList(this, "balkan", 1);
    }
}

