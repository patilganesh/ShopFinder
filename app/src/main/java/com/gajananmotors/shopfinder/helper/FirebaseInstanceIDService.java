package com.gajananmotors.shopfinder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.facebook.GraphRequest.TAG;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private SharedPreferences sharedpreferences;
    private String refreshedToken = "";
    @Override
        public void onTokenRefresh() {
            // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e(TAG, "Refreshed token: " + refreshedToken);
            Constant.device_token=refreshedToken;
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
//      setting values to sharedpreferences keys.
            editor.putString(Constant.DEVICE_TOKEN, refreshedToken);
            editor.apply();
        }
}


























