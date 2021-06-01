package com.omkar.conversionrates.dataservice;

import com.omkar.conversionrates.model.RequiredRate;

import retrofit2.Call;
import retrofit2.http.GET;

public interface rateservice {

    String BASE_URL ="https://lufickdev.bitbucket.io/";



    @GET("api/latest")
    Call<RequiredRate>getDatafromServer();



}
