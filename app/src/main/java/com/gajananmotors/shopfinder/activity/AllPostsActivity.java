package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.ShopsArrayListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllPostsActivity extends AppCompatActivity {

    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private SharedPreferences sharedPreferences;
    private boolean b = true;
    private Toolbar toolbar;
    private SearchView searchView;
    private String name = "";
    private String search_text;
    private ProgressBar allPostProgressBar;
    private TextView txtowneremptylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        allPostProgressBar = findViewById(R.id.allPostProgressBar);
        txtowneremptylist = findViewById(R.id.txtowneremptylist);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent i = getIntent();
        name= i.getStringExtra("owner");

        recyclerView = findViewById(R.id.recyclerview);
        sharedPreferences = getSharedPreferences(Constant.MyPREFERENCES, MODE_PRIVATE);
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        allPostProgressBar.setVisibility(View.VISIBLE);
        allPostProgressBar.setIndeterminate(true);
        allPostProgressBar.setProgress(500);
        Call<ShopsArrayListModel> call = restInterface.getShoplist(sharedPreferences.getInt(Constant.OWNER_ID, 0));
        shops_list.clear();
        call.enqueue(new Callback<ShopsArrayListModel>() {
            @Override
            public void onResponse(Call<ShopsArrayListModel> call, Response<ShopsArrayListModel> response) {
                if (response.isSuccessful()) {

                    ShopsArrayListModel list = response.body();
                    ArrayList<ShopsListModel> shopsListModels = list.getShopList();
                    for (ShopsListModel model : shopsListModels) {
                        if (model.getStatus() == 1) {
                            shops_list.add(model);
                        }
                    }
                    setAdapter(name);
                }
            }

            @Override
            public void onFailure(Call<ShopsArrayListModel> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(search);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                setAdapter("");
//                newText = newText.toLowerCase();
//                search_text = newText;
//                ArrayList<ShopsListModel> suggest_list = new ArrayList<>();
//                for (ShopsListModel s : shops_list) {
//                    if (s.getShop_name().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getArea().toLowerCase().contains(newText) || s.getSub_category_name().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText) || s.getState().toLowerCase().contains(newText) || s.getCountry().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getShop_timing().toLowerCase().contains(newText) || s.getWebsite().toLowerCase().contains(newText))
//
//                        suggest_list.add(s);
//                    if (s.getShop_name().toLowerCase().startsWith(newText) || s.getAddress().toLowerCase().startsWith(newText) || s.getAddress().toLowerCase().startsWith(newText) || s.getCity().toLowerCase().startsWith(newText) || s.getCategory_name().toLowerCase().startsWith(newText) || s.getShop_mob_no().toLowerCase().startsWith(newText))
//                        suggest_list.add(s);
//                    else if (s.getShop_name().toLowerCase().endsWith(newText) || s.getAddress().toLowerCase().endsWith(newText) || s.getAddress().toLowerCase().endsWith(newText) || s.getCity().toLowerCase().endsWith(newText) || s.getCategory_name().toLowerCase().endsWith(newText) || s.getShop_mob_no().toLowerCase().endsWith(newText))
//                        suggest_list.add(s);
//                    else if (s.getShop_name().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText))
//                        suggest_list.add(s);
//                }

                              adapter.getFilter().filter(newText);
               // adapter.setFilter(suggest_list);
               return true;
            }
        });
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void setAdapter(String name) {
        if (shops_list.size() != 0) {
            adapter = new ShopsListAdpater(this, shops_list, name);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            allPostProgressBar.setVisibility(View.GONE);
        } else {
            txtowneremptylist.setText("No shops found!");
            allPostProgressBar.setVisibility(View.GONE);
        }
    }
}