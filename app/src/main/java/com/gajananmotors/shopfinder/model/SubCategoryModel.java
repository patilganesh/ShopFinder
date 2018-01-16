package com.gajananmotors.shopfinder.model;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Ashwin on 1/5/2018.
 */
public class SubCategoryModel {
    @SerializedName("sub_category_id")
    private int sub_category_id;
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("status")
    private String status;

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
