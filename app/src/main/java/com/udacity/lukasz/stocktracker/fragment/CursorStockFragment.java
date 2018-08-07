package com.udacity.lukasz.stocktracker.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lukasz.stocktracker.R;
import com.udacity.lukasz.stocktracker.adapter.CursorStockAdapter;
import com.udacity.lukasz.stocktracker.data.StockContract;
import com.udacity.lukasz.stocktracker.fragment.StockFragment.OnStockFragmentInteractionListener;

import java.util.Objects;

public class CursorStockFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    CursorStockAdapter adapter;

    private OnStockFragmentInteractionListener listener;

    public CursorStockFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stock_list, container, false);
        recyclerView = (RecyclerView) rootView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CursorStockAdapter(null, listener);
        recyclerView.setAdapter(adapter);
        return rootView;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int LOADER_ID = 1;
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(Objects.requireNonNull(getContext()), StockContract.StockEntry.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}