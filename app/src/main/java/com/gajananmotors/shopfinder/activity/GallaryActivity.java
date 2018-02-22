package com.gajananmotors.shopfinder.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForGallary;
import com.gajananmotors.shopfinder.adapter.CustomGalleryAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GallaryActivity extends AppCompatActivity {

    Gallery simpleGallery;
    CustomAdapterForGallary customGalleryAdapter;
    ImageView selectedImageView;
    TextView text;
    // array of images
    int shop_id;
    ArrayList<String> images = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView recycleView;
    private LinearLayoutManager mLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.black_dark));
        }
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recycleView = findViewById(R.id.gallary_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        Intent i = getIntent();
        String shop_pic = i.getExtras().getString("shopCoverphoto");
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

        customGalleryAdapter = new CustomAdapterForGallary(this, images, shop_id, selectedImageView);
        customGalleryAdapter.notifyDataSetChanged();
        recycleView.setLayoutManager(mLayoutManager);
        //  recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(customGalleryAdapter);
        //  simpleGallery.setAdapter(customGalleryAdapter);
        //simpleGallery.setSpacing(6);
        ImageView imageView = new ImageView(GallaryActivity.this);

        Picasso.with(GallaryActivity.this)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + shop_pic)
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(selectedImageView);
        // selectedImageView.setImageResource(images.get(0));
       /* simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });*/
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        images.clear();
        finish();
    }
}
