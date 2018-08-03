package com.udacity.lukasz.stocktracker.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Exchange {

    private String name;
    private List<StockCode> codes;
}
