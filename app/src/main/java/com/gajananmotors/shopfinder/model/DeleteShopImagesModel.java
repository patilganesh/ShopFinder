package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 2/16/2018.
 */

public class DeleteShopImagesModel {
    @SerializedName("msg")
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
