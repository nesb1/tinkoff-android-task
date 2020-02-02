package com.example.tinkoff_currency_converter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response{
    private String Transaction;
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }
}