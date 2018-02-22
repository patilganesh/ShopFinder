package com.gajananmotors.shopfinder.activity;
import android.content.Intent;
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
public class ItemDetailsActivity extends AppCompatActivity {

    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private Toolbar toolbar;
    private SearchView searchView;
    private int int_cat_id,int_sub_cat_id;
    private String name;
    private ProgressBar item_details_progressbar;
    private TextView txtemptylist;
    //private com.arlib.floatingsearchview.FloatingSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
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
        item_details_progressbar = findViewById(R.id.item_details_progressbar);
        recyclerView = findViewById(R.id.recyclerview);
        txtemptylist = findViewById(R.id.txtemptylist);
      /* *//* searchView = findViewById(R.id.floating_search_view);
        searchView.clearSearchFocus(*//*);*/
        Intent intent = getIntent();
        int_cat_id = intent.getIntExtra("CategoryId", 0);
        int_sub_cat_id = intent.getIntExtra("Sub_CategoryId", 0);
        name = intent.getStringExtra("owner");
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        Call<ShopsArrayListModel> call = restInterface.getShoplistbyCategory(int_cat_id,int_sub_cat_id);
        shops_list.clear();
        item_details_progressbar.setVisibility(View.VISIBLE);
        item_details_progressbar.setIndeterminate(true);
        item_details_progressbar.setProgress(500);
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
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        if (!shops_list.isEmpty()) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    newText = newText.toLowerCase();
                    ArrayList<ShopsListModel> suggest_list = new ArrayList<>();
                    for (ShopsListModel s : shops_list) {
                        if (s.getShop_name().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getArea().toLowerCase().contains(newText) || s.getSub_category_name().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText) || s.getState().toLowerCase().contains(newText) || s.getCountry().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getShop_timing().toLowerCase().contains(newText) || s.getWebsite().toLowerCase().contains(newText))

                            suggest_list.add(s);
                    }
                    adapter.setFilter(suggest_list);
                    return true;
                }
            });
        }
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
            item_details_progressbar.setVisibility(View.GONE);
        } else {
            item_details_progressbar.setVisibility(View.GONE);
            txtemptylist.setText("No shops found!");
        }
    }
}
