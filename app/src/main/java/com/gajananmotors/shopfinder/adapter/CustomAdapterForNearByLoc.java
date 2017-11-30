package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;

/**
 * Created by asus on 18-Nov-17.
 */

public class CustomAdapterForNearByLoc extends RecyclerView.Adapter<CustomAdapterForNearByLoc.MyViewHolder> {


    Activity context1;
    String[] name;
    String[] address;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shop_name;
        public TextView shop_address;

        public MyViewHolder(View rowView) {
            super(rowView);
            shop_name = rowView.findViewById(R.id.list_shopname);
            shop_address = rowView.findViewById(R.id.list_address);

        }
    }


    public CustomAdapterForNearByLoc(Activity context, String[] source, String[] destination) {

        this.name = source;
        this.address = destination;
        this.context1 = context;
    }

    @Override
    public CustomAdapterForNearByLoc.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_map_activity_data, parent, false);

        return new CustomAdapterForNearByLoc.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.shop_name.setText(name[position]);
        holder.shop_address.setText(address[position]);

    }

    @Override
    public int getItemCount() {
        return name.length;
    }


}