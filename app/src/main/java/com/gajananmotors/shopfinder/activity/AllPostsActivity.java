package com.gajananmotors.shopfinder.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.ShopsArrayListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllPostsActivity extends AppCompatActivity {

    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    ShopsListAdpater adapter;
    private RecyclerView recyclerView;
    private LinearLayout viewPostLayout;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private SharedPreferences sharedPreferences;
    private boolean b=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPostLayout = findViewById(R.id.viewPostLayout);
        recyclerView = findViewById(R.id.recyclerview);
        sharedPreferences=getSharedPreferences(Constant.MyPREFERENCES,MODE_PRIVATE);
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        Call<ShopsArrayListModel> call = restInterface.getShoplist(sharedPreferences.getInt(Constant.OWNER_ID,0));
        call.enqueue(new Callback<ShopsArrayListModel>() {
            @Override
            public void onResponse(Call<ShopsArrayListModel> call, Response<ShopsArrayListModel> response) {
                if (response.isSuccessful()) {
                    ShopsArrayListModel list = response.body();
                    shops_list = list.getShopList();
                    setAdapter(true);
                }
            }

            @Override
            public void onFailure(Call<ShopsArrayListModel> call, Throwable t) {

            }



        });


    }

    private void setAdapter(boolean b) {

        adapter = new ShopsListAdpater(this, viewPostLayout, shops_list,b);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        finish();

    }
}
