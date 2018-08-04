package com.udacity.lukasz.stocktracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.lukasz.stocktracker.data.StockContract.StockEntry;

public class StockDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stocks.db";
    private static final int VERSION = 1;

    StockDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + StockEntry.TABLE_NAME + " (" +
                StockEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                StockEntry.COLUMN_NAME + " TEXT UNIQUE, " +
                StockEntry.COLUMN_PRICE + " INTEGER, " +
                StockEntry.COLUMN_LAST_UPDATE + " INTEGER);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StockEntry.TABLE_NAME);
        onCreate(db);
    }
}
