package com.example.tinkoff_currency_converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MyViewModel extends ViewModel {
    private MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
    private String transaction;
    private double value;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private Database getDatabase() {
        return App.getInstance().getDatabase();
    }

    public void invokeCurrencyConverter(String from, String to, String value) {
        transaction = String.format("%s_%s", from, to);
        this.value = Double.parseDouble(value);
        Disposable disposable = Utils.hasInternetConnection().subscribe(this::processResultOfInternetConnection);
        compositeDisposable.add(disposable);
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }


    private void processResultOfInternetConnection(boolean hasConnection) {
        if (hasConnection) {
            Disposable disposable = RetrofitSingleton.getInstance().create(ConverterApi.class).convert(transaction, ConverterApi.s, ConverterApi.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::processResultOfRequest, (e -> {
                        errorLiveData.postValue("free api reached limit");
                        mutableLiveData.postValue("");
                    }));
            compositeDisposable.add(disposable);
        } else {
            getDataFromDB(transaction);
        }

    }

    private void processResultOfRequest(CurrencyConverterResponse currencyConverterResponse) {
        postValue(String.valueOf(currencyConverterResponse.getValue() * value));
        setDataToDB(new CurrencyConverterDTO(currencyConverterResponse.getTransaction(), currencyConverterResponse.getValue()));
    }

    private void setDataToDB(CurrencyConverterDTO currencyConverter) {
        Completable.fromAction(() -> getDatabase().currencyConverterDAO()
                .insert(currencyConverter))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void getDataFromDB(String transaction) {
        Disposable disposable = getDatabase().currencyConverterDAO()
                .get(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((data -> postValue(String.valueOf(data.value * value))),
                        e -> {
                            errorLiveData.postValue("No internet");
                            mutableLiveData.postValue("");
                        });
        compositeDisposable.add(disposable);
    }

    public LiveData<String> getDefaultMutableLiveData() {
        return mutableLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    private void postValue(String value) {
        errorLiveData.postValue("");
        mutableLiveData.postValue(value);
    }


}


