package com.gajananmotors.shopfinder.adapter;

/**
 * Created by asus on 13-Jan-18.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.gajananmotors.shopfinder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomGalleryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> images = new ArrayList<>();
    private int shop_id;
    int itemBackground;

    public CustomGalleryAdapter(Context c, ArrayList<String> images, int shop_id) {
        context = c;
        this.images = images;
        this.shop_id = shop_id;
      /*  TypedArray array = c.obtainStyledAttributes(R.styleable.MyGallery);
        itemBackground = array.getResourceId
                (R.styleable.MyGallery_android_galleryItemBackground, 0);
        array.recycle();*/
    }


    // returns the number of images
    public int getCount() {
        return images.size();
    }

    // returns the ID of an item
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {

        // create a ImageView programmatically
        ImageView imageView = new ImageView(context);
        Picasso.with(context)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + images.get(position))
                .fit()
                .placeholder(R.drawable.no_image_found)
                .into(imageView);
        //imageView.setImageResource(images[position]); // set image in ImageView*/
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 300)); // set ImageView param
        return imageView;
    }
}
