package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2/22/2018.
 */

public class AddShopServicesModel {
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

    @SerializedName("msg")
    private String msg;
}
