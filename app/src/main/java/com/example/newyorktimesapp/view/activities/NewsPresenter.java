package com.example.newyorktimesapp.view.activities;

import com.example.newyorktimesapp.contracts.NewsContract;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Example;

import java.util.ArrayList;

public class NewsPresenter implements NewsContract.NewsPresenter {

    NewsContract.NewsModel model;
    NewsContract.NewsView view;

    public NewsPresenter(NewsContract.NewsView view) {
        this.view = view;
        model = new NewsModel(this);
    }

    @Override
    public void getNews(String oldString, int page) {
        view.showLoading();
        model.getNewsFromApi(oldString, page);
    }

    @Override
    public void newsPrepared(Example newsResponse) {
        view.hideLoading();
        if (newsResponse != null) {
            view.showData((ArrayList<Doc>) newsResponse.getResponse().getDocs());
        }
    }
}
