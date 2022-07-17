package com.example.newyorktimesapp.contracts;

import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Example;

import java.util.ArrayList;

public class NewsContract {
    //responsible of getting data from api
    //just connect with presenter
    public interface NewsModel{
        void getNewsFromApi(String oldString, int page);
    }
    //responsible of show data in view
    //just connect with presenter
    public interface NewsView{
        void showLoading();
        void hideLoading();
        void showData(ArrayList<Doc> news);
    }
    //the road between model and view
    public interface NewsPresenter{
        void getNews(String oldString, int page);
        void newsPrepared(Example newsResponse);
    }
}
