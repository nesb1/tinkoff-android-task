package com.example.tinkoff_currency_converter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Single;

@Dao
public interface CurrencyConverterDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CurrencyConverterDTO currencyConverter);

    @Query("SELECT * FROM CurrencyConverterDTO WHERE `transaction`= :transaction LIMIT 1")
    Single<CurrencyConverterDTO> get(String transaction);
}
