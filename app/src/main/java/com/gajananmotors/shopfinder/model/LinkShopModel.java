package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2/15/2018.
 */




import com.google.gson.annotations.SerializedName;


    public class LinkShopModel {
        @SerializedName("shop_id")
        private int shop_id;
        @SerializedName("owner_id")
        private int owner_id;
        @SerializedName("category_id")
        private int category_id;
        @SerializedName("sub_category_id")
        private int sub_category_id;
        @SerializedName("category_name")
        private String category_name;
        @SerializedName("sub_category_name")
        private String sub_category_name;
        @SerializedName("category_search")
        private String category_search;
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
        @SerializedName("place_search")
        private String place_search;
        @SerializedName("website")
        private String website;
        @SerializedName("shop_pic")
        private String shop_pic;
        @SerializedName("image1")
        private String image1;
        @SerializedName("image2")
        private String image2;
        @SerializedName("image3")
        private String image3;
        @SerializedName("image4")
        private String image4;
        @SerializedName("image5")
        private String image5;
        @SerializedName("image6")
        private String image6;
        @SerializedName("country_code")
        private String country_code;
        @SerializedName("shop_mob_no")
        private String shop_mob_no;
        @SerializedName("status")
        private int status;
        public LinkShopModel(int shop_id, int owner_id, int category_id, int sub_category_id, String category_name,
                              String sub_category_name,
                              String category_search, String shop_name, String shop_timing, String address, String shop_details,
                              String shop_lat, String shop_long, String country, String state, String city,
                              String pincode, String area, String place_search, String website, String shop_pic,
                              String image1, String image2, String image3, String image4,
                              String image5, String image6, String country_code, String shop_mob_no, int status) {
            this.setShop_id(shop_id);
            this.setOwner_id(owner_id);
            this.setCategory_id(category_id);
            this.setSub_category_id(sub_category_id);
            this.setCategory_id(category_id);
            this.setCategory_name(category_name);
            this.setSub_category_name(sub_category_name);
            this.setCategory_search(category_search);
            this.setShop_name(shop_name);
            this.setShop_timing(shop_timing);
            this.setAddress(address);
            this.setShop_details(shop_details);
            this.setShop_lat(shop_lat);
            this.setShop_long(shop_long);
            this.setCountry(country);
            this.setState(state);
            this.setCity(city);
            this.setPincode(pincode);
            this.setArea(area);
            this.setPlace_search(place_search);
            this.setWebsite(website);
            this.setShop_pic(shop_pic);
            this.setImage1(image1);
            this.setImage2(image2);
            this.setImage3(image3);
            this.setImage4(image4);
            this.setImage5(image5);
            this.setImage6(image6);
            this.setCountry_code(country_code);
            this.setShop_mob_no(shop_mob_no);
            this.setStatus(status);

        }


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

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getSub_category_name() {
            return sub_category_name;
        }

        public void setSub_category_name(String sub_category_name) {
            this.sub_category_name = sub_category_name;
        }

        public String getCategory_search() {
            return category_search;
        }

        public void setCategory_search(String category_search) {
            this.category_search = category_search;
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

        public String getPlace_search() {
            return place_search;
        }

        public void setPlace_search(String place_search) {
            this.place_search = place_search;
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

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }

        public String getImage4() {
            return image4;
        }

        public void setImage4(String image4) {
            this.image4 = image4;
        }

        public String getImage5() {
            return image5;
        }

        public void setImage5(String image5) {
            this.image5 = image5;
        }

        public String getImage6() {
            return image6;
        }

        public void setImage6(String image6) {
            this.image6 = image6;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getShop_mob_no() {
            return shop_mob_no;
        }

        public void setShop_mob_no(String shop_mob_no) {
            this.shop_mob_no = shop_mob_no;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


    }


