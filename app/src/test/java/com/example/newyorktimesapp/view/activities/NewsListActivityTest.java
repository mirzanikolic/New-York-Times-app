package com.example.newyorktimesapp.view.activities;

import static org.junit.Assert.*;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newyorktimesapp.R;

import org.junit.Test;

public class NewsListActivityTest {

    NewsListActivity newsListActivity;

    @Test
    public void recyclerViewNotEmpty() {
        RecyclerView recyclerView = newsListActivity.findViewById(R.id.recycler_view);
        assertNotNull(recyclerView);
    }

}