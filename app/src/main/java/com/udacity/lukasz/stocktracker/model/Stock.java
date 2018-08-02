package com.udacity.lukasz.stocktracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Stock implements Parcelable {

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

    //Parcelable
    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    private Stock(Parcel in){
        this.name = in.readString();
        this.time = in.readString();
        this.close = in.readDouble();
        this.high = in.readDouble();
        this.low = in.readDouble();
        this.open = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.time);
        dest.writeDouble(this.close);
        dest.writeDouble(this.high);
        dest.writeDouble(this.low);
        dest.writeDouble(this.open);
    }
}
