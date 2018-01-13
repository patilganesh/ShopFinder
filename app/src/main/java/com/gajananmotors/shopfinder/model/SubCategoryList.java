package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/5/2018.
 */
public class SubCategoryList {
    @SerializedName("sub_categories")
    private ArrayList<SubCategory> subcategory = new ArrayList<>();

    public ArrayList<SubCategory> getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(ArrayList<SubCategory> subcategory) {
        this.subcategory = subcategory;
    }
}
