package com.udacity.lukasz.stocktracker.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.udacity.lukasz.stocktracker.model.Exchange;

import java.lang.reflect.Type;

public class ExchangeDeserializer implements JsonDeserializer<Exchange> {
    @Override
    public Exchange deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonElement stock = je.getAsJsonObject().get("Cryptsy");
        return new Gson().fromJson(stock, Exchange.class);
    }
}
