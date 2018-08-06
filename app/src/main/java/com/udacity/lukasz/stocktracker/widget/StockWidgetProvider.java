package com.udacity.lukasz.stocktracker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.udacity.lukasz.stocktracker.MainActivity;
import com.udacity.lukasz.stocktracker.R;
import com.udacity.lukasz.stocktracker.model.Stock;

import java.util.Locale;

public class StockWidgetProvider extends AppWidgetProvider {

    public static String SHARED_PREFERENCES = "StockWidgetData";
    public static String KEY_STOCK = "Stock";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, Stock stock) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stock);
        views.setViewVisibility(R.id.widget_starter_text, View.GONE);
        views.setViewVisibility(R.id.widget_starter_image, View.GONE);

        views.setTextViewText(R.id.name_tv, stock.getName());
        views.setTextViewText(R.id.price_tv, String.format(Locale.getDefault(), "%.2f", stock.getPrice()));
        views.setTextViewText(R.id.high_tv, String.format(Locale.getDefault(), "%.2f", stock.getHigh()));
        views.setTextViewText(R.id.low_tv, String.format(Locale.getDefault(), "%.2f", stock.getLow()));
        views.setTextViewText(R.id.open_tv, String.format(Locale.getDefault(), "%.2f", stock.getOpen()));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String stockJson = sharedPreferences.getString(KEY_STOCK, null);
        Stock stock = new Gson().fromJson(stockJson, Stock.class);
        if (stock != null) {
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, stock);
            }
        }
    }
}
