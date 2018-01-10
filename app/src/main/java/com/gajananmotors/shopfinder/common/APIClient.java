package com.gajananmotors.shopfinder.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ashwin on 11/18/2017.
 */
public class APIClient
{
//TODO:Finish this!
    private static Retrofit retrofit;
    private APIClient(){}
    public static Retrofit getClient() {

        if(retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.findashop.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
//index.php?r=customerRegister/createOwner
//http://www.findashop.in
