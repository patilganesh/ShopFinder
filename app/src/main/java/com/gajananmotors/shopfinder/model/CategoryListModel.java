package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 11/29/2017.
 */

public class CategoryListModel {
    @SerializedName("categories")
    ArrayList<CategoryModel> categories = new ArrayList<>();

    public ArrayList<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryModel> categories) {
        this.categories = categories;
    }
}
