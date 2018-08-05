package com.udacity.lukasz.stocktracker.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class StockContract {

    public static final String AUTHORITY = "com.udacity.lukasz.stocktracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_STOCKS = "stocks";

    public static final class StockEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOCKS).build();

        public static final String TABLE_NAME = "stocks";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_HIGH = "high";
        public static final String COLUMN_LOW = "low";
        public static final String COLUMN_OPEN = "open";
        public static final String COLUMN_CHANGE_24H = "change_24h";
        public static final String COLUMN_CHANGE_24H_PERCENT = "change_24h_percent";
        public static final String COLUMN_VOLUME_24H = "volume_24h";
        public static final String COLUMN_LAST_UPDATE = "lastUpdate";
    }
}
