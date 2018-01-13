package com.gajananmotors.shopfinder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.facebook.GraphRequest.TAG;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs";

    @Override
        public void onTokenRefresh() {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e(TAG, "Refreshed token: " + refreshedToken);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
//      setting values to sharedpreferences keys.
            editor.putString(Constant.DEVICETOKEN, refreshedToken);
            editor.apply();
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
           // sendRegistrationToServer(refreshedToken);
        }
}


























