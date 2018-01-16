package com.gajananmotors.shopfinder.helper;


import android.content.SharedPreferences;

public class Constant{


    private static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;
    public static final String OWNER_NAME = "owner_name";
    public static final String  ADDRESS= "address";
    public static final String MOBILE = "mob_no";
    public static final String OWNWER_EMAIL = "owner_email";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String OWNER_ID = "owner_id";
    public static final String OWNER_PROFILE = "owner_profile";
    public static final String DEVICE_TOKEN = "device_token";



    public static final String PROVIDER_URL = "http//control.c2sms.com/api/sendhttp.php";
    public static final String QUESTION_PARAMETER = "?";
    public static final String EQUALS_TO_PARAMETER = "=";
    public static final String AMPERSAND_PARAMETER = "&";
    public static final String AUTHKEY_PARAMETER = "authkey";
    public static final String MOBILES_PARAMETER = "mobiles";
    public static final String MESSAGE_PARAMETER = "message";
    public static final String SENDER_PARAMETER = "sender";
    public static final String ROUTE_PARAMETER = "route";
    public static final String SENDER = "TR";
    public static final String ROUTE = "4";
    public static final String AUTHKEY = "119718AkintvzJaWd578dd8d3";
    public static final String MESSAGE = "Hi, Your otp is:";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static boolean doubleBackToExitPressedOnce = false;







}