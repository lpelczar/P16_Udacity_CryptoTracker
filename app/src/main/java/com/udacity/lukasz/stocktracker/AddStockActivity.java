package com.udacity.lukasz.stocktracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddStockActivity extends AppCompatActivity {

    private List<String> stockCodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        stockCodes.add("BTC");
        stockCodes.add("ETH");
        stockCodes.add("CANN");

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, stockCodes);
        spinner.setAdapter(adapter);

    }

}
