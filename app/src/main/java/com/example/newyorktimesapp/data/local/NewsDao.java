package com.example.newyorktimesapp.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news")
    List<NewsEntity> getLocalNews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveLocalNews(List<NewsEntity> newsEntities);

    @Query("DELETE FROM news")
    void deleteAll();
}
