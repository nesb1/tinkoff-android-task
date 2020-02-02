package com.example.tinkoff_currency_converter;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ConverterApi {
    String API_KEY = "334b27634bb44849d412";
    String s = "ultra";
    String baseUrl = "https://free.currconv.com/api/v7/";


    @GET("convert")
    Single<CurrencyConverterResponse> convert(@Query("q") String currencies, @Query("compact") String s, @Query("apiKey") String key);
}
