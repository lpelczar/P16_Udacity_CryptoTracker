package com.udacity.lukasz.stocktracker.service;

import com.udacity.lukasz.stocktracker.model.Stock;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface StockAPIService {

    @GET("/data/histohour?tsym=USD&limit=1")
    void getStockDataByStockCode(@Query("fsym") String stockCode, Callback<Stock> cb);
}
