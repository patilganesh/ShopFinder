package com.gajananmotors.shopfinder.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForVerticalGridView;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.model.ShopsList;

import java.util.ArrayList;

import static com.gajananmotors.shopfinder.helper.Config.hasPermissions;

/*import com.arlib.floatingsearchview.FloatingSearchView;*/

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RecyclerView recycler_view_vertical, recyclerView;
    private ArrayList<ShopsList> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    public static String search_text;
    public android.support.v7.widget.SearchView searchView;
    private ShopsList indivisual_list[] = new ShopsList[6];
    private static int RESPONSE_CODE = 1;
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
            R.drawable.mobile_shop
    };
    private ImageView nearby;
    private LinearLayoutManager mLayoutManager_vertical;
    private CustomAdapterForVerticalGridView gridAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      /*  searchView = (android.support.v7.widget.SearchView) findViewById(R.id.simpleSearchView);*/

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
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
        recycler_view_vertical.setNestedScrollingEnabled(false);
        mLayoutManager_vertical = new GridLayoutManager(this, 3);
        mLayoutManager_vertical.setOrientation(LinearLayout.VERTICAL);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        gridAdapter = new CustomAdapterForVerticalGridView(this, nameList, imglist);
        recycler_view_vertical.setLayoutManager(mLayoutManager_vertical);
        recycler_view_vertical.setItemAnimator(new DefaultItemAnimator());
        recycler_view_vertical.setAdapter(gridAdapter);

       /* nearby = findViewById(R.id.nearby);
        nearby.setOnClickListener(this);*/
        recyclerView = findViewById(R.id.rcv);
        shops_list.add(new ShopsList("Gajana Motors Pvt.Ltd.", "500.00 m", "Vinayak Residencey,near DMart,Baner", "Opens 24 Hours", "http:/www.informedevice.com", "Hotel", "9856237845"));
        shops_list.add(new ShopsList("Aloha Technology Pvt.Ltd.", "400.00 m", "2nd & 3rd Floor, Kumar Crystals, New D P Road, Opposite Converses, Aundh, Baner, Pune", "Opens 9Am-10PM", "http:/www.aloha.com", "IT", "7812345645"));
        shops_list.add(new ShopsList("Xoriant Technology", "200.00 m", "501-502, 5th Floor, Amar Paradigm, Baner Road, Near D-Mart, Baner, Pune", "Opens 9.30Am-6PM", "http:/www.xoriant.com", "IT", "8185868231"));
        shops_list.add(new ShopsList("Veritas Technology", "800.00 m", "East Middlefield Road Mountain View, CA 94043", "Opens 9.30Am-7PM", "http:/www.veritas.com", "Hospital", "9095969314"));
        shops_list.add(new ShopsList("MNM Solutions", "5.00 km", "UNIT no 802, Tower no. 7, SEZ Magarpatta city, Hadapsar, Pune, Maharashtra 411013", "Opens 24 Hours", "http:/www.mnm.com", "Business", "8794156568"));
        shops_list.add(new ShopsList("Infosys Solutions", "100.00 km", "UNIT no 802, Tower no. 7, SEZ Magarpatta city, Hadapsar, Pune, Maharashtra 411013", "Opens 24 Hours", "http:/www.infosys.com", "Business", "7856123245"));
        adapter = new ShopsListAdpater(shops_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(adapter);
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
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    recycler_view_vertical.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                newText = newText.toLowerCase();
                search_text = newText;

                ArrayList<ShopsList> suggest_list = new ArrayList<>();
                for (ShopsList s : shops_list) {
                    if (s.getName().toLowerCase().startsWith(newText) || s.getAddress().toLowerCase().startsWith(newText) || s.getType().toLowerCase().startsWith(newText) || s.getDistance().toLowerCase().startsWith(newText) || s.getTiming().toLowerCase().startsWith(newText) || s.getMobileNo().toLowerCase().startsWith(newText))
                        suggest_list.add(s);
                    else if (s.getName().toLowerCase().endsWith(newText) || s.getAddress().toLowerCase().endsWith(newText) || s.getType().toLowerCase().endsWith(newText) || s.getDistance().toLowerCase().endsWith(newText) || s.getTiming().toLowerCase().endsWith(newText) || s.getMobileNo().toLowerCase().endsWith(newText))
                        suggest_list.add(s);
                    else if (s.getName().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getType().toLowerCase().contains(newText) || s.getDistance().toLowerCase().contains(newText) || s.getTiming().toLowerCase().contains(newText) || s.getMobileNo().toLowerCase().contains(newText))
                        suggest_list.add(s);
                }
                adapter.setFilter(suggest_list);
                return true;
            }
        });
        //    Toast.makeText(this, "On Create Option Menu", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       /* switch (item.getItemId()) {

            case R.id.action_change_city:
                return true;
        }*/
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

        } else if (id == R.id.nav_nearby) {

            startActivity(new Intent(MainActivity.this, MapsActivity.class));

        } else if (id == R.id.nav_addpost) {

            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        } else if (id == R.id.nav_allposts) {

            startActivity(new Intent(MainActivity.this, AllPostsActivity.class));

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESPONSE_CODE && resultCode == RESULT_OK && null != data) {
            Toast.makeText(this, "" + data.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.nearby:
                if (!isNetworkAvailable(getApplicationContext())) {
                    displayPromptForEnablingData(this);
                } else {
                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(i);
                }
        }*/
    }
   /* @Override
    public boolean onQueryTextSubmit(String query) {

        Toast.makeText(this, "On Submit......", Toast.LENGTH_SHORT).show();
        return false;
    }*/
    /*@Override
    public boolean onQueryTextChange(String newText) {
        recycleView.setVisibility(RecyclerView.VISIBLE);
        recycler_view_vertical.setVisibility(RecyclerView.GONE);
        newText = newText.toLowerCase();
        ArrayList<ShopsList> suggest_list = new ArrayList<>();
        for (ShopsList s : shops_list) {
            if (s.getName().toLowerCase().contains(newText)||s.getAddress().toLowerCase().contains(newText)||s.getType().toLowerCase().contains(newText)||s.getDistance().toLowerCase().contains(newText)||s.getTiming().toLowerCase().contains(newText))
                //   Toast.makeText(this, "Text:" + newText, Toast.LENGTH_LONG).show();
                suggest_list.add(s);
        }
        adapter.setFilter(suggest_list);
        return true;
    }*/
}