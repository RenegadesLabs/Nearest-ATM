package com.renegades.labs.nearestatm.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Виталик on 01.10.2017.
 */

public interface AtmApi {
    @GET("/p24api/infrastructure")
    Call<ATM> getAtms(@QueryMap Map<String, String> options);
}
