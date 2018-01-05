package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 11/29/2017.
 */

public class Category {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("images")
    private String images;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}
