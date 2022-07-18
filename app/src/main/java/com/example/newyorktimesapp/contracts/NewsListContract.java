package com.example.newyorktimesapp.contracts;

import com.example.newyorktimesapp.models.Doc;

import java.util.List;

public interface NewsListContract {

    interface Model {
        interface OnFinishedListener {
            void onFinished(List<Doc> newsList);

            void onFailure(Throwable throwable);
        }

        void getNewsList(OnFinishedListener onFinishedListener, String query, int page);
    }

    interface View {
        void onNewsItemClick(int position);

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(List<Doc> newsList);

        void onResponseFail(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();

        void getMoreData(String query, int page);

        void requestDataFromServer();
    }
}
