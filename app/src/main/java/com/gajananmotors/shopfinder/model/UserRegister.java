package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MultipartBody;

/**
 * Created by Ashwin on 11/18/2017.
 */
/*Creating POJO class for User or ShopOwner */
public class UserRegister {
    @SerializedName("owner_id")
    private int owner_id;
    @SerializedName("owner_name")
    private String owner_name;
    @SerializedName("owner_email")
    private String owner_email;
    @SerializedName("mob_no")
    private String mob_no;
    @SerializedName("date_of_birth")
    private String date_of_birth;
    @SerializedName("image1")
    private String image1;
    @SerializedName("password")
    private String password;
    @SerializedName("device_token")
    private String device_token;
    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private int result;

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

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

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
