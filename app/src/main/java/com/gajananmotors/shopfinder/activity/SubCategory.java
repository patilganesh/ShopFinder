package com.gajananmotors.shopfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForSubCategory;
import com.gajananmotors.shopfinder.helper.CircleImageView;

public class SubCategory extends AppCompatActivity {

    private TextView tvHospital;
    private CircleImageView imageView;
    public static String[] nameList = {
            "Public Hospitals",
            "Dental Hospitals",
    };
    public static int[] imglist = {
            R.drawable.mobile_shop,
            R.drawable.mobile_shop,};

    private RecyclerView recycler_view;
    private LinearLayoutManager mLayoutManager;
    private CustomAdapterForSubCategory customAdapterForSubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

        setContentView(R.layout.activity_sub_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler_view = findViewById(R.id.recycler_view_subcategory);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayout.VERTICAL);

        customAdapterForSubCategory = new CustomAdapterForSubCategory(this, nameList, imglist);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setAdapter(customAdapterForSubCategory);

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
}
