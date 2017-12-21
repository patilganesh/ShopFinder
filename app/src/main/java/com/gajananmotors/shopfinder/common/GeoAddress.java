package com.gajananmotors.shopfinder.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by asus on 21-Dec-17.
 */

public class GeoAddress {

    public static String getAddress(double latitude, double longitude,Context context) {
        Geocoder geocoder;
        List<Address> addresses;
        String result=null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(" ");
                }
                sb.append(address.getFeatureName()).append(" ");
                sb.append(address.getThoroughfare()).append(" ");
                sb.append(address.getSubLocality()).append(" ");
                sb.append(address.getLocality()).append(" ");
                sb.append(address.getPostalCode()).append(" ");
                sb.append(address.getCountryName());
                result = sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getUrlWithDistance(String current_address) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&");
        googlePlacesUrl.append("origins=" + current_address);
        googlePlacesUrl.append("&destinations=" + "Hadapsar");
        googlePlacesUrl.append("&key=" + "AIzaSyDZyQsqWLtFJDQHlckNgpiEVAT4LwuaBj8");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
}
