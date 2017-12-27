package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.ItemDetailsActivity;

/**
 * Created by asus on 29-Nov-17.
 */

public class CustomAdapterForSubCategory extends RecyclerView.Adapter<CustomAdapterForSubCategory.MyViewHolder> {


    String[] name;
    Context context;
    int[] imageId;

    public CustomAdapterForSubCategory(Activity subCategory, String[] nameList, int[] imglist) {
        this.name = nameList;
        this.imageId = imglist;
        context = subCategory;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView images;


        public MyViewHolder(View rowView) {
            super(rowView);

            text = rowView.findViewById(R.id.txtsubcategory);
            images = rowView.findViewById(R.id.imgsubcategory);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(name[position]);
        holder.images.setImageResource(imageId[position]);

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ItemDetailsActivity.class));
            }
        });
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ItemDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}