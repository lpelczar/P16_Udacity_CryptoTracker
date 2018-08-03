package com.udacity.lukasz.stocktracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Exchange {

    @SerializedName("Cryptsy")
    private Map<String, String[]> codes;

    public List<String> getStockCodes() {
        return new ArrayList<>(codes.keySet());
    }
}
