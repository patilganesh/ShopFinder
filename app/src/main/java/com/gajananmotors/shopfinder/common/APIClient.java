package com.gajananmotors.shopfinder.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ashwin on 11/18/2017.
 */
public class APIClient
{
//TODO:Finish this!
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
