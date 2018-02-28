package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.model.ShopsArrayListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchrecyclerView;
    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    private ProgressBar search_list_progressbar;
    private TextView txtemptylistsearch;
    private Toolbar toolbar;
    private String search_keyword;
    private String name = "";
    private SearchView searchView;
    private  MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
        searchrecyclerView = findViewById(R.id.searchrecyclerview);
        search_list_progressbar = findViewById(R.id.search_list_progressbar);
        txtemptylistsearch = findViewById(R.id.txtemptylistsearch);
        Intent intent = getIntent();
        search_keyword = intent.getStringExtra("search_keyword");
        name = intent.getStringExtra("owner");
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<ShopsArrayListModel> call = restInterface.getSearchByKeyword(search_keyword);
        search_list_progressbar.setVisibility(View.VISIBLE);
        search_list_progressbar.setIndeterminate(true);
        search_list_progressbar.setProgress(500);
        call.enqueue(new Callback<ShopsArrayListModel>() {
            @Override
            public void onResponse(Call<ShopsArrayListModel> call, Response<ShopsArrayListModel> response) {
                if (response.isSuccessful()) {
                    ShopsArrayListModel list = response.body();
                    ArrayList<ShopsListModel> shopsListModels = list.getShopList();
                    Log.d("shopsListModels","shopsListModels"+shopsListModels.toString());
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
        item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
               /* ArrayList<ShopsListModel> suggest_list = new ArrayList<>();
                for (ShopsListModel s : shops_list) {
                    if (s.getShop_name().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getArea().toLowerCase().contains(newText) || s.getSub_category_name().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText) || s.getState().toLowerCase().contains(newText) || s.getCountry().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getShop_timing().toLowerCase().contains(newText) || s.getWebsite().toLowerCase().contains(newText))

                        suggest_list.add(s);
                }
                adapter.setFilter(suggest_list);*/
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setAdapter(String name) {
        if (shops_list.size() != 0) {

            adapter = new ShopsListAdpater(this, shops_list, name);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            searchrecyclerView.setLayoutManager(mLayoutManager);
            adapter.notifyDataSetChanged();
            searchrecyclerView.setAdapter(adapter);
            search_list_progressbar.setVisibility(View.GONE);
            item.setVisible(true);
        } else {
            search_list_progressbar.setVisibility(View.GONE);
            txtemptylistsearch.setText("No shops found!");
            item.setVisible(false);
        }
    }
}
