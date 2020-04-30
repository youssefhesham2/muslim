package com.example.muslim.Data.Local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.muslim.Models.EntintyModel;
import com.example.muslim.Models.QuranModel;

@Database(entities ={EntintyModel.class, QuranModel.class},version =4,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract dataa dao();
}
