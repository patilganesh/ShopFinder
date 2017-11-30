package com.gajananmotors.shopfinder.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ashwin on 11/24/2017.
 */

public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity= (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=connectivity.getActiveNetworkInfo();
        boolean isConnect=activeNetwork!=null&&activeNetwork.isConnectedOrConnecting();
        return isConnect;
    }
}
