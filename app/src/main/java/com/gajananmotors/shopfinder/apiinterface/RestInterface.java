package com.gajananmotors.shopfinder.apiinterface;

import com.gajananmotors.shopfinder.model.CategoryList;
import com.gajananmotors.shopfinder.model.CreateShopModel;
import com.gajananmotors.shopfinder.model.LoginUser;
import com.gajananmotors.shopfinder.model.SubCategoryList;
import com.gajananmotors.shopfinder.model.UploadShopImagesModel;
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
    @FormUrlEncoded
    @POST("index.php/mobile_api/login_user")
    Call<LoginUser> loginUsersList(
            @Field("mob_no") String mob_no,
            @Field("password") String password,
            @Field("device_token") String device_token
    );
    @POST("index.php/mobile_api/get_categories")
    Call<CategoryList> getCategoryList();
    @FormUrlEncoded
    @POST("index.php/mobile_api/get_sub_categories")
    Call<SubCategoryList> getSubCategoryList(
            @Field("category_id") int category_id
    );

    @Multipart
    @POST("index.php/mobile_api/create_shop")
    Call<CreateShopModel> createShop(
            @Part("category_id") int category_id,
            @Part("sub_category_id") int sub_category_id,
            @Part("category_search") String category_search,
            @Part("owner_id") int owner_id,
            @Part("shop_name") String shop_name,
            @Part("shop_timing") String shop_timing,
            @Part("address") String address,
            @Part("shop_details") String shop_details,
            @Part("shop_lat") String shop_lat,
            @Part("shop_long") String shop_long,
            @Part("area") String area,
            @Part("city") String city,
            @Part("state") String state,
            @Part("country") String country,
            @Part("pincode") String pincode,
            @Part("place_search") String place_search,
            @Part("website") String website,
            @Part MultipartBody.Part shop_pic,
            @Part("shop_mob_no") String shop_mob_no
    );

    @Multipart
    @POST("index.php/mobile_api/upload_shop_images")
    Call<UploadShopImagesModel> uploadShopImages(
            @Part("shop_id") int shop_id,
            @Part MultipartBody.Part image,
            @Part("shop_mob_no") String shop_mob_no,
            @Part("action") String action,
            @Part("count") int count
    );
}
//index.php?r=customerRegister/createOwner"