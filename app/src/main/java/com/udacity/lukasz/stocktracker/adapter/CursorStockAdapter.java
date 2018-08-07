package com.udacity.lukasz.stocktracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.lukasz.stocktracker.R;
import com.udacity.lukasz.stocktracker.model.Stock;

import static com.udacity.lukasz.stocktracker.data.StockContract.*;
import static com.udacity.lukasz.stocktracker.fragment.StockFragment.*;

public class CursorStockAdapter extends RecyclerView.Adapter<CursorStockAdapter.ViewHolder> {

    private Cursor dataCursor;
    private final OnStockFragmentInteractionListener listener;

    public CursorStockAdapter(Cursor cursor, OnStockFragmentInteractionListener listener) {
        dataCursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CursorStockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);
        return new ViewHolder(view);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);

        int id = dataCursor.getInt(dataCursor.getColumnIndex(StockEntry.COLUMN_ID));
        String name = dataCursor.getString(dataCursor.getColumnIndex(StockEntry.COLUMN_NAME));
        double price = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_PRICE));
        double high = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_HIGH));
        double low = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_LOW));
        double open = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_OPEN));
        double change24h = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_CHANGE_24H));
        double change24hPercent = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_CHANGE_24H_PERCENT));
        double volume24h = dataCursor.getDouble(dataCursor.getColumnIndex(StockEntry.COLUMN_VOLUME_24H));
        long lastUpdate = dataCursor.getLong(dataCursor.getColumnIndex(StockEntry.COLUMN_LAST_UPDATE));

        Stock stock = new Stock(id, name, price, high, low, open, change24h,
                change24hPercent, volume24h, lastUpdate);

        holder.name.setText(name);
        holder.price.setText(String.format("Price: %s", price));

        holder.view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onStockItemInteraction(stock);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView name;
        final TextView price;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.stock_name_tv);
            price = view.findViewById(R.id.stock_price_tv);
        }
    }
}
