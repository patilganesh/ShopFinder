package com.gajananmotors.shopfinder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Gajanan Motars on 12/20/2017.
 */

public class Config {


    public static boolean hasPermissions(Context context, String... permissions) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {


                    return false;
                }
            }
        }
        return true;

    }
}
