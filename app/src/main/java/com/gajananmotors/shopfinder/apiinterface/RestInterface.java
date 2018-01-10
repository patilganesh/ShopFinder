package com.gajananmotors.shopfinder.apiinterface;

import com.gajananmotors.shopfinder.model.AddPost;
import com.gajananmotors.shopfinder.model.CategoryList;
import com.gajananmotors.shopfinder.model.LoginUsersList;
import com.gajananmotors.shopfinder.model.SubCategoryList;
import com.gajananmotors.shopfinder.model.UserRegister;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Ashwin on 11/18/2017.
 */
public interface RestInterface {
    //String BASE_URL = "http://192.168.2.6/event/";
    @FormUrlEncoded
    @Multipart
    @POST("index.php/mobile_api/create_shop_owner")
    Call<UserRegister> userRegister(
            @Field("owner_name") String owner_name,
            @Field("owner_email") String owner_email,
            @Field("mob_no") String mob_no,
            @Field("date_of_birth") String date_of_birth,
            @Part MultipartBody.Part image,
            @Field("password") String password,
            @Field("device_token") String device_token

    );
   /* @GET("index.php?r=CustomerRegister/IndexApi")
    Call<LoginUsersList>getUsers();
    @GET("index.php?r=Category/getCategory")
    Call<CategoryList>getCategoryList();
    @GET("index.php?r=SubCategory/admin")
    Call<SubCategoryList> getSubCategoryList();
    @POST("service_url")
    Call<AddPost>postRegister(@Body AddPost post);*/
}
//index.php?r=customerRegister/createOwner"