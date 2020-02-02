package com.example.tinkoff_currency_converter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyViewModel myViewModel;
    private TextView textView;
    private Button button;
    private LiveData<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        data = myViewModel.getDefaultLiveData();
        data.observe(this, res->textView.setText(res));
        button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editText);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.getAllCurrencies());
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);


        button.setOnClickListener(view -> {
            String from = Utils.getAllCurrencies().get(spinner1.getSelectedItemPosition());
            String to = Utils.getAllCurrencies().get(spinner2.getSelectedItemPosition());
            myViewModel.invokeCurrencyConverter(from, to, editText.getText().toString());
        });


    }
}
