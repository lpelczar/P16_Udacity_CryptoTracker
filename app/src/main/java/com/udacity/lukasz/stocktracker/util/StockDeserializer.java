package com.udacity.lukasz.stocktracker.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.udacity.lukasz.stocktracker.model.Stock;

import java.lang.reflect.Type;

public class StockDeserializer implements JsonDeserializer<Stock> {

    @Override
    public Stock deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonElement stock = je.getAsJsonObject().get("RAW");
        return new Gson().fromJson(stock, Stock.class);
    }
}
