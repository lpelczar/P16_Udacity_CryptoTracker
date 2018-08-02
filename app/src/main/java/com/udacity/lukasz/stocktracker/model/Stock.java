package com.udacity.lukasz.stocktracker.model;

public class Stock {

    private String name;
    private String time;
    private double close;
    private double high;
    private double low;
    private double open;

    public Stock(String name, String time, double close, double high, double low, double open) {
        this.name = name;
        this.time = time;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }
}
