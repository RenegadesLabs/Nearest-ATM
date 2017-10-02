package com.renegades.labs.nearestatm;

import android.app.Application;

import com.renegades.labs.nearestatm.api.AtmApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Виталик on 01.10.2017.
 */

public class MyApp extends Application {
    private static AtmApi atmApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.privatbank.ua/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        atmApi = retrofit.create(AtmApi.class);
    }

    public static AtmApi getAtmApi() {
        return atmApi;
    }
}
