package com.udacity.lukasz.stocktracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.lukasz.stocktracker.model.Exchange;
import com.udacity.lukasz.stocktracker.service.StockAPIService;
import com.udacity.lukasz.stocktracker.util.InternetCheck;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddStockActivity extends AppCompatActivity {

    public final static String PREFS_NAME = "stock-codes";
    public final static String PREFS_CODES = "codes";

    private final String ARG_STOCK_CODES = "arg-stock-codes";
    private final String ARG_SPINNER_POSITION = "arg-spinner-position";
    private List<String> stockCodes;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        spinner = findViewById(R.id.spinner);

        if (savedInstanceState != null) {
            stockCodes = savedInstanceState.getStringArrayList(ARG_STOCK_CODES);
            spinner.setSelection(savedInstanceState.getInt(ARG_SPINNER_POSITION));
            fillSpinnerWithData();
        } else {
            getStockCodesFromApi();
        }
    }

    private void fillSpinnerWithData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, stockCodes);
        spinner.setAdapter(adapter);
        setListenerOnButton(spinner);
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
                stockCodes = exchange.getStockCodes();
                fillSpinnerWithData();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void setListenerOnButton(final Spinner spinner) {

        Button button = findViewById(R.id.follow_button);
        button.setOnClickListener(v -> new InternetCheck(internet -> {
            if (internet) {
                handleFollowingStock(spinner);
            } else {
                displayErrorMessage();
            }
        }));
    }

    private void handleFollowingStock(Spinner spinner) {
        String code = spinner.getSelectedItem().toString();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String codesJson = prefs.getString(PREFS_CODES, null);
        ArrayList<String> codes;

        if (codesJson != null) {
            Type type = new TypeToken<ArrayList<String>>() { }.getType();
            codes = new Gson().fromJson(codesJson, type);
            if (!codes.contains(code)) {
                codes.add(code);
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.currency_already_added, Toast.LENGTH_LONG).show();
            }
        } else {
            codes = new ArrayList<>();
            codes.add(code);
        }

        String data = new Gson().toJson(codes);
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.putString(PREFS_CODES, data);
        editor.apply();

        Toast.makeText(getApplicationContext(),
                code + getString(R.string.is_now_followed), Toast.LENGTH_LONG).show();
    }

    private void displayErrorMessage() {
        Toast.makeText(getApplicationContext(), R.string.you_are_offline, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArrayList(ARG_STOCK_CODES, new ArrayList<>(stockCodes));
        outState.putInt(ARG_SPINNER_POSITION, spinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }
}
