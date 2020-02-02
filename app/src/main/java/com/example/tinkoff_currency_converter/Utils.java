package com.example.tinkoff_currency_converter;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Utils {
    public static Single<Boolean> hasInternetConnection() {
        return Single.fromCallable(() -> {
            try {
                // Connect to Google DNS to check for connection
                int timeoutMs = 1500;
                Socket socket = new Socket();
                Log.e("TAG", "hasInternetConnection: ");
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, timeoutMs);
                socket.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //custom deserializer
    public static class MyAdapter implements JsonDeserializer<CurrencyConverterResponse> {

        @Override
        public CurrencyConverterResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            CurrencyConverterResponse currencyConverterResponse = new CurrencyConverterResponse();

            try {
                if (json != null) {
                    JsonObject jsonObject = json.getAsJsonObject();
                    for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
                        currencyConverterResponse.setTransaction(elementEntry.getKey());
                        currencyConverterResponse.setValue(elementEntry.getValue().getAsDouble());
                        return currencyConverterResponse;
                    }
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }

        }
    }

    public static List<String> getAllCurrencies() {
        Currency[] res = Currency.values();
        List<String> list = new ArrayList<>(res.length);
        for (Currency re : res) {
            list.add(re.name());
        }
        return list;
    }

}
