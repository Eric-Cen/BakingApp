package com.eightmin4mile.goandroid.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by goandroid on 6/25/18.
 */

public class Ingredient implements Parcelable {

    /*
    {
        "quantity":2,
        "measure":"CUP",
        "ingredient":"Graham Cracker crumbs"
    },
     */

    private double quantiy;
    private String measure;
    private String ingredient;

    public Ingredient(double quantiy,
                      String measure,
                      String ingredient) {
        this.quantiy = quantiy;
        this.measure = measure;
        this.ingredient = ingredient;

    }

    public double getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(double quantiy) {
        this.quantiy = quantiy;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

//    private double quantiy;
//    private String measure;
//    private String ingredient;


    @Override
    public int describeContents() {
        return 0;
    }

    protected Ingredient(Parcel in) {
        this.quantiy = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantiy);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
