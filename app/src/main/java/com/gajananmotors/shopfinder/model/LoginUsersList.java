package com.gajananmotors.shopfinder.model;

import com.gajananmotors.shopfinder.model.LoginUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ashwin on 11/18/2017.
 */
public class LoginUsersList {
    @SerializedName("users")
    private ArrayList<UserRegister> users = new ArrayList<>();

    public ArrayList<UserRegister> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserRegister> users) {
        this.users = users;
    }
}
