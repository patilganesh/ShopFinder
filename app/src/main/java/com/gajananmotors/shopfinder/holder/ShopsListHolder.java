package com.gajananmotors.shopfinder.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.helper.CircleImageView;

/**
 * Created by Ashwin on 1/3/2018.
 */
public class ShopsListHolder extends RecyclerView.ViewHolder {
    public TextView name, type, distance, address, timing, call, weburl;
    public CircleImageView image;
   public ImageView googleShopDirection;


    public ShopsListHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.shop_name);
        address = itemView.findViewById(R.id.shop_address);
        distance = itemView.findViewById(R.id.shop_distance);
        type = itemView.findViewById(R.id.shop_type);
        timing = itemView.findViewById(R.id.shop_timing);
        call = itemView.findViewById(R.id.shop_timing);
        weburl = itemView.findViewById(R.id.website);
        image = itemView.findViewById(R.id.shop_image);
        googleShopDirection = itemView.findViewById(R.id.googleShopDirection);
    }
}