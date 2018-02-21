package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/5/2018.
 */
public class ShopServicesListModel {
    @SerializedName("services")
    private ArrayList<ShopServicesModel> shopServicesModels = new ArrayList<>();

    public ArrayList<ShopServicesModel> getShopServicesModels() {
        return shopServicesModels;
    }

    public void setShopServicesModels(ArrayList<ShopServicesModel> shopServicesModels) {
        this.shopServicesModels = shopServicesModels;
    }
}
