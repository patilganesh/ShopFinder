package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 1/22/2018.
 */

public class DeleteUserModel {
    @SerializedName("result")
    private int result;
    @SerializedName("msg")
    private String msg;

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
