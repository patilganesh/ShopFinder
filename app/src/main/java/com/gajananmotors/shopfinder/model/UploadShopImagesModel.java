package com.gajananmotors.shopfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 1/18/2018.
 */

public class UploadShopImagesModel {
    @SerializedName("result")
    private int result;
    @SerializedName("count")
    private int count;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
