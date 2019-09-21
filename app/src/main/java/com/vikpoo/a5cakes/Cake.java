package com.vikpoo.a5cakes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Cake implements Parcelable {

    String image; //url of cake's image
    List<String> price; // price of cake
    String name; //cake of name

    public Cake(String image, List<String> price, String name) {
        this.image = image;
        this.price = price;
        this.name = name;
    }

    public Cake() {
    }

    protected Cake(Parcel in) {
        image = in.readString();
        price = in.createStringArrayList();
        name = in.readString();
    }

    public static final Creator<Cake> CREATOR = new Creator<Cake>() {
        @Override
        public Cake createFromParcel(Parcel in) {
            return new Cake(in);
        }

        @Override
        public Cake[] newArray(int size) {
            return new Cake[size];
        }
    };

    public String getImage() {
        return image;
    }

    public List<String> getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name : "+name+" : price : "+price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeStringList(price);
        parcel.writeString(name);
    }
}
