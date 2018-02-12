package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.common.SlideAnimationUtil;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.ShopsArrayListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
public class ItemDetailsActivity extends AppCompatActivity{
    private LinearLayout viewLayout;
    private LinearLayout shopDirection;
    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    private RecyclerView recyclerView;
    private LinearLayout viewPostLayout;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private SharedPreferences sharedPreferences;
    private int int_cat_id,int_sub_cat_id;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewLayout = findViewById(R.id.viewLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView = findViewById(R.id.recyclerView);
        viewPostLayout = findViewById(R.id.viewLayout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        int_cat_id = intent.getIntExtra("CategoryId", 0);
        int_sub_cat_id = intent.getIntExtra("Sub_CategoryId", 0);
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        Call<ShopsArrayListModel> call = restInterface.getShoplistbyCategory(int_cat_id,int_sub_cat_id);
        call.enqueue(new Callback<ShopsArrayListModel>() {
            @Override
            public void onResponse(Call<ShopsArrayListModel> call, Response<ShopsArrayListModel> response) {
                if (response.isSuccessful()) {
                    ShopsArrayListModel list = response.body();
                    ArrayList<ShopsListModel>shopsListModels=list.getShopList();
                    for(ShopsListModel model:shopsListModels)
                    {
                        if(model.getStatus()==1)
                        {
                            shops_list.add(model);
                        }
                    }
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Call<ShopsArrayListModel> call, Throwable t) {

            }
        });


    }@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
private void setAdapter() {
        adapter = new ShopsListAdpater(this, viewPostLayout, shops_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}

