package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 11/18/2017.
 */

public class LoginUser {
    @SerializedName("mob_no")
    private String mob_no;

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    @SerializedName("password")
    private String password;
    @SerializedName("device_token")
    private String device_token;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
