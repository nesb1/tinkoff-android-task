package com.example.tinkoff_currency_converter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
            okhttpClientBuilder.readTimeout(4, TimeUnit.SECONDS);
            okhttpClientBuilder.addInterceptor(interceptor);
            instance = new Retrofit.Builder()
                    .baseUrl("https://free.currconv.com/api/v7/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonSingleton.getInstance()))
                    .client(okhttpClientBuilder.build())
                    .build();

        }
        return instance;
    }
}
