package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.ViewPostActivity;
import com.gajananmotors.shopfinder.holder.ShopsListHolder;
import com.gajananmotors.shopfinder.model.ShopsListModel;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListHolder> {
    Activity activity;
    ArrayList<ShopsListModel> list = new ArrayList<>();
    private LinearLayout viewPostLayout;

    public ShopsListAdpater(Activity activity, LinearLayout viewPostLayout, ArrayList<ShopsListModel> shops_list) {
        this.list = shops_list;
        this.activity = activity;
        this.viewPostLayout = viewPostLayout;
    }

    public ShopsListAdpater(ArrayList<ShopsListModel> shops_list) {
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition();
            }
        });

    }

    private void transition() {
        Log.d("Allpost", "transition");

        Intent intent = new Intent(activity, ViewPostActivity.class);
        Pair<View, String> p1 = Pair.create((View) viewPostLayout, "view");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, p1);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<ShopsListModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}