package com.gajananmotors.shopfinder.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.model.ShopsListModel;

import java.util.ArrayList;

/**
 * Created by dell on 1/30/2018.
 */

public class ViewShopList implements Parcelable {
    private int shop_id;
    private double latitude,longitude;
    private String strShop_name;
    private String strAddress;
    private String strCategory;
    private String strSub_category;
    private String strWeburl;
    private String strMobile;
    private String strShop_pic;
    private String strShop_time;
    private String strShop_services;
    public String getStrShop_time() {
        return strShop_time;
    }

    public void setStrShop_time(String strShop_time) {
        this.strShop_time = strShop_time;
    }

    public String getStrShop_services() {
        return strShop_services;
    }

    public void setStrShop_services(String strShop_services) {
        this.strShop_services = strShop_services;
    }
    private ArrayList arrayList = new ArrayList<>();
    public String getStrShop_pic() {
        return strShop_pic;
    }
    public void setStrShop_pic(String strShop_pic) {
        this.strShop_pic = strShop_pic;
    }
    public ViewShopList(){}
    public ViewShopList(Parcel in) {
        shop_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        strShop_name = in.readString();
        strAddress = in.readString();
        strCategory = in.readString();
        strSub_category = in.readString();
        strWeburl = in.readString();
        strMobile = in.readString();
        strShop_pic = in.readString();
        strShop_time = in.readString();
        strShop_services = in.readString();
        in.readList(arrayList,null);
    }

    public static final Creator<ViewShopList> CREATOR = new Creator<ViewShopList>() {
        @Override
        public ViewShopList createFromParcel(Parcel in) {
            return new ViewShopList(in);
        }
        @Override
        public ViewShopList[] newArray(int size) {
            return new ViewShopList[size];
        }
    };

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }


    public String getStrShop_name() {
        return strShop_name;
    }

    public void setStrShop_name(String strShop_name) {
        this.strShop_name = strShop_name;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrSub_category() {
        return strSub_category;
    }

    public void setStrSub_category(String strSub_category) {
        this.strSub_category = strSub_category;
    }

    public String getStrWeburl() {
        return strWeburl;
    }

    public void setStrWeburl(String strWeburl) {
        this.strWeburl = strWeburl;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shop_id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(strShop_name);
        dest.writeString(strAddress);
        dest.writeString(strCategory);
        dest.writeString(strSub_category);
        dest.writeString(strWeburl);
        dest.writeString(strMobile);
        dest.writeString(strShop_pic);
        dest.writeString(strShop_time);
        dest.writeString(strShop_services);
        dest.writeList(arrayList);
    }
}
