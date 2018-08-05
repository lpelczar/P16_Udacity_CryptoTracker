package com.udacity.lukasz.stocktracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        price.setText(String.valueOf(stock.getPrice()));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_STOCK, stock);
        super.onSaveInstanceState(outState);
    }
}
