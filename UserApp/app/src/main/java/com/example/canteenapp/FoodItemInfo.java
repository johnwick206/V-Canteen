package com.example.canteenapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class FoodItemInfo {
    String itemName, itemLocation, catagory ,uri , cost , itemQty;
     Context context;

    public FoodItemInfo() {
        //Firebase need this constructor
    }

    public FoodItemInfo(String itemName, String itemLocation, String catagory, String uri , String cost) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.catagory = catagory;
        this.uri = uri;
        this.cost = cost;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getUri() {
        return uri;
    }

    public String getCost() {
        return cost;
    }

    public String getItemQty() {
        SharedPreferences  sharedPreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        itemQty = sharedPreferences.getString( getItemName() , "1");
        return itemQty;
    }

    public void setItemQty(String itemQty) {

        SharedPreferences  sharedPreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getItemName() , itemQty);
        editor.apply();
    }

    public void setContext(Context context){
        this.context = context ;
    }
}
