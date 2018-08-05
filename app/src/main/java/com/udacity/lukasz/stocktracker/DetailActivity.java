package com.udacity.lukasz.stocktracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.lukasz.stocktracker.model.Stock;

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
        Toast.makeText(this, "Stock not available", Toast.LENGTH_SHORT).show();
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
                // Widget
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
