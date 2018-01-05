package com.gajananmotors.shopfinder.model;

/**
 * Created by Ashwin on 12/30/2017.
 */
public class ShopsList {
    private String name;
    private String distance;
    private String address;
    private String timing;
    private String image;
    private String web_url;
    private String type;
    private String mobileNo;

    public ShopsList(String name, String distance, String address, String timing, String web_url, String type, String mobileNo)
    {
        this.setName(name);
        this.setDistance(distance);
        this.setAddress(address);
        this.setTiming(timing);
        this.setWeb_url(web_url);
        this.setType(type);
        this.setMobileNo(mobileNo);
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeb_url() {
        return web_url;
    }
    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
