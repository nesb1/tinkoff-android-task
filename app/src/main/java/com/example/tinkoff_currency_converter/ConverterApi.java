package com.example.tinkoff_currency_converter;


import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConverterApi {
    @GET("convert")
    Single<Response> convert(@Query("q") String currencies, @Query("compact") String s, @Query("apiKey") String key);
}
