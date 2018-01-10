package com.gajananmotors.shopfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForSubCategory;
import com.gajananmotors.shopfinder.common.SlideAnimationUtil;
import com.gajananmotors.shopfinder.helper.CircleImageView;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class SubCategory extends AppCompatActivity {

    private TextView tvHospital;
    private CircleImageView imageView;
    public static String[] nameList = {
            "Public Hospitals",
            "Dental Hospitals",
            "Mental Hospitals",
            "Public Hospitals",
    };
    public static int[] imglist = {
            R.drawable.mobile_shop,
            R.drawable.mobile_shop,
            R.drawable.mobile_shop,
            R.drawable.mobile_shop,};

    private RecyclerView recycler_view;
    private LinearLayoutManager mLayoutManager;
    private CustomAdapterForSubCategory customAdapterForSubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        setContentView(R.layout.activity_sub_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recycler_view = findViewById(R.id.recycler_view_subcategory);
        customAdapterForSubCategory = new CustomAdapterForSubCategory(this, nameList, imglist);
        recycler_view.setLayoutManager(mLayoutManager);

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(customAdapterForSubCategory);
        alphaAdapter.setDuration(2000);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        recycler_view.setAdapter(alphaAdapter);

       /* tvHospital=findViewById(R.id.tvHospital);
        imageView=findViewById(R.id.imgHospital);

        tvHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategory.this,ItemDetailsActivity.class));
               // Toast.makeText(SubCategory.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }); imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategory.this,ItemDetailsActivity.class));
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
