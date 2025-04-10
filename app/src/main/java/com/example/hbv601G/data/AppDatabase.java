package com.example.hbv601G.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hbv601G.entities.Category;

import java.util.Locale;


@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context){
    if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "db").build();
        }
        return INSTANCE;
    }
}
