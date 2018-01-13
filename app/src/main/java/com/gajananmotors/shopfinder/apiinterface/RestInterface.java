package com.gajananmotors.shopfinder.apiinterface;

import com.gajananmotors.shopfinder.model.CategoryList;
import com.gajananmotors.shopfinder.model.LoginUser;
import com.gajananmotors.shopfinder.model.SubCategoryList;
import com.gajananmotors.shopfinder.model.UserRegister;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Ashwin on 11/18/2017.
 */
public interface RestInterface {
    //String BASE_URL = "http://192.168.2.6/event/";
    @Multipart
    @POST("index.php/mobile_api/create_shop_owner")
    Call<UserRegister> userRegister(
            @Part("owner_name") String owner_name,
            @Part("owner_email") String owner_email,
            @Part("mob_no") String mob_no,
            @Part("date_of_birth") String date_of_birth,
            @Part MultipartBody.Part image,
            @Part("password") String password,
            @Part("device_token") String device_token
    );

    Call<UserRegister> userRegister(
            @Part("owner_name") String owner_name,
            @Part("owner_email") String owner_email,
            @Part("mob_no") String mob_no,
            @Part("date_of_birth") String date_of_birth,
            @Part("image") String image1,
            @Part("password") String password,
            @Part("device_token") String device_token,
            @Part("msg") String msg
    );

    @FormUrlEncoded
    @POST("index.php/mobile_api/login_user")
    Call<LoginUser> loginUsersList(
            @Field("mob_no") String mob_no,
            @Field("password") String password,
            @Field("device_token") String device_token
    );

    Call<LoginUser> loginUsersList(
            @Field("mob_no") String mob_no,
            @Field("password") String password,
            @Field("device_token") String device_token,
            @Field("msg") String msg,
            @Field("owner_name") String owner_name,
            @Field("owner_email") String owner_email,
            @Field("otp") String otp
    );

    @POST("index.php/mobile_api/get_categories")
    Call<CategoryList> getCategoryList();

    @FormUrlEncoded
    @POST("index.php/mobile_api/get_sub_categories")
    Call<SubCategoryList> getSubCategoryList(
            @Field("category_id") int category_id
    );

    Call<SubCategoryList> getSubCategoryList(
            @Field("category_id") int category_id,
            @Field("sub_category_id") int sub_category_id,
            @Field("name") String name,
            @Field("image") String image,
            @Field("status") String status
    );
   /*
    @POST("service_url")
    Call<AddPost>postRegister(@Body AddPost post);*/
}
//index.php?r=customerRegister/createOwner"