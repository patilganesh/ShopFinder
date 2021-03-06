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
    private String strservices;

    public String getStrservices() {
        return strservices;
    }

    public void setStrservices(String strservices) {
        this.strservices = strservices;
    }


    protected ViewShopList(Parcel in) {
        shop_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        strShop_name = in.readString();
        strAddress = in.readString();
        strCategory = in.readString();
        strSub_category = in.readString();
        strWeburl = in.readString();
        strMobile = in.readString();
        strShopTime = in.readString();
        strShop_pic = in.readString();
        strservices = in.readString();
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

    public String getStrShopTime() {
        return strShopTime;
    }

    public void setStrShopTime(String strShopTime) {
        this.strShopTime = strShopTime;
    }

    private String strShopTime;

    public String getStrShop_pic() {
        return strShop_pic;
    }

    public void setStrShop_pic(String strShop_pic) {
        this.strShop_pic = strShop_pic;
    }

    private String strShop_pic;
    private ArrayList arrayList=new ArrayList<>();
    public ViewShopList(){}
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
        dest.writeString(strShopTime);
        dest.writeString(strShop_pic);
        dest.writeString(strservices);
        dest.writeList(arrayList);
    }



}
