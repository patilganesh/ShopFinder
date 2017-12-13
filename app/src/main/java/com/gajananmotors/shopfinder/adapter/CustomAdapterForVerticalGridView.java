package com.gajananmotors.shopfinder.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.MainActivity;
import com.gajananmotors.shopfinder.activity.SubCategory;

/**
 * Created by asus on 29-Nov-17.
 */

public class CustomAdapterForVerticalGridView extends RecyclerView.Adapter<CustomAdapterForVerticalGridView.MyViewHolder> {


    String[] name;
    Context context;
    int[] imageId;

    private static LayoutInflater inflater = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView images;

        public MyViewHolder(View rowView) {
            super(rowView);

            text = rowView.findViewById(R.id.category_name_vertical);
            images = rowView.findViewById(R.id.category_imgs_vertical);

        }
    }

    public CustomAdapterForVerticalGridView(MainActivity mainActivity, String[] namelist, int[] imglist) {

        this.name = namelist;
        this.imageId = imglist;
        context = mainActivity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_grid_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(name[position]);
        holder.images.setImageResource(imageId[position]);
        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,SubCategory.class));
            }
        });

    }




    @Override
    public int getItemCount() {
        return name.length;
    }

    @Override
    public int getItemViewType(int position) {

        //Toast.makeText(context, "You Clicked " + name[position], Toast.LENGTH_SHORT).show();

        return super.getItemViewType(position);
    }

}