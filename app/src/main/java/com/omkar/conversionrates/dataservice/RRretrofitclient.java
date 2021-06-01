package com.omkar.conversionrates.dataservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RRretrofitclient {
    private static RRretrofitclient instance = null;
    private rateservice myApi;

    private RRretrofitclient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(rateservice.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(rateservice.class);
    }

    public static synchronized RRretrofitclient getInstance() {
        if (instance == null) {
            instance = new RRretrofitclient();
        }
        return instance;
    }

    public rateservice getMyApi() {
        return myApi;
    }
}
