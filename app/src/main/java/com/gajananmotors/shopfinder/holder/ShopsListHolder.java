package com.gajananmotors.shopfinder.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;

/**
 * Created by Ashwin on 1/3/2018.
 */
public class ShopsListHolder extends RecyclerView.ViewHolder {
    public TextView name, type, distance, address, timing, call, weburl;
    public ImageView image;

    public ShopsListHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.shop_name);
        address = itemView.findViewById(R.id.shop_address);
        distance = itemView.findViewById(R.id.shop_distance);
        type = itemView.findViewById(R.id.shop_type);
        timing = itemView.findViewById(R.id.shop_timing);
        call = itemView.findViewById(R.id.shop_call);
        weburl = itemView.findViewById(R.id.website);
        image = itemView.findViewById(R.id.shop_image);
    }
}