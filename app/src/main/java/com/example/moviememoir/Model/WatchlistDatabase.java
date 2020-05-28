package com.example.moviememoir.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Watchlist.class}, version = 2, exportSchema = false)
    public abstract class WatchlistDatabase extends RoomDatabase {
        private static final int NUMBER_OF_THREADS = 4;
        public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        public abstract WatchlistDAO watchlistDAO();
        private static WatchlistDatabase INSTANCE;
        public static synchronized WatchlistDatabase getInstance(final Context context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WatchlistDatabase.class, "WatchlistDatabase") .fallbackToDestructiveMigration() .build();
            } return INSTANCE; }
    }
