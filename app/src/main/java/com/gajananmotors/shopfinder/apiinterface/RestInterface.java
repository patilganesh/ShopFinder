package com.gajananmotors.shopfinder.apiinterface;

import com.gajananmotors.shopfinder.model.CategoryListModel;
import com.gajananmotors.shopfinder.model.CreateShopModel;
import com.gajananmotors.shopfinder.model.DeleteShopModel;
import com.gajananmotors.shopfinder.model.DeleteUserModel;
import com.gajananmotors.shopfinder.model.LoginUserModel;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.UpdateUserModel;
import com.gajananmotors.shopfinder.model.UploadShopImagesModel;
import com.gajananmotors.shopfinder.model.UserRegisterModel;

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
    Call<UserRegisterModel> userRegister(
            @Part("owner_name") String owner_name,
            @Part("owner_email") String owner_email,
            @Part("mob_no") String mob_no,
            @Part("date_of_birth") String date_of_birth,
            @Part MultipartBody.Part image,
            @Part("password") String password,
            @Part("device_token") String device_token
    );
    @FormUrlEncoded
    @POST("index.php/mobile_api/create_shop_owner")
    Call<UserRegisterModel> userRegisterforEmptyImage(
            @Field("owner_name") String owner_name,
            @Field("owner_email") String owner_email,
            @Field("mob_no") String mob_no,
            @Field("date_of_birth") String date_of_birth,
            @Field("password") String password,
            @Field("device_token") String device_token
    );
    @FormUrlEncoded
    @POST("index.php/mobile_api/login_user")
    Call<LoginUserModel> loginUsersList(
            @Field("mob_no") String mob_no,
            @Field("password") String password,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("index.php/mobile_api/delete_owner")
    Call<DeleteUserModel> deleteOwnerList(
            @Field("owner_id") int owner_id);
 /*   @FormUrlEncoded
    @POST("index.php/mobile_api/delete_shop")
    Call<DeleteShopModel> deleteShop(
            @Field("shop_id") int shop_id

    );*/
    @POST("index.php/mobile_api/get_categories")
    Call<CategoryListModel> getCategoryList();
    @FormUrlEncoded
    @POST("index.php/mobile_api/get_sub_categories")
    Call<SubCategoryListModel> getSubCategoryList(
            @Field("category_id") int category_id
    );
    @Multipart
    @POST("index.php/mobile_api/update_owner")
    Call<UpdateUserModel> updateRegister(
            @Part("owner_id") int owner_id,
            @Part("owner_name") String owner_name,
            @Part("owner_email") String owner_email,
            @Part("mob_no") String mob_no,
            @Part("date_of_birth") String date_of_birth,
            @Part MultipartBody.Part image

    );
    @FormUrlEncoded
    @POST("index.php/mobile_api/update_owner")
    Call<UpdateUserModel> updateRegisterforEmptyImage(
            @Field("owner_name") String owner_name,
            @Field("owner_email") String owner_email,
            @Field("mob_no") String mob_no,
            @Field("date_of_birth") String date_of_birth,
            @Field("owner_id") int owner_id
    );
    @Multipart
    @POST("index.php/mobile_api/create_shop")
    Call<CreateShopModel> createShop(
            @Part("category_id") int category_id,
            @Part("sub_category_id") int sub_category_id,
            @Part("category_name") String category_name,
            @Part("sub_category_name") String sub_category_name,
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

    @FormUrlEncoded
    @POST("index.php/mobile_api/create_shop")
    Call<CreateShopModel> createShopforEmptyImage(
            @Field("category_id") int category_id,
            @Field("sub_category_id") int sub_category_id,
            @Field("category_name") String category_name,
            @Field("sub_category_name") String sub_category_name,
            @Field("category_search") String category_search,
            @Field("owner_id") int owner_id,
            @Field("shop_name") String shop_name,
            @Field("shop_timing") String shop_timing,
            @Field("address") String address,
            @Field("shop_details") String shop_details,
            @Field("shop_lat") String shop_lat,
            @Field("shop_long") String shop_long,
            @Field("area") String area,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("pincode") String pincode,
            @Field("place_search") String place_search,
            @Field("website") String website,
            @Field("shop_mob_no") String shop_mob_no
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