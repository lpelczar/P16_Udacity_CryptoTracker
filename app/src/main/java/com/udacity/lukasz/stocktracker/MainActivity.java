package com.udacity.lukasz.stocktracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.udacity.lukasz.stocktracker.fragment.CursorStockFragment;
import com.udacity.lukasz.stocktracker.fragment.StockFragment;
import com.udacity.lukasz.stocktracker.model.Stock;
import com.udacity.lukasz.stocktracker.service.StockAPIService;
import com.udacity.lukasz.stocktracker.util.InternetCheck;
import com.udacity.lukasz.stocktracker.util.StockDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import static com.udacity.lukasz.stocktracker.data.StockContract.*;

public class MainActivity extends AppCompatActivity implements
        StockFragment.OnStockFragmentInteractionListener  {

    public static final String ARG_STOCK_FRAGMENT = "stock-fragment";
    private Fragment stockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            stockFragment = getSupportFragmentManager()
                    .getFragment(savedInstanceState, ARG_STOCK_FRAGMENT);
        }

        getStocksData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddStockActivity.class);
            startActivity(intent);
        });
    }

    private void getStocksData() {
        new InternetCheck(internet -> {
            if (!internet) {
                getStocksFromDatabase();
            } else {
                getStocksFromApi();
            }
        });
    }

    private void getStocksFromApi() {

        final List<String> stockCodes = getStockCodesFromSharedPreferences();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Stock.class, new StockDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://min-api.cryptocompare.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        StockAPIService service = restAdapter.create(StockAPIService.class);

        final List<Stock> stocks = new ArrayList<>();

        for (int i = 0; i < stockCodes.size(); i++) {

            service.getStockDataByStockCode(stockCodes.get(i), new Callback<Stock>() {

                @Override
                public void success(Stock stock, Response response) {
                    stocks.add(stock);
                    if (stocks.size() == stockCodes.size()) {
                        clearDatabase();
                        saveStocksToDatabase(stocks);
                        startFragment(stocks);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    getStocksFromDatabase();
                }
            });
        }

    }

    private void clearDatabase() {
        getContentResolver().delete(StockEntry.CONTENT_URI, null, null);
    }

    private void saveStocksToDatabase(List<Stock> stocks) {

        for (Stock stock : stocks) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(StockEntry.COLUMN_NAME, stock.getName());
            contentValues.put(StockEntry.COLUMN_PRICE, stock.getPrice());
            contentValues.put(StockEntry.COLUMN_HIGH, stock.getHigh());
            contentValues.put(StockEntry.COLUMN_LOW, stock.getLow());
            contentValues.put(StockEntry.COLUMN_OPEN, stock.getOpen());
            contentValues.put(StockEntry.COLUMN_CHANGE_24H, stock.getChange24h());
            contentValues.put(StockEntry.COLUMN_CHANGE_24H_PERCENT, stock.getChange24hPercent());
            contentValues.put(StockEntry.COLUMN_VOLUME_24H, stock.getVolume24h());
            contentValues.put(StockEntry.COLUMN_LAST_UPDATE, stock.getLastUpdate());

            getContentResolver().insert(StockEntry.CONTENT_URI, contentValues);
        }
    }

    private void getStocksFromDatabase() {
        if (stockFragment == null) {
            stockFragment = new CursorStockFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, stockFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, stockFragment)
                    .commit();
        }
    }

    private List<String> getStockCodesFromSharedPreferences() {
        List<String> stockCodes;
        SharedPreferences prefs = getSharedPreferences(AddStockActivity.PREFS_NAME, MODE_PRIVATE);
        String codes = prefs.getString(AddStockActivity.PREFS_CODES, null);
        if (codes != null) {
            Type type = new TypeToken<ArrayList<String>>() { }.getType();
            stockCodes = new Gson().fromJson(codes, type);
        } else {
            stockCodes = new ArrayList<>();
        }
        return stockCodes;
    }

    private void startFragment(List<Stock> stocks) {
        if (stockFragment == null) {
            stockFragment = StockFragment.newInstance(1, stocks);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, stockFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, stockFragment)
                    .commit();
        }
    }

    @Override
    public void onStockItemInteraction(Stock stock) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ARG_STOCK, stock);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (stockFragment != null && stockFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, ARG_STOCK_FRAGMENT, stockFragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
