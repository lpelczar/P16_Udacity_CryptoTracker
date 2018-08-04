package com.udacity.lukasz.stocktracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.udacity.lukasz.stocktracker.model.Exchange;
import com.udacity.lukasz.stocktracker.model.Stock;
import com.udacity.lukasz.stocktracker.service.StockAPIService;
import com.udacity.lukasz.stocktracker.util.ExchangeDeserializer;
import com.udacity.lukasz.stocktracker.util.StockDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class AddStockActivity extends AppCompatActivity {

    public final static String PREFS_NAME = "stock-codes";
    public final static String PREFS_CODES = "codes";


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

        setListenerOnButton(spinner);
    }

    private void setListenerOnButton(final Spinner spinner) {

        Button button = findViewById(R.id.follow_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = spinner.getSelectedItem().toString();
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String codesJson = prefs.getString(PREFS_CODES, null);
                ArrayList<String> codes;

                if (codesJson != null) {
                    Type type = new TypeToken<ArrayList<String>>() { }.getType();
                    codes = new Gson().fromJson(codesJson, type);
                    if (!codes.contains(code)) {
                        codes.add(code);
                    }
                } else {
                    codes = new ArrayList<>();
                    codes.add(code);
                }

                String data = new Gson().toJson(codes);
                Log.e("JSON", data);
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.clear();
                editor.putString(PREFS_CODES, data);
                editor.apply();

            }
        });
    }
}
