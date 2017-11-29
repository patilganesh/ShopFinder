package com.gajananmotors.shopfinder.APIInterface;

import com.gajananmotors.shopfinder.category.CategoryList;
import com.gajananmotors.shopfinder.loginservice.LoginUsers;
import com.gajananmotors.shopfinder.registerservice.UserRegister;

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
    Call<LoginUsers>getUsers();
    @GET("service_url")
    Call<CategoryList>getCategoryList();
}