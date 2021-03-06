package com.gajananmotors.shopfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.GallaryActivity;
import com.gajananmotors.shopfinder.activity.MainActivity;
import com.gajananmotors.shopfinder.activity.SubCategoryActivity;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterForGallary extends RecyclerView.Adapter<CustomAdapterForGallary.MyViewHolder> {
    ImageView imgView;
    int shop_id;
    // private String[] name;
    private Context context;
    private ArrayList<String> imageList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView images;

        public MyViewHolder(View rowView) {
            super(rowView);

            images = rowView.findViewById(R.id.shop_img);
            // setFadeAnimation(images,200);
            //  setFadeAnimation(text,200);
        }
    }

    public CustomAdapterForGallary(GallaryActivity mainActivity, ArrayList<String> imageList, int shop_id, ImageView selectedImageView) {
        this.context = mainActivity;
        this.shop_id = shop_id;
        this.imgView=selectedImageView;
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallary_images_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context)
                        .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + imageList.get(position))
                        .fit()
                        .placeholder(R.drawable.no_image_found)
                        .into(imgView);
                //  context.startActivity(new Intent(context, SubCategoryActivity.class));
            }
        });
        Picasso.with(context)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + imageList.get(position))
                .fit()
                .placeholder(R.drawable.no_image_found)
                .into(holder.images);
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }
    @Override
    public int getItemViewType(int position) {

        ImageView imageView = new ImageView(context);
        Picasso.with(context)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + imageList.get(position))
                .fit()
                .placeholder(R.drawable.no_image_found)
                .into(imageView);
        //imageView.setImageResource(images[position]); // set image in ImageView*/
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 300)); // set ImageView param

        return super.getItemViewType(position);
    }
}