package com.gajananmotors.shopfinder.activity;

/**
 * Created by asus on 24-Nov-17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForCategory;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.model.Category;
import com.gajananmotors.shopfinder.model.CategoryList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, View.OnClickListener {
    RecyclerView recycleView,recycler_view_vertical;
    /*
    public static String[] nameList = {
            "Special Offers",
            "Hospitals",
            "Medicals",
            "Cloth Shops",
            "Mobile Shop",
            "Computers",
            "Shoes",
            "Hotels",
            "Pizza",
            "Tours & Travels",
            "Transports",
            "Educational",
            "Business & Jobs",
            "Home Products",
            "Construction",
            "Finance",
    };

    public static int[] imgs = {

            R.drawable.specialoffers,
            R.drawable.hospital,
            R.drawable.medical,
            R.drawable.clothshop,
            R.drawable.mobile_shop,
            R.drawable.computers,
            R.drawable.hotel,
            R.drawable.hospital,
            R.drawable.specialoffers,
            R.drawable.hotel,
            R.drawable.hospital,
            R.drawable.clothshop,
            R.drawable.hospital,
            R.drawable.medical,
            R.drawable.hospital,
            R.drawable.mobile_shop};
*/
    private CustomAdapterForCategory adapter;
    private SearchView editsearch;
    private ImageView nearby;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager_vertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

            }
        });
        recycleView = findViewById(R.id.recycler_view);
        recycler_view_vertical = findViewById(R.id.recycler_view_vertical);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
       // mLayoutManager_vertical = new LinearLayoutManager(getApplicationContext());
        mLayoutManager_vertical = new GridLayoutManager(this, 2);
        mLayoutManager_vertical.setOrientation(LinearLayout.VERTICAL);
      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycleView.getContext(),
                mLayoutManager.getOrientation());
        recycleView.addItemDecoration(dividerItemDecoration);*/
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl(RestInterface.BASE_URL)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
        RestInterface restInterface=retrofit.create(RestInterface.class);
        Call<CategoryList> call=restInterface.getCategoryList();
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                CategoryList list=response.body();
                ArrayList<Category>cat_list=list.getCategories();
                String nameList[]=new String[cat_list.size()];
                String images[]=new String[cat_list.size()];
                for(int i=0;i<cat_list.size();i++)
                {
                    nameList[i]=cat_list.get(i).getName();
                    images[i]=cat_list.get(i).getImages();
                }
                if(response.isSuccessful())
                {
                    Log.i("NameList", "onResponse: "+nameList.length);

                    adapter = new CustomAdapterForCategory(MainActivity.this, nameList, images);
                    recycleView.setLayoutManager(mLayoutManager);
                    recycleView.setItemAnimator(new DefaultItemAnimator());
                    recycleView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       /* adapter = new CustomAdapterForCategory(this, nameList, images);
        recycler_view_vertical.setLayoutManager(mLayoutManager_vertical);
        recycler_view_vertical.setItemAnimator(new DefaultItemAnimator());
        recycler_view_vertical.setAdapter(adapter);
*/
        editsearch =  findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        nearby = findViewById(R.id.nearby);
        nearby.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar listview_map_activity_data clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_change_city:
                return true;
           /* case R.id.action_nearby:
                Intent i = new Intent(getApplicationContext(), com.gajananmotors.shopfinder.activity.MapsActivity.class);
                startActivity(i);
                return true;*/
        }


        return super.onOptionsItemSelected(item);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view listview_map_activity_data clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        } else if (id == R.id.nav_aboutus) {

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store?hl=en";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby:
                Intent i = new Intent(getApplicationContext(), com.gajananmotors.shopfinder.activity.MapsActivity.class);
                startActivity(i);
                return;
        }
    }
}
