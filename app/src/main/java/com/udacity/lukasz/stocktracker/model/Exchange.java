package com.udacity.lukasz.stocktracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Exchange {

    @SerializedName("Kraken")
    private Map<String, String[]> codes;

    public List<String> getStockCodes() {
        List<String> stockCodes = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : codes.entrySet()) {
            if (Arrays.asList(entry.getValue()).contains("USD")) {
                stockCodes.add(entry.getKey());
            }
        }

        return stockCodes;
    }
}
