package com.udacity.lukasz.stocktracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.lukasz.stocktracker.fragment.StockFragment;
import com.udacity.lukasz.stocktracker.model.Stock;
import com.udacity.lukasz.stocktracker.service.StockAPIService;
import com.udacity.lukasz.stocktracker.util.StockDeserializer;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MainActivity extends AppCompatActivity implements
        StockFragment.OnStockFragmentInteractionListener  {

    public static final String ARG_STOCK_FRAGMENT = "stock-fragment";
    private StockFragment stockFragment;

    private List<String> stockCodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockCodes.add("BTC");
        stockCodes.add("ETH");

        if (savedInstanceState != null) {
            stockFragment = (StockFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, ARG_STOCK_FRAGMENT);
        }

        getStocksFromApi();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStockActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getStocksFromApi() {
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

            final int finalI = i;
            service.getStockDataByStockCode(stockCodes.get(i), new Callback<Stock>() {
                @Override
                public void success(Stock stock, Response response) {
                    stocks.add(stock);
                    if (finalI == stockCodes.size() - 1) {
                        startFragment(stocks);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }
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
        Toast.makeText(this, "Stock: " + stock, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (stockFragment != null && stockFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, ARG_STOCK_FRAGMENT, stockFragment);
        }
        super.onSaveInstanceState(outState);
    }
}
