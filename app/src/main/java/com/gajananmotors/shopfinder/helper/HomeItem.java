package com.gajananmotors.shopfinder.helper;

/**
 * Created by dell on 2/13/2018.
 */

public class HomeItem {
    String str_title, str_img, str_sub_title;

    public HomeItem(String str_title, String str_img, String str_sub_title) {
        this.str_title = str_title;
        this.str_img = str_img;
        this.str_sub_title = str_sub_title;
    }

    public String getStr_title() {
        return str_title;
    }

    public void setStr_title(String str_title) {
        this.str_title = str_title;
    }

    public String getStr_img() {
        return str_img;
    }

    public void setStr_img(String str_img) {
        this.str_img = str_img;
    }

    public String getStr_sub_title() {
        return str_sub_title;
    }

    public void setStr_sub_title(String str_sub_title) {
        this.str_sub_title = str_sub_title;
    }
}
