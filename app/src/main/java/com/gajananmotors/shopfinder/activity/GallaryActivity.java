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
    static TextView text;
    // array of images
    private int shop_id;
    private int position;
    ArrayList<String> images = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView recycleView;
    private LinearLayoutManager mLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String shop_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text =  findViewById(R.id.tvText);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.black_dark));
        }
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recycleView = findViewById(R.id.gallary_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        Intent i = getIntent();
        String name=i.getStringExtra("owner");
        images.clear();
        if(name.equalsIgnoreCase("owner")) {
            position=i.getIntExtra("position",0);
            shop_pic=AllPostsActivity.shops_list.get(position).getShop_pic();
            if(AllPostsActivity.shops_list.get(position).getImage1()!=null) {
                images.add(AllPostsActivity.shops_list.get(position).getImage1());
            }
            if(AllPostsActivity.shops_list.get(position).getImage2()!=null) {
                images.add(AllPostsActivity.shops_list.get(position).getImage2());
            }
            if(AllPostsActivity.shops_list.get(position).getImage3()!=null) {
                images.add(AllPostsActivity.shops_list.get(position).getImage3());
            }
            if(AllPostsActivity.shops_list.get(position).getImage4()!=null) {
                images.add(AllPostsActivity.shops_list.get(position).getImage4());
            }
            if(AllPostsActivity.shops_list.get(position).getImage5()!=null) {
                images.add(AllPostsActivity.shops_list.get(position).getImage5());
            }
            if(AllPostsActivity.shops_list.get(position).getImage6()!=null)
            images.add(AllPostsActivity.shops_list.get(position).getImage6());
        }
        else
        {
            images = i.getExtras().getStringArrayList("images");
            shop_pic=i.getStringExtra("shopCoverphoto");
        }

        if(images.isEmpty()||images.size()==0){
            text.setVisibility(View.VISIBLE);
            text.setText("No shop images found ");
        }
        shop_id = i.getIntExtra("shop_id", 0);
        selectedImageView = findViewById(R.id.selectedImageView);
        customGalleryAdapter = new CustomAdapterForGallary(this, images, shop_id, selectedImageView);
        customGalleryAdapter.notifyDataSetChanged();
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setAdapter(customGalleryAdapter);
        ImageView imageView = new ImageView(GallaryActivity.this);
        Picasso.with(GallaryActivity.this)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + shop_pic)
                .fit()
                .placeholder(R.drawable.no_image_found)
                .into(selectedImageView);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        images.clear();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
