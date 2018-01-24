package com.gajananmotors.shopfinder.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForVerticalGridViewAdapter;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.CategoryModel;
import com.gajananmotors.shopfinder.model.CategoryListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.gajananmotors.shopfinder.helper.Config.hasPermissions;
/*import com.arlib.floatingsearchview.FloatingSearchView;*/
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private RecyclerView recycler_view_vertical, recyclerView;
    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    public static String search_text;
    public android.support.v7.widget.SearchView searchView;
    private ShopsListModel indivisual_list[] = new ShopsListModel[6];
    private static int RESPONSE_CODE = 1;
    private ArrayList<CategoryModel> category_Model_list = new ArrayList<>();
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<String> categoryImages = new ArrayList<>();
    private ArrayList<Integer> categoryId = new ArrayList<>();
    private Retrofit retrofit;
    private RestInterface restInterface;
    private ImageView nearby;
    private LinearLayoutManager mLayoutManager_vertical;
    private CustomAdapterForVerticalGridViewAdapter gridAdapter;
    private Toolbar toolbar;
    private SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinate_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinate_layout = findViewById(R.id.coordinate_layout);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
      /*  searchView = (android.support.v7.widget.SearchView) findViewById(R.id.simpleSearchView);*/
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        recycler_view_vertical = findViewById(R.id.recycler_view_vertical);
        mLayoutManager_vertical = new GridLayoutManager(this, 3);
        mLayoutManager_vertical.setOrientation(LinearLayout.VERTICAL);
        recycler_view_vertical.setNestedScrollingEnabled(false);
        recycler_view_vertical.setItemAnimator(new DefaultItemAnimator());
        recycler_view_vertical.setLayoutManager(mLayoutManager_vertical);
      /*  gridAdapter=new CustomAdapterForVerticalGridViewAdapter(this,nameList,imglist);
        recycler_view_vertical.setAdapter(gridAdapter);*/
        String img = sharedpreferences.getString(Constant.OWNER_PROFILE, "");
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
        if (!sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
            fab.setVisibility(View.GONE);
        }
        Call<CategoryListModel> call = restInterface.getCategoryList();
        call.enqueue(new Callback<CategoryListModel>() {
            @Override
            public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                if (response.isSuccessful()) {
                    CategoryListModel categoryListModel = response.body();
                    category_Model_list = categoryListModel.getCategories();
                    for (CategoryModel model : category_Model_list) {
                        categoryNames.add(model.getName());
                        categoryImages.add(model.getImage());
                        categoryId.add(model.getCategory_id());
                    }
                    setadapter(categoryNames, categoryImages, categoryId);
                }
            }
            @Override
            public void onFailure(Call<CategoryListModel> call, Throwable t) {
                // Toast.makeText(MainActivity.this, "Fail to load categories,check your internet connection!", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(coordinate_layout, "Fail to load categories,check your internet connection!", Snackbar.LENGTH_LONG);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (!sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            fab.setVisibility(View.GONE);

        }
        navigationView.setNavigationItemSelectedListener(this);
        String name = sharedpreferences.getString(Constant.OWNER_NAME, null);
        if (name != null) {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView tvOwner_Name = headerView.findViewById(R.id.tvOwner_Name);
            TextView tvOwner_Email = headerView.findViewById(R.id.tvOwner_Email);
            CircleImageView user_profile = headerView.findViewById(R.id.imgProfile);
            tvOwner_Name.setText(sharedpreferences.getString(Constant.OWNER_NAME, ""));
            tvOwner_Email.setText(sharedpreferences.getString(Constant.OWNWER_EMAIL, ""));
            // String img1 = sharedpreferences.getString(Constant.OWNER_PROFILE, "");
            Picasso.with(MainActivity.this)
                    .load(sharedpreferences.getString(Constant.OWNER_PROFILE, ""))
                    .fit()
                    .into(user_profile);
        }
      /* nearby = findViewById(R.id.nearby);
      nearby.setOnClickListener(this);*/
        // below code is for feature refernce,please dont delete this code.
        /*recyclerView = findViewById(R.id.rcv);
        shops_list.add(new ShopsListModel("Gajana Motors Pvt.Ltd.", "500.00 m", "Vinayak Residencey,near DMart,Baner", "Opens 24 Hours", "http:/www.informedevice.com", "Hotel", "9856237845"));
        shops_list.add(new ShopsListModel("Aloha Technology Pvt.Ltd.", "400.00 m", "2nd & 3rd Floor, Kumar Crystals, New D P Road, Opposite Converses, Aundh, Baner, Pune", "Opens 9Am-10PM", "http:/www.aloha.com", "IT", "7812345645"));
        shops_list.add(new ShopsListModel("Xoriant Technology", "200.00 m", "501-502, 5th Floor, Amar Paradigm, Baner Road, Near D-Mart, Baner, Pune", "Opens 9.30Am-6PM", "http:/www.xoriant.com", "IT", "8185868231"));
        shops_list.add(new ShopsListModel("Veritas Technology", "800.00 m", "East Middlefield Road Mountain View, CA 94043", "Opens 9.30Am-7PM", "http:/www.veritas.com", "Hospital", "9095969314"));
        shops_list.add(new ShopsListModel("MNM Solutions", "5.00 km", "UNIT no 802, Tower no. 7, SEZ Magarpatta city, Hadapsar, Pune, Maharashtra 411013", "Opens 24 Hours", "http:/www.mnm.com", "Business", "8794156568"));
        shops_list.add(new ShopsListModel("Infosys Solutions", "100.00 km", "UNIT no 802, Tower no. 7, SEZ Magarpatta city, Hadapsar, Pune, Maharashtra 411013", "Opens 24 Hours", "http:/www.infosys.com", "Business", "7856123245"));
        adapter = new ShopsListAdpater(shops_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(adapter);*/
    }
    public void setadapter(ArrayList<String> arrayList_name, ArrayList<String> arrayList_image, ArrayList<Integer> arrayList_id) {
        gridAdapter = new CustomAdapterForVerticalGridViewAdapter(this, arrayList_name, arrayList_image, arrayList_id);
        recycler_view_vertical.setAdapter(gridAdapter);
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
                ArrayList<ShopsListModel> suggest_list = new ArrayList<>();
                for (ShopsListModel s : shops_list) {
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
            if (sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, AddPostActivity.class));
            }

        } else if (id == R.id.nav_allposts) {

            startActivity(new Intent(MainActivity.this, AllPostsActivity.class));

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store?hl=en";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
        } else if (id == R.id.nav_logout) {

            sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            String imd = sharedpreferences.getString(Constant.OWNER_PROFILE, "");
            Log.d("name", sharedpreferences.getString(Constant.OWNER_NAME, ""));
            startActivity(new Intent(this, MainActivity.class));
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
        ArrayList<ShopsListModel> suggest_list = new ArrayList<>();
        for (ShopsListModel s : shops_list) {
            if (s.getName().toLowerCase().contains(newText)||s.getAddress().toLowerCase().contains(newText)||s.getType().toLowerCase().contains(newText)||s.getDistance().toLowerCase().contains(newText)||s.getTiming().toLowerCase().contains(newText))
                //   Toast.makeText(this, "Text:" + newText, Toast.LENGTH_LONG).show();
                suggest_list.add(s);
        }
        adapter.setFilter(suggest_list);
        return true;
    }*/
}