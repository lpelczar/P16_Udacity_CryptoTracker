package com.udacity.lukasz.stocktracker;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.lukasz.stocktracker.model.Stock;
import com.udacity.lukasz.stocktracker.widget.StockWidgetProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String ARG_STOCK = "extra-stock";
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState != null) {
            stock = savedInstanceState.getParcelable(ARG_STOCK);
        } else {
            if (getIntent() == null) closeOnError();
            Bundle data = getIntent().getExtras();
            if (data != null) {
                stock = data.getParcelable(ARG_STOCK);
            } else {
                closeOnError();
                return;
            }
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(stock.getName());

        populateUI();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.stock_not_available, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        TextView price = findViewById(R.id.price_tv);
        TextView high = findViewById(R.id.high_tv);
        TextView low = findViewById(R.id.low_tv);
        TextView open = findViewById(R.id.open_tv);
        TextView change24h = findViewById(R.id.change24_tv);
        TextView change24hPercent = findViewById(R.id.change_percent_tv);
        TextView volume24h = findViewById(R.id.volume_24h_tv);

        price.setText(String.format(Locale.getDefault(), "%.2f", stock.getPrice()));
        high.setText(String.format(Locale.getDefault(), "%.2f", stock.getHigh()));
        low.setText(String.format(Locale.getDefault(), "%.2f", stock.getLow()));
        open.setText(String.format(Locale.getDefault(), "%.2f", stock.getOpen()));
        change24h.setText(String.format(Locale.getDefault(), "%.2f", stock.getChange24h()));

        if (stock.getChange24h() < 0) {
            change24h.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            change24h.setTextColor(getResources().getColor(R.color.green));
        }

        change24hPercent.setText(String.format(Locale.getDefault(), "%.2f", stock.getChange24hPercent()));

        if (stock.getChange24hPercent() < 0) {
            change24hPercent.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            change24hPercent.setTextColor(getResources().getColor(R.color.green));
        }

        volume24h.setText(String.format(Locale.getDefault(), "%.2f", stock.getVolume24h()));

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_STOCK, stock);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_to_widget:
                updateWidget();
                return true;
            case R.id.unfollow:
                unfollowStock();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void unfollowStock() {
        SharedPreferences prefs = getSharedPreferences(AddStockActivity.PREFS_NAME, MODE_PRIVATE);
        String codesJson = prefs.getString(AddStockActivity.PREFS_CODES, null);
        ArrayList<String> codes;

        if (codesJson != null) {
            Type type = new TypeToken<ArrayList<String>>() { }.getType();
            codes = new Gson().fromJson(codesJson, type);
            codes.remove(stock.getName());
            Toast.makeText(getApplicationContext(), R.string.currency_now_unfollowed, Toast.LENGTH_LONG).show();
        } else {
            codes = new ArrayList<>();
        }

        String data = new Gson().toJson(codes);
        SharedPreferences.Editor editor = getSharedPreferences(AddStockActivity.PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.putString(AddStockActivity.PREFS_CODES, data);
        editor.apply();
    }

    private void updateWidget() {
        SharedPreferences sharedPreferences = getSharedPreferences(StockWidgetProvider.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(StockWidgetProvider.KEY_STOCK, new Gson().toJson(stock)).apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName componentName = new ComponentName(this, StockWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        Intent intent = new Intent(this, StockWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        this.sendBroadcast(intent);
        Toast.makeText(this, getString(R.string.added) + " " + stock.getName() + " " +
                getString(R.string.to_widget), Toast.LENGTH_SHORT).show();
    }
}
