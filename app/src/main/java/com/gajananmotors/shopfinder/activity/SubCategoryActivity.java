package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForSubCategoryAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;

import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubCategoryActivity extends AppCompatActivity {
    private TextView tvHospital;
    private CircleImageView imageView;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private int int_cat_id;
    private ArrayList<SubCategoryModel> sub_category_list = new ArrayList<>();
    private ArrayList<String> subCategoryNames = new ArrayList<>();
    private ArrayList<String> subCategoryImages = new ArrayList<>();
    private ArrayList<Integer> subCatId = new ArrayList<>();
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
    private CustomAdapterForSubCategoryAdapter customAdapterForSubCategoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        setContentView(R.layout.activity_sub_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int_cat_id = intent.getIntExtra("CategoryId", 0);
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        Call<SubCategoryListModel> call = restInterface.getSubCategoryList(int_cat_id);
        call.enqueue(new Callback<SubCategoryListModel>() {
            @Override
            public void onResponse(Call<SubCategoryListModel> call, Response<SubCategoryListModel> response) {
                if (response.isSuccessful()) {
                    SubCategoryListModel list = response.body();
                    sub_category_list = list.getSubcategory();
                    for (SubCategoryModel model : sub_category_list) {
                        subCategoryNames.add(model.getName());
                        subCategoryImages.add(model.getImage());
                        subCatId.add(model.getSub_category_id());

                    }
                    setAdapetr(int_cat_id,subCatId);
                }
            }

            @Override
            public void onFailure(Call<SubCategoryListModel> call, Throwable t) {
            }
        });
        // mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //  mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mLayoutManager = new GridLayoutManager(this, 2);
        recycler_view = findViewById(R.id.recycler_view_subcategory);
        recycler_view.setLayoutManager(mLayoutManager);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(customAdapterForSubCategoryAdapter);
        alphaAdapter.setDuration(2000);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
       /* customAdapterForSubCategoryAdapter = new CustomAdapterForSubCategoryAdapter(this, nameList, imglist);
        recycler_view.setAdapter(alphaAdapter);*/
       /* tvHospital=findViewById(R.id.tvHospital);
        imageView=findViewById(R.id.imgHospital);
        tvHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryModel.this,ItemDetailsActivity.class));
               // Toast.makeText(SubCategoryModel.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }); imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryModel.this,ItemDetailsActivity.class));
            }
        });*/
    }
    public void setAdapetr(int int_cat_id, ArrayList<Integer> subCatId) {
        Log.d("CustomAdapter", "set Adapter method called");
        customAdapterForSubCategoryAdapter = new CustomAdapterForSubCategoryAdapter(this, subCategoryNames, subCategoryImages,this.int_cat_id, this.subCatId);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(customAdapterForSubCategoryAdapter);
        recycler_view.setAdapter(alphaAdapter);
    }
}
