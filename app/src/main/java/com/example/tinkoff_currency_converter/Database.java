package com.example.tinkoff_currency_converter;

import androidx.room.RoomDatabase;
@androidx.room.Database(entities = {CurrencyConverterDTO.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract CurrencyConverterDAO currencyConverterDAO();
}
