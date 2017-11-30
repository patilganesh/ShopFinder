package com.gajananmotors.shopfinder.apiinterface;

import com.gajananmotors.shopfinder.model.CategoryList;
import com.gajananmotors.shopfinder.model.LoginUsersList;
import com.gajananmotors.shopfinder.model.UserRegister;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ashwin on 11/18/2017.
 */
public interface RestInterface {
    @POST("service_url")
    Call<UserRegister>userRegister(@Body UserRegister user);
    @GET("service_url")
    Call<LoginUsersList>getUsers();
    @GET("service_url")
    Call<CategoryList>getCategoryList();
}