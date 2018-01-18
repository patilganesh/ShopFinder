package com.gajananmotors.shopfinder.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Ashwin on 11/18/2017.
 */
public class APIClient {
    //TODO:Finish this!
    private static Retrofit retrofit;

    private APIClient() {
    }

    public static String getImagePath() {
        return "http://www.findashop.in/images/";
    }

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.findashop.in/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
//index.php?r=customerRegister/createOwner
//http://www.findashop.in
