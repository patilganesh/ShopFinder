package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.AddPostActivity;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashwin on 2/12/2018.
 */
public class ShopImagesAdapter extends RecyclerView.Adapter<ShopImagesAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<String> images = new ArrayList<>();
    private int shop_id;
    private String shop_image;

    public ShopImagesAdapter(Activity activity, ArrayList<String> images, int shop_id) {
        this.activity = activity;
        this.images = images;
        this.shop_id = shop_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.shops_images_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        shop_image = "http://findashop.in/images/shop_profile/" + shop_id + "/" + images.get(position);
        Picasso.with(activity)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + images.get(position))
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(holder.imageView);
        holder.shopImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        activity);
                alertDialog.setTitle("Choose your option: ");
                alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  Toast.makeText(activity, "!", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public ImageView shopImageEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shop_img);
            shopImageEdit = itemView.findViewById(R.id.shop_img_edit);

        }
    }
}
