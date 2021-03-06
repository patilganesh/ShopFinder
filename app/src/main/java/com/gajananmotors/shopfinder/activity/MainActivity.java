package com.gajananmotors.shopfinder.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForVerticalGridViewAdapter;
import com.gajananmotors.shopfinder.adapter.SectionRecyclerViewAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.helper.HomeItems;
import com.gajananmotors.shopfinder.helper.RecyclerViewType;
import com.gajananmotors.shopfinder.model.CategoryListModel;
import com.gajananmotors.shopfinder.model.CategoryModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
import static com.gajananmotors.shopfinder.helper.Config.hasPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int PERIOD = 2000;
    private RecyclerView recycler_view_vertical, recyclerView;
    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private static int RESPONSE_CODE = 1;
    private ArrayList<CategoryModel> category_Model_list = new ArrayList<>();
    private ArrayList<SubCategoryModel> sub_category_list = new ArrayList<>();
    private ArrayList<ArrayList<SubCategoryModel>> indi_sub_category_list = new ArrayList<ArrayList<SubCategoryModel>>();
    private Retrofit retrofit;
    private RestInterface restInterface;
    private CustomAdapterForVerticalGridViewAdapter gridAdapter;
    private Toolbar toolbar;
    private ProgressBar category_progressbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private String name = "user";
    private int index = 0;
    private SharedPreferences sharedpreferences;
    private String refreshedToken = "";
    private boolean result = false;
    public static Activity activityMain;
    private com.arlib.floatingsearchview.FloatingSearchView searchView;
    private RecyclerViewType recyclerViewType;
    android.support.design.widget.CoordinatorLayout coordinate_layout;
    private ImageView nearBy;
    private ImageView ivSearch;
    private String search_keyword = "";
    private long lastPressedTime;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activityMain = MainActivity.this;
        drawer = findViewById(R.id.drawer_layout);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        category_progressbar = findViewById(R.id.category_progressbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        coordinate_layout = findViewById(R.id.coordinate_layout);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.e("Refreshed token:", refreshedToken);
        Constant.device_token = refreshedToken;
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constant.DEVICE_TOKEN, refreshedToken);
        editor.apply();
        nearBy = findViewById(R.id.ivNearby);
        ivSearch = findViewById(R.id.ivSearch);
        nearBy.setOnClickListener(this);
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        init();
        searchView = findViewById(R.id.floating_search_view);
        searchView.clearSearchFocus();
        searchView.setOnFocusChangeListener(new com.arlib.floatingsearchview.FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                toolbar.setVisibility(View.GONE);

            }

            @Override
            public void onFocusCleared() {
                toolbar.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.GONE);
                nearBy.setVisibility(View.GONE);
                searchView.clearQuery();
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                // Toast.makeText(MainActivity.this, "Text:"+currentQuery, Toast.LENGTH_SHORT).show();

                getSearchService(currentQuery);


            }
        });

        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        recyclerViewType = RecyclerViewType.GRID;
        String img = sharedpreferences.getString(Constant.OWNER_PROFILE, "");
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        fab = findViewById(R.id.fab);
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
        checkConnection();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        searchView.attachNavigationDrawerToMenuButton(drawer);
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {


                search_keyword = newQuery;
                Log.d("search_keyword", "newQuery" + newQuery);
                nearBy.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchService(search_keyword);
            }
        });


        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_nearby:
                        if (!isNetworkAvailable(getApplicationContext())) {
                            displayPromptForEnablingData(MainActivity.this);
                        } else {

                            if (!search_keyword.isEmpty()) {

                                Log.d("search_keywordOnCLick", "search_keyword" + search_keyword);
                                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                intent.putExtra("search_keyword", search_keyword);
                                intent.putExtra("owner", "search");
                                startActivity(intent);

                            } else {

                                Toast.makeText(MainActivity.this, "Please Type something", Toast.LENGTH_SHORT).show();

                            }
                        }
                        break;
                }
            }
        });
    }

    private void init() {
        mAdView = findViewById(R.id.adView);
      /*  mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_home_footer));
*/
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(getString(R.string.string_addtest_device))
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    public void getSearchService(String search_keyword) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("search_keyword", search_keyword);
        intent.putExtra("owner", "search");
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_vertical);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        populateRecyclerView();
    }

    //populate recycler view
    private void populateRecyclerView() {
        ArrayList<HomeItems> sectionModelArrayList = new ArrayList<>();
        //for loop for sections
        for (int i = 0; i < category_Model_list.size(); i++) {
            //add the section and items to array list
            sectionModelArrayList.add(new HomeItems(category_Model_list.get(i).getCategory_id(), category_Model_list.get(i).getName(), indi_sub_category_list.get(i)));
        }
        SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(this, recyclerViewType, sectionModelArrayList);
        recyclerView.setAdapter(adapter);
        category_progressbar.setVisibility(View.GONE);
    }

    public void getCategory() {
        Call<CategoryListModel> call = restInterface.getCategoryList();
        category_progressbar.setVisibility(View.VISIBLE);
        category_progressbar.setIndeterminate(true);
        category_progressbar.setProgress(500);
        call.enqueue(new Callback<CategoryListModel>() {
            @Override
            public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                if (response.isSuccessful()) {

                    CategoryListModel categoryListModel = response.body();
                    category_Model_list = categoryListModel.getCategories();
                    //   setadapter(categoryNames, categoryImages, categoryId, name);
                    result = true;
                }
                if (result)
                    getSub(category_Model_list.get(index).getCategory_id());
            }

            @Override
            public void onFailure(Call<CategoryListModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSub(final int int_cat_id) {

        Call<SubCategoryListModel> call = restInterface.getSubCategoryList(int_cat_id);

        call.enqueue(new Callback<SubCategoryListModel>() {
            @Override
            public void onResponse(Call<SubCategoryListModel> call, Response<SubCategoryListModel> response) {
                if (response.isSuccessful()) {
                    SubCategoryListModel list = response.body();
                    sub_category_list = list.getSubcategory();
                    indi_sub_category_list.add(sub_category_list);
                    index++;
                    if (index < category_Model_list.size()) {
                        getSub(category_Model_list.get(index).getCategory_id());
                    }
                }
                if (index == category_Model_list.size()) {

                    //   Log.i("size", "Size: "+indi_sub_category_list.size());
                    setUpRecyclerView();

                }
            }

            @Override
            public void onFailure(Call<SubCategoryListModel> call, Throwable t) {
                // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Index Error:" + index, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkConnection() {
        final ConnectionDetector detector = new ConnectionDetector(MainActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        if (!detector.isConnectingToInternet()) {
            alertDialog.setMessage("Network not available!");
            alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (detector.isConnectingToInternet()) {
                        dialog.dismiss();
                        getCategory();
                    } else {
                        checkConnection();
                    }
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else {
            getCategory();
        }
    }

    public void setadapter(ArrayList<String> arrayList_name, ArrayList<String> arrayList_image, ArrayList<Integer> arrayList_id, String name) {
        gridAdapter = new CustomAdapterForVerticalGridViewAdapter(this, arrayList_name, arrayList_image, arrayList_id, name);
        // recycler_view_vertical.setAdapter(gridAdapter);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getDownTime() - lastPressedTime < PERIOD) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            finish();
        } else {
            Snackbar.make(coordinate_layout, "Are you Sure wants to exit!", Snackbar.LENGTH_SHORT).setAction("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    finish();
                }
            }).show();
            lastPressedTime = event.getEventTime();
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearQuery();
        navigationView = findViewById(R.id.nav_view);
        if (mAdView != null) {
            mAdView.resume();
        }
        if (navigationView != null) {
            if (!sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
                navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_addpost).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_allposts).setVisible(true);
                fab.setVisibility(View.GONE);
            } else {
                navigationView.removeHeaderView(navigationView.getHeaderView(0));
                View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
                TextView tvOwner_Name = headerView.findViewById(R.id.tvOwner_Name);
                TextView tvOwner_Email = headerView.findViewById(R.id.tvOwner_Email);
                tvOwner_Name.setText("User Name");
                tvOwner_Email.setText("User Email_id");

            }
            navigationView.setNavigationItemSelectedListener(MainActivity.this);
            String name = sharedpreferences.getString(Constant.OWNER_NAME, "");
            if (!TextUtils.isEmpty(name)) {
                navigationView.removeHeaderView(navigationView.getHeaderView(0));
                View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
                TextView tvOwner_Name = headerView.findViewById(R.id.tvOwner_Name);
                TextView tvOwner_Email = headerView.findViewById(R.id.tvOwner_Email);
                CircleImageView user_profile = headerView.findViewById(R.id.imgProfile);
                tvOwner_Name.setText(sharedpreferences.getString(Constant.OWNER_NAME, ""));
                tvOwner_Email.setText(sharedpreferences.getString(Constant.OWNWER_EMAIL, ""));
                Picasso.with(MainActivity.this)
                        .load(sharedpreferences.getString(Constant.OWNER_PROFILE, ""))
                        .fit()
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .into(user_profile);
            }
        }
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

        } else if (id == R.id.nav_addpost) {
            if (sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, AddPostActivity.class));
            }
        } else if (id == R.id.nav_allposts) {
            Intent intent = new Intent(MainActivity.this, AllPostsActivity.class);
            intent.putExtra("owner", "owner");
            startActivity(intent);
            //   startActivity(new Intent(MainActivity.this, AllPostsActivity.class));
        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
           /* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store?hl=en";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));*/
        } else if (id == R.id.nav_logout) {

            sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            finish();
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
        switch (v.getId()) {
            case R.id.ivNearby:
                if (!isNetworkAvailable(getApplicationContext())) {
                    displayPromptForEnablingData(this);
                } else {
                    Log.d("search_keywordOnCLick", "search_keyword" + search_keyword);
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("search_keyword", search_keyword);
                    intent.putExtra("owner", "search");
                    startActivity(intent);
                }
        }
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
