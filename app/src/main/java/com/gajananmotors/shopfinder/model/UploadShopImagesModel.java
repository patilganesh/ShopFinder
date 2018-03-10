package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;
/**
 * Created by Ashwin on 1/18/2018.
 */
public class UploadShopImagesModel {
    @SerializedName("result")
    private int result;
    @SerializedName("image")
    private String image;
    @SerializedName("msg")
    private String msg;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

   /* public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }*/
}
