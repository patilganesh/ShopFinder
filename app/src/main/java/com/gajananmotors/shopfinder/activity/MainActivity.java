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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.gajananmotors.shopfinder.adapter.CustomAdapter;
import com.gajananmotors.shopfinder.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GridView gridview;
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
            "Constructions",
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        gridview = findViewById(R.id.customgrid);
        gridview.setAdapter(new CustomAdapter(this, nameList, imgs));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            case R.id.action_search:
                return true;
            case R.id.action_change_city:
                return true;
            case R.id.action_nearby:
                Intent i = new Intent(getApplicationContext(), com.gajananmotors.shopfinder.activity.MapsActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view listview_map_activity_data clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store?hl=en";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
