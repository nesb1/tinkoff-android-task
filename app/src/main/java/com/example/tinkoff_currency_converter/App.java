package com.example.tinkoff_currency_converter;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    private static App instance;

    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this,Database.class, "database")
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }
}
