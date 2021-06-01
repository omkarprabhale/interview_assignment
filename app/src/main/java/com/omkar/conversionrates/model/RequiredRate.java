package com.omkar.conversionrates.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

public class RequiredRate implements Serializable {

    @SerializedName("success")
    boolean success;

    @SerializedName("base")
    String base;

    @SerializedName("date")
    String date;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }

    @SerializedName("rates")
    HashMap<String,Double> rates;









}
