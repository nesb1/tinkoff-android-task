package com.example.tinkoff_currency_converter;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.SocketException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
    private Currency from;
    private Currency to;
    private String transaction = "RUB_USD";
    private double value;

    private Database getDatabase() {
        return App.getInstance().getDatabase();
    }

    public void invokeCurrencyConverter(String from, String to, String value) {
        // с помощью регулярок проверить является ли строка числом, если нет, то скинуть в лайв дату текст
        transaction = String.format("%s_%s",from,to);
        this.value = Double.parseDouble(value);
        Utils.hasInternetConnection().subscribe(this::processResultOfInternetConnection);
    }



    private void processResultOfInternetConnection(boolean hasConnection) {
        if (hasConnection) {
            RetrofitSingleton.getInstance().create(ConverterApi.class).convert(transaction, "ultra", "334b27634bb44849d412")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::processResultOfRequest);
        } else {
            getDataFromDB(transaction);
        }
    }

    private void processResultOfRequest(Response response) {
        mutableLiveData.postValue(String.valueOf(response.getValue() * value));
        setDataToDB(new CurrencyConverter(response.getTransaction(), response.getValue()));
    }

    private void setDataToDB(CurrencyConverter currencyConverter) {
        Completable.fromAction(() -> getDatabase().currencyConverterDAO()
                .insert(currencyConverter))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void getDataFromDB(String transaction) {
        getDatabase().currencyConverterDAO()
                .get(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> mutableLiveData.postValue(String.valueOf(data.value * value)), r -> mutableLiveData.postValue("No internet"));
    }

    public LiveData<String> getDefaultLiveData(){
        return mutableLiveData;
    }


}


