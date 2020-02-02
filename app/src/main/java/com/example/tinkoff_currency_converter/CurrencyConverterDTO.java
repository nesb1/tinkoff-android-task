package com.example.tinkoff_currency_converter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class CurrencyConverterDTO {
    @PrimaryKey @NonNull
    public String transaction;

    public double value;

    public CurrencyConverterDTO(@NonNull String transaction, double value) {
        this.transaction = transaction;
        this.value = value;
    }
}
