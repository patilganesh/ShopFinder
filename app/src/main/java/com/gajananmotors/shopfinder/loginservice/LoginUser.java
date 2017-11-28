package com.gajananmotors.shopfinder.loginservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 11/18/2017.
 */

public class LoginUser {
    @SerializedName("mobileNo")
    private String mobileNo;
    @SerializedName("password")
    private String password;
    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
