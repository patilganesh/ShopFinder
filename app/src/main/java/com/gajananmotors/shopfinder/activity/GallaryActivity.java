package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomGalleryAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GallaryActivity extends AppCompatActivity {

    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    private ProgressBar gallery_progressbar;
    // array of images
    int shop_id;
 ArrayList<String>images=new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gallery_progressbar = findViewById(R.id.gallery_progressbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent i = getIntent();
        String img=i.getExtras().getString("image");
        String shop_pic=i.getExtras().getString("shopCoverphoto");
        simpleGallery = (Gallery) findViewById(R.id.simpleGallery);
        shop_id=i.getIntExtra("shop_id",0);
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView);
        images = i.getExtras().getStringArrayList("images");
        customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), images, shop_id);
        simpleGallery.setAdapter(customGalleryAdapter);
        simpleGallery.setSpacing(6);
        ImageView imageView = new ImageView(GallaryActivity.this);
        gallery_progressbar.setVisibility(View.VISIBLE);
        gallery_progressbar.setIndeterminate(true);
        gallery_progressbar.setProgress(500);
        Picasso.with(GallaryActivity.this)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + shop_pic)
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(selectedImageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //Success image already loaded into the view
                                gallery_progressbar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                //Error placeholder image already loaded into the view, do further handling of this situation here
                            }
                        }
                );
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = new ImageView(GallaryActivity.this);
                Picasso.with(GallaryActivity.this)
                        .load("http://findashop.in/images/shop_profile/"+shop_id+"/"+images.get(position))
                        .fit()
                        .placeholder(R.drawable.background_splashscreen)
                        .into(selectedImageView);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        images.clear();
        finish();
    }
}
