package com.gajananmotors.shopfinder.model;

import com.gajananmotors.shopfinder.common.StringCallback;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 1/17/2018.
 */

public class CreateShopModel {
    @SerializedName("shop_id")
    private int shop_id;
    @SerializedName("owner_id")
    private int owner_id;
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("sub_category_id")
    private int sub_category_id;
    @SerializedName("shop_name")
    private String shop_name;
    @SerializedName("shop_timing")
    private String shop_timing;
    @SerializedName("address")
    private String address;
    @SerializedName("shop_details")
    private String shop_details;
    @SerializedName("shop_lat")
    private String shop_lat;
    @SerializedName("shop_long")
    private String shop_long;
    @SerializedName("country")
    private String country;
    @SerializedName("state")
    private String state;
    @SerializedName("city")
    private String city;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("area")
    private String area;
    @SerializedName("website")
    private String website;
    @SerializedName("shop_pic")
    private String shop_pic;
    @SerializedName("shop_mob_no")
    private String shop_mob_no;
    @SerializedName("result")
    private int result;
    @SerializedName("msg")
    private String msg;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_timing() {
        return shop_timing;
    }

    public void setShop_timing(String shop_timing) {
        this.shop_timing = shop_timing;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShop_details() {
        return shop_details;
    }

    public void setShop_details(String shop_details) {
        this.shop_details = shop_details;
    }

    public String getShop_lat() {
        return shop_lat;
    }

    public void setShop_lat(String shop_lat) {
        this.shop_lat = shop_lat;
    }

    public String getShop_long() {
        return shop_long;
    }

    public void setShop_long(String shop_long) {
        this.shop_long = shop_long;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getShop_pic() {
        return shop_pic;
    }

    public void setShop_pic(String shop_pic) {
        this.shop_pic = shop_pic;
    }

    public String getShop_mob_no() {
        return shop_mob_no;
    }

    public void setShop_mob_no(String shop_mob_no) {
        this.shop_mob_no = shop_mob_no;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
