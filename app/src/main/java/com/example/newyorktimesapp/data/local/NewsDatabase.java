package com.example.newyorktimesapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsEntity.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
    private static NewsDatabase INSTANCE;

    public static NewsDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, NewsDatabase.class, "appDatabase").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
