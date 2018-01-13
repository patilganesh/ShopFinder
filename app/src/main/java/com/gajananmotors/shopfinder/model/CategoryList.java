package com.gajananmotors.shopfinder.model;

import com.gajananmotors.shopfinder.model.Category;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 11/29/2017.
 */

public class CategoryList {
    @SerializedName("categories")
    ArrayList<Category>categories=new ArrayList<>();

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
