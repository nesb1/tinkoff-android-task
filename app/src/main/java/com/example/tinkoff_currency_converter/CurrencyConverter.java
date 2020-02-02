package com.example.tinkoff_currency_converter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class CurrencyConverter {
    @PrimaryKey @NonNull
    public String transaction;

    public double value;

    public CurrencyConverter(@NonNull String transaction, double value) {
        this.transaction = transaction;
        this.value = value;
    }
}
