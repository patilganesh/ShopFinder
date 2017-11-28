package com.gajananmotors.shopfinder.loginservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 11/18/2017.
 */
public class LoginUsers {
    @SerializedName("users")
    private ArrayList<LoginUser> users=new ArrayList<>();

    public ArrayList<LoginUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<LoginUser> users) {
        this.users = users;
    }
}
