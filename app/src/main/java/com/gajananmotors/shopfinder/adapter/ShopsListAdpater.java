package com.gajananmotors.shopfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.MainActivity;

import com.gajananmotors.shopfinder.holder.ShopsListHolder;
import com.gajananmotors.shopfinder.model.ShopsList;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListHolder> {
    ArrayList<ShopsList> list = new ArrayList<>();
    Context context;
    private String query;

    public ShopsListAdpater(ArrayList<ShopsList> shops_list) {
        this.list = shops_list;
    }

    @Override
    public ShopsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_shops_list, parent, false);
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

    public void setFilter(ArrayList<ShopsList> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}