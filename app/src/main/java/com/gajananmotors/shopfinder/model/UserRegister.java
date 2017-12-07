package com.gajananmotors.shopfinder.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwin on 11/18/2017.
 */
/*Creating POJO class for User or ShopOwner */
public class UserRegister {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
   /* @SerializedName("address")
    private String address;
   */
    @SerializedName("mobileno")
    private String mobileNo;
    @SerializedName("dob")
    private String dob;
    @SerializedName("password")
    private String password;
    @SerializedName("image")
    private String image;
    @SerializedName("country_code")
    private String country_code;
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getImage() {
        return image;

    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
   /* public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }*/
    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
