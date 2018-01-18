package com.gajananmotors.shopfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomGalleryAdapter;

public class GallaryActivity extends AppCompatActivity {


    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    // array of images
    int[] images = {R.drawable.background_splashscreen, R.drawable.advertise, R.drawable.background_splashscreen, R.drawable.advertise};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        simpleGallery = (Gallery) findViewById(R.id.simpleGallery);
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView);
        customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), images);
        simpleGallery.setAdapter(customGalleryAdapter);
        simpleGallery.setSpacing(6);
        selectedImageView.setImageResource(images[0]);
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedImageView.setImageResource(images[position]);

            }
        });

    }
}
