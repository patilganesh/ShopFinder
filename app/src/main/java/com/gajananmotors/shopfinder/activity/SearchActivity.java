package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.model.GoogleShopList;
import com.gajananmotors.shopfinder.model.ShopsArrayListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.gajananmotors.shopfinder.utility.DataParser;
import com.gajananmotors.shopfinder.utility.DownloadUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar nearby_search_list_progressbar;
    private TextView txtemptylistnearbysearch;
    Button google_search, main_search;
    private RecyclerView searchrecyclerView;
    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    private ProgressBar search_list_progressbar;
    private Toolbar toolbar;
    private String search_keyword;
    private String name = "";
    private ArrayList<GoogleShopList> google_shops_list = new ArrayList<>();
    private RecyclerView googlesearchrecyclerview;
    private LinearLayoutManager mLayoutManager;
    public String placeName = "";
    public String vicinity = "";

    private String opening_hours = "", icon = "";
    private String shopLong = "", shopLat = "";
    private View view_main_search, view_google_search;

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
        searchrecyclerView = findViewById(R.id.recyclerview__search);
        searchrecyclerView.setVisibility(View.VISIBLE);
        googlesearchrecyclerview = findViewById(R.id.googlerecyclerview_search);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        search_list_progressbar = findViewById(R.id.list_progressbar_search);
        main_search = findViewById(R.id.btnMain_search);
        google_search = findViewById(R.id.btnGoogle_search);
        view_main_search = findViewById(R.id.viewMain_search);
        view_google_search = findViewById(R.id.viewGoogle_search);
        txtemptylistnearbysearch = findViewById(R.id.txtemptylist_search);
        nearby_search_list_progressbar = findViewById(R.id.list_progressbar_search);
        main_search.setOnClickListener(this);
        google_search.setOnClickListener(this);

        Intent intent = getIntent();
        search_keyword = intent.getStringExtra("search_keyword");
        name = intent.getStringExtra("owner");
        getplacesResults();
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
            txtemptylistnearbysearch.setText("");
            adapter.notifyDataSetChanged();
            searchrecyclerView.setAdapter(adapter);
            search_list_progressbar.setVisibility(View.GONE);

        } else {
            search_list_progressbar.setVisibility(View.GONE);
            txtemptylistnearbysearch.setText("No shops found!");

        }
    }

    public void clear() {
        if (!shops_list.isEmpty()) {
            final int size = shops_list.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    shops_list.remove(0);
                }

                adapter.notifyItemRangeRemoved(0, size);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMain_search:
                if (isNetworkAvailable(getApplicationContext())) {


                    google_search.setTextColor(getResources().getColor(R.color.Black));
                    main_search.setTextColor(getResources().getColor(R.color.colorPrimary));
                    searchrecyclerView.setVisibility(View.VISIBLE);
                    view_main_search.setVisibility(View.VISIBLE);
                    view_google_search.setVisibility(View.INVISIBLE);
                    googlesearchrecyclerview.setVisibility(View.GONE);
                    clear();
                    getplacesResults();
                }
                break;
            case R.id.btnGoogle_search:
                if (isNetworkAvailable(getApplicationContext())) {
                    google_search.setTextColor(getResources().getColor(R.color.colorPrimary));
                    main_search.setTextColor(getResources().getColor(R.color.Black));
                    searchrecyclerView.setVisibility(View.GONE);
                    view_google_search.setVisibility(View.VISIBLE);
                    view_main_search.setVisibility(View.INVISIBLE);
                    googlesearchrecyclerview.setVisibility(View.VISIBLE);

                    getplaces(search_keyword);
                }
                break;
        }

    }

    private void getplacesResults() {
        try {


            Retrofit retrofit = APIClient.getClient();
            RestInterface restInterface = retrofit.create(RestInterface.class);
            Call<ShopsArrayListModel> call = restInterface.getSearchByKeyword(search_keyword);
            nearby_search_list_progressbar.setVisibility(View.VISIBLE);
            nearby_search_list_progressbar.setIndeterminate(true);
            nearby_search_list_progressbar.setProgress(500);
            call.enqueue(new Callback<ShopsArrayListModel>() {
                @Override
                public void onResponse(Call<ShopsArrayListModel> call, Response<ShopsArrayListModel> response) {
                    if (response.isSuccessful()) {
                        ShopsArrayListModel list = response.body();
                        ArrayList<ShopsListModel> shopsListModels = list.getShopList();
                        Log.d("shopsListModels", "shopsListModels" + shopsListModels.toString());
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getplaces(String search_keyword) {
        String url = getUrl(search_keyword);
        Object[] DataTransfer = new Object[2];
        //  DataTransfer[0] = mMap;
        DataTransfer[0] = url;

        GetPlacesData getPlacesData = new GetPlacesData();
        getPlacesData.execute(DataTransfer);
        // Toast.makeText(MapsActivity.this, "Nearby Hospitals", Toast.LENGTH_LONG).show();
    }

    private String getUrl(String nearbyPlace) {
        String searchKey = nearbyPlace.trim().replaceAll(" ", "+");
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("&query=" + searchKey);
        googlePlacesUrl.append("&key=" + "AIzaSyAGaiRVEwaNMzwXK5_OnOG_5PsanNumoMI");
        return (googlePlacesUrl.toString());
    }

    class GetPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData = " ";
        String url;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nearby_search_list_progressbar.setVisibility(View.VISIBLE);
            nearby_search_list_progressbar.setIndeterminate(true);
            nearby_search_list_progressbar.setProgress(500);

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                // mMap = (GoogleMap) params[0];
                url = (String) params[0];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = "";
                googlePlacesData = downloadUrl.readUrl(url);

            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.d("GooglePlacesReadTask", "onPostExecute Entered" + result);
                List<HashMap<String, String>> nearbyPlacesList = null;
                DataParser dataParser = new DataParser();
                if (!result.isEmpty()) {
                    nearby_search_list_progressbar.setVisibility(View.GONE);
                    nearbyPlacesList = dataParser.parse(result);
                    Log.d("SearchActivity", "placesList" + nearbyPlacesList.toString());
                    ShowNearbyPlaces(nearbyPlacesList);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (google_shops_list.size() != 0) {
                                txtemptylistnearbysearch.setText("");
                                adapter = new ShopsListAdpater(SearchActivity.this, "GoogleData", google_shops_list);
                                googlesearchrecyclerview.setLayoutManager(mLayoutManager);
                                googlesearchrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                googlesearchrecyclerview.setAdapter(adapter);
                            } else {
                                nearby_search_list_progressbar.setVisibility(View.GONE);
                                txtemptylistnearbysearch.setText("No shops found!");
                            }

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        try {
            double lng = 0, lat = 0;
            //  mMap.clear();
            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                GoogleShopList googleShopList = new GoogleShopList();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                shopLat = googlePlace.get("lat");
                shopLong = googlePlace.get("lng");
                placeName = googlePlace.get("name");
                vicinity = googlePlace.get("formatted_address");
                icon = googlePlace.get("icon");
                opening_hours = googlePlace.get("opening_hours");
                try {
                    if (!placeName.isEmpty()) {

                        googleShopList.setShopName(placeName);
                    }
                    if (!vicinity.isEmpty()) {
                        googleShopList.setShopAddress(vicinity);
                    }
                    if (!shopLat.isEmpty() && !shopLat.equalsIgnoreCase("")) {
                        googleShopList.setShopLat(shopLat);
                    }
                    if (!shopLong.isEmpty() && !shopLong.equalsIgnoreCase("")) {
                        googleShopList.setShopLong(shopLong);
                    }
                    if (!shopLat.isEmpty() && !shopLat.equalsIgnoreCase("")) {
                        lat = Double.parseDouble(shopLat);
                    }
                    if (!shopLong.isEmpty() && !shopLong.equalsIgnoreCase("")) {
                        lng = Double.parseDouble(shopLong);
                    }
                    if (!opening_hours.isEmpty()) {

                        googleShopList.setShopOpeningHours(opening_hours);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                google_shops_list.add(googleShopList);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
