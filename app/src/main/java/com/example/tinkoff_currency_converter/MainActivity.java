package com.example.tinkoff_currency_converter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MyViewModel myViewModel;

    private LiveData<String> dataFromCurrencyConverter;
    private LiveData<String> errorLiveData;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView errorTextView = findViewById(R.id.textViewErrorMessage);
        TextView resultTextView = findViewById(R.id.textViewResult);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        EditText editText = findViewById(R.id.editTextValueInput);

        dataFromCurrencyConverter = myViewModel.getDefaultMutableLiveData();
        errorLiveData = myViewModel.getErrorLiveData();
        errorLiveData.observe(this, errorTextView::setText);
        dataFromCurrencyConverter.observe(this, resultTextView::setText);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.getAllCurrencies());
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinnerTo);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        compositeDisposable = new CompositeDisposable();
        Disposable disposable = RxTextView.textChanges(editText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((res) -> {
                    if (res.length() != 0) {
                        String from = Utils.getAllCurrencies().get(spinner1.getSelectedItemPosition());
                        String to = Utils.getAllCurrencies().get(spinner2.getSelectedItemPosition());
                        myViewModel.invokeCurrencyConverter(from, to, res.toString());
                    }
                });
        compositeDisposable.add(disposable);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
