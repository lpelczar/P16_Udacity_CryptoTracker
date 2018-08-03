package com.udacity.lukasz.stocktracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.lukasz.stocktracker.model.Exchange;
import com.udacity.lukasz.stocktracker.model.Stock;
import com.udacity.lukasz.stocktracker.service.StockAPIService;
import com.udacity.lukasz.stocktracker.util.ExchangeDeserializer;
import com.udacity.lukasz.stocktracker.util.StockDeserializer;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class AddStockActivity extends AppCompatActivity {

    private List<String> stockCodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        getStockCodesFromApi();
    }

    private void getStockCodesFromApi() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://min-api.cryptocompare.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        StockAPIService service = restAdapter.create(StockAPIService.class);

        service.getExchange(new Callback<Exchange>() {
            @Override
            public void success(Exchange exchange, Response response) {
                fillSpinnerWithData(exchange);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void fillSpinnerWithData(Exchange exchange) {
        stockCodes.addAll(exchange.getStockCodes());

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, stockCodes);
        spinner.setAdapter(adapter);
    }

}
