package com.udacity.lukasz.stocktracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class Stock implements Parcelable {

    @SerializedName("FROMSYMBOL")
    private String name;

    @SerializedName("PRICE")
    private double price;

    @SerializedName("HIGH24HOUR")
    private double high;

    @SerializedName("LOW24HOUR")
    private double low;

    @SerializedName("OPEN24HOUR")
    private double open;

    @SerializedName("CHANGE24HOUR")
    private double change24h;

    @SerializedName("CHANGEPCT25HOUR")
    private double change24hPercent;

    @SerializedName("VOLUME24HOUR")
    private double volume24h;

    @SerializedName("LASTUPDATE")
    private long lastUpdate;

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
        this.price = in.readDouble();
        this.high = in.readDouble();
        this.low = in.readDouble();
        this.open = in.readDouble();
        this.change24h = in.readDouble();
        this.change24hPercent = in.readDouble();
        this.volume24h = in.readDouble();
        this.lastUpdate = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeDouble(this.high);
        dest.writeDouble(this.low);
        dest.writeDouble(this.open);
        dest.writeDouble(this.change24h);
        dest.writeDouble(this.change24hPercent);
        dest.writeDouble(this.volume24h);
        dest.writeLong(this.lastUpdate);
    }
}
