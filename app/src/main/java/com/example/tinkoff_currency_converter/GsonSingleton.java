package com.example.tinkoff_currency_converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingleton {
    private static Gson instance;

    public static Gson getInstance() {
        if (instance == null) {
            instance = new GsonBuilder()
                    .registerTypeAdapter(Response.class, new Utils.MyAdapter())
                    .create();
        }
        return instance;
    }
}
