package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


    public class ShopsArrayListModel {
        @SerializedName("shop_list")
        private ArrayList<ShopsListModel> shopsListModelArrayList = new ArrayList<>();

        public ArrayList<ShopsListModel> getShopList() {
            return shopsListModelArrayList;
        }
        public void setShopList(ArrayList<ShopsListModel> shopsListModelArrayList) {
            this.shopsListModelArrayList = shopsListModelArrayList;
        }
    }
