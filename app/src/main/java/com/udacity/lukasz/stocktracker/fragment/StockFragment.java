package com.udacity.lukasz.stocktracker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lukasz.stocktracker.R;
import com.udacity.lukasz.stocktracker.adapter.StockAdapter;
import com.udacity.lukasz.stocktracker.model.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_STOCKS = "stocks";
    private static final String ARG_RECYCLER_VIEW_STATE = "recycler-state";

    private int columnCount = 1;
    private List<Stock> stocks;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    private OnStockFragmentInteractionListener listener;

    public StockFragment() {
    }

    @SuppressWarnings("unused")
    public static StockFragment newInstance(int columnCount, List<Stock> stocks) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelableArrayList(ARG_STOCKS, new ArrayList<>(stocks));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            columnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);
            stocks = savedInstanceState.getParcelableArrayList(ARG_STOCKS);
            recyclerViewState = savedInstanceState.getParcelable(ARG_RECYCLER_VIEW_STATE);
        }

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            stocks = getArguments().getParcelableArrayList(ARG_STOCKS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            StockAdapter stockAdapter = new StockAdapter(stocks, listener);
            recyclerView.setAdapter(stockAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStockFragmentInteractionListener) {
            listener = (OnStockFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
