package com.gajananmotors.shopfinder.category;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 11/29/2017.
 */

public class CategoryList {
   @SerializedName("Category")
    ArrayList<Category>categories=new ArrayList<>();

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
