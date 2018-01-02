package com.gajananmotors.shopfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.MainActivity;

import com.gajananmotors.shopfinder.model.ShopsList;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListAdpater.ShopsListHolder> {
    ArrayList<ShopsList> list = new ArrayList<>();
    MainActivity context;
    public ShopsListAdpater(ArrayList<ShopsList> shops_list, MainActivity mainActivity) {
        this.list=shops_list;
        this.context=mainActivity;
    }
    @Override
    public ShopsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.activity_shops_list,parent,false);
        return new ShopsListHolder(view);
    }
    @Override
    public void onBindViewHolder(ShopsListHolder holder, int position) {
    holder.name.setText(list.get(position).getName());
    holder.address.setText(list.get(position).getAddress());
    holder.distance.setText(list.get(position).getDistance());
    holder.timing.setText(list.get(position).getTiming());
    holder.type.setText(list.get(position).getType());
    holder.weburl.setText(list.get(position).getWeb_url());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShopsListHolder extends RecyclerView.ViewHolder
    {
        public TextView name,type,distance,address,timing,call,weburl;
        public ImageView image;
        public ShopsListHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.shop_name);
            type=itemView.findViewById(R.id.shop_type);
            distance=itemView.findViewById(R.id.shop_distance);
            timing=itemView.findViewById(R.id.shop_timing);
            call=itemView.findViewById(R.id.shop_call);
            weburl=itemView.findViewById(R.id.website);
            image=itemView.findViewById(R.id.shop_image);
       }
    }
    public void setFilter(ArrayList<ShopsList>newList)
    {
        if(newList.isEmpty())
            Toast.makeText(context, "newList is Empty", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(context, "newList is not Empty", Toast.LENGTH_SHORT).show();
            list = new ArrayList<>();
            list.addAll(newList);
            notifyDataSetChanged();
        }
    }

}
