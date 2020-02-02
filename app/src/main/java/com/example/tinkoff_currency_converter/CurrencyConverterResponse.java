package com.example.tinkoff_currency_converter;

public class CurrencyConverterResponse {
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