package com.udacity.lukasz.stocktracker.adapter;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.lukasz.stocktracker.R;
import com.udacity.lukasz.stocktracker.fragment.StockFragment;
import com.udacity.lukasz.stocktracker.fragment.StockFragment.OnStockFragmentInteractionListener;
import com.udacity.lukasz.stocktracker.model.Stock;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private final List<Stock> stocks;
    private final OnStockFragmentInteractionListener listener;

    public StockAdapter(List<Stock> stocks, OnStockFragmentInteractionListener listener) {
        this.stocks = stocks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Stock stock = stocks.get(position);
        holder.name.setText(stock.getName());
        holder.price.setText(String.format("Price: %s", stock.getPrice()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onStockItemInteraction(stock);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stocks.size();
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
