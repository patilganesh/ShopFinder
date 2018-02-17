package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomGalleryAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GallaryActivity extends AppCompatActivity {

    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    TextView text;
    // array of images
    int shop_id;
    ArrayList<String> images = new ArrayList<>();
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String shop_pic = i.getExtras().getString("shopCoverphoto");

        simpleGallery = findViewById(R.id.simpleGallery);
        text =  findViewById(R.id.tvText);
        shop_id = i.getIntExtra("shop_id", 0);
        selectedImageView = findViewById(R.id.selectedImageView);
        images.clear();
        images = i.getExtras().getStringArrayList("images");
        if(images.isEmpty()){
            text.setVisibility(View.VISIBLE);
            text.setText("No shop images found ");
        }
        Log.d("listsize", String.valueOf(images.size()));

        customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), images, shop_id);
        customGalleryAdapter.notifyDataSetChanged();
        simpleGallery.setAdapter(customGalleryAdapter);
        simpleGallery.setSpacing(6);
        ImageView imageView = new ImageView(GallaryActivity.this);
        Picasso.with(GallaryActivity.this)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + shop_pic)
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(selectedImageView);
        // selectedImageView.setImageResource(images.get(0));

        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = new ImageView(GallaryActivity.this);
                Picasso.with(GallaryActivity.this)
                        .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + images.get(position))
                        .fit()
                        .placeholder(R.drawable.background_splashscreen)
                        .into(selectedImageView);

                //  selectedImageView.setImageResource(images.get(position));
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
        Log.d("listsize", String.valueOf(images.size()));


    }
}
