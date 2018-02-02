package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomGalleryAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GallaryActivity extends AppCompatActivity {

    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    // array of images
    int shop_id;
 ArrayList<String>images=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        Intent i = getIntent();
        String img=i.getExtras().getString("image");
        simpleGallery = (Gallery) findViewById(R.id.simpleGallery);
        shop_id=i.getIntExtra("shop_id",0);
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView);
        Picasso.with(GallaryActivity.this)
                .load("http://findashop.in/images/shop_profile/"+shop_id+"/"+i.getExtras().getString("shopCoverphoto"))
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(selectedImageView);
        images=i.getExtras().getStringArrayList("images");

        customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(),images ,shop_id);
        simpleGallery.setAdapter(customGalleryAdapter);
        simpleGallery.setSpacing(6);
        ImageView imageView = new ImageView(GallaryActivity.this);
        Picasso.with(GallaryActivity.this)
                .load("http://findashop.in/images/shop_profile/"+shop_id+"/"+images.get(0))
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(selectedImageView);
       // selectedImageView.setImageResource(images.get(0));
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = new ImageView(GallaryActivity.this);
                Picasso.with(GallaryActivity.this)
                        .load("http://findashop.in/images/shop_profile/"+shop_id+"/"+images.get(position))
                        .fit()
                        .placeholder(R.drawable.background_splashscreen)
                        .into(selectedImageView);
              //  selectedImageView.setImageResource(images.get(position));
            }
        });

    }
    @Override
    public void onBackPressed() {
        images.clear();

    }
}
