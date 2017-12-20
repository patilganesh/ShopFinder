package com.gajananmotors.shopfinder.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.ViewFlipper;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForCategory;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForVerticalGridView;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, View.OnClickListener {

  RecyclerView recycleView, recycler_view_vertical;
    public static String[] nameList = {
            "Offers",
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
    public static int[] imglist = {

            R.drawable.hospital,
            R.drawable.mobile_shop,
            R.drawable.hospital,
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

    private ImageView nearby;
    private LinearLayoutManager mLayoutManager_vertical;
    private CustomAdapterForVerticalGridView gridAdapter;
    private Toolbar toolbar;
    private FloatingSearchView searchView;
    private ViewFlipper mViewFlipper;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewFlipper =  this.findViewById(R.id.view_flipper);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(1000);
        mViewFlipper.startFlipping();
        searchView = findViewById(R.id.floating_search_view);
        searchView.clearSearchFocus();

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                toolbar.setVisibility(View.GONE);
                nearby.setVisibility(View.GONE);
            }

            @Override
            public void onFocusCleared() {
                toolbar.setVisibility(View.VISIBLE);
                nearby.setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recycler_view_vertical = findViewById(R.id.recycler_view_vertical);
        mLayoutManager_vertical = new GridLayoutManager(this, 3);
        mLayoutManager_vertical.setOrientation(LinearLayout.HORIZONTAL);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gridAdapter = new CustomAdapterForVerticalGridView(this, nameList, imglist);
        recycler_view_vertical.setLayoutManager(mLayoutManager_vertical);
        recycler_view_vertical.setItemAnimator(new DefaultItemAnimator());
        recycler_view_vertical.setAdapter(gridAdapter);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
                if (!isNetworkAvailable(getApplicationContext())) {
                    displayPromptForEnablingData(this);
                } else {
                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(i);
                }
        }
    }



}
