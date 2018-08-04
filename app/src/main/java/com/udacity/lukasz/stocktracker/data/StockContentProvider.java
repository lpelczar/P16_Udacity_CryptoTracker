package com.udacity.lukasz.stocktracker.data;

import android.content.ContentProvider;
import android.content.UriMatcher;

public class StockContentProvider extends ContentProvider {

    public static final int STOCKS = 100;
    public static final int STOCK_WITH_NAME = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private StockDbHelper stockDbHelper;

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(StockContract.AUTHORITY,
                StockContract.PATH_STOCKS, STOCKS);
        uriMatcher.addURI(StockContract.AUTHORITY,
                StockContract.PATH_STOCKS + "/#", STOCK_WITH_NAME);
        return uriMatcher;
    }

    
}
