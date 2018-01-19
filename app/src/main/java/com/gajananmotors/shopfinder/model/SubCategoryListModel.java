package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/5/2018.
 */
public class SubCategoryListModel {
    @SerializedName("sub_categories")
    private ArrayList<SubCategoryModel> subcategory = new ArrayList<>();

    public ArrayList<SubCategoryModel> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<SubCategoryModel> subcategory) {
        this.subcategory = subcategory;
    }
}
