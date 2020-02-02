package com.example.tinkoff_currency_converter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CurrencyConverterDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CurrencyConverter currencyConverter);

    @Query("SELECT * FROM CurrencyConverter WHERE `transaction`= :transaction LIMIT 1")
    Single<CurrencyConverter> get(String transaction);
}
