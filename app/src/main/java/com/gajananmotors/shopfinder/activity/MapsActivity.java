package com.gajananmotors.shopfinder.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomAdapterForNearByLocAdapter;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.model.GoogleShopList;
import com.gajananmotors.shopfinder.model.ShopsArrayListModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.gajananmotors.shopfinder.utility.DataParser;
import com.gajananmotors.shopfinder.utility.DownloadUrl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingGPS;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private GoogleMap mMap;

    private ArrayList<GoogleShopList> google_shops_list = new ArrayList<>();
    private double latitude;
    private double longitude;
    private int distance = 0;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    RecyclerView searchnearbyrecyclerview, googlesearchnearbyrecyclerview;
    LinearLayoutManager mLayoutManager;
    private ShopsListAdpater adapter;
    private TextView textView;
    private Marker marker;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private String nearbyPlace = "";
    private ArrayList<ShopsListModel> shops_list = new ArrayList<>();
    private ProgressBar nearby_search_list_progressbar;
    private TextView txtemptylistnearbysearch;
    Button googleNearby, mainNearBy;

    public ArrayList<String> nameList;
    public ArrayList<String> addressList;
    public ArrayList<String> openingHoursList;
    public ArrayList<String> iconList;
    public ArrayList<String> latList;
    public ArrayList<String> longList;
    private CustomAdapterForNearByLocAdapter adapter1;
    private int seekvalue = 10;
    String data = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        if (!CheckGooglePlayServices()) {
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }
        textView = findViewById(R.id.txtSeekBar);
        textView.setText(seekvalue + "km");
        mainNearBy = findViewById(R.id.btnMainNearBy);
        googleNearby = findViewById(R.id.btnGoogleNearby);
        mainNearBy.setOnClickListener(this);
        googleNearby.setOnClickListener(this);
        txtemptylistnearbysearch = findViewById(R.id.txtemptylistnearbysearch);
        nearby_search_list_progressbar = findViewById(R.id.nearby_search_list_progressbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        searchnearbyrecyclerview = findViewById(R.id.searchnearbyrecyclerview);
        googlesearchnearbyrecyclerview = findViewById(R.id.googlesearchnearbyrecyclerview);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gps_enabled && !network_enabled) {
            displayPromptForEnablingGPS(this);
        }
        Intent intent = getIntent();
        nearbyPlace = intent.getStringExtra("search_keyword");
        SeekBar seek = findViewById(R.id.seek);
        seek.setOnSeekBarChangeListener(this);

        distance = seekvalue;
      /*  seek.setMin(2);*/
      /*  seek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                textView.setText(value + " km");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                mMap.clear();
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                distance = seekBar.getProgress();
                getSearchList(distance);
            }
        });*/

        mapFragment.getMapAsync(this);

    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Log.d("MapsActivity", "onCreate");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }


    public void getSearchList(int distance) {
        Log.d("mapActivty", "getSearchList Called");

        searchnearbyrecyclerview.setVisibility(View.VISIBLE);
        googlesearchnearbyrecyclerview.setVisibility(View.GONE);
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<ShopsArrayListModel> call = restInterface.getNearByShops(nearbyPlace, latitude, longitude, distance);
        nearby_search_list_progressbar.setVisibility(View.VISIBLE);
        nearby_search_list_progressbar.setIndeterminate(true);
        nearby_search_list_progressbar.setProgress(500);
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


                    ShowNearbyPlaces(shops_list);
                    nearby_search_list_progressbar.setVisibility(View.GONE);
                    setAdapter(shops_list);
                }
            }

            @Override
            public void onFailure(Call<ShopsArrayListModel> call, Throwable t) {

            }
        });
    }

    private void ShowNearbyPlaces(ArrayList<ShopsListModel> nearbyPlacesList) {

        mMap.clear();

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            // HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            try {
                double lat = Double.parseDouble(nearbyPlacesList.get(i).getShop_lat());
                double lng = Double.parseDouble(nearbyPlacesList.get(i).getShop_long());
                String placeName = nearbyPlacesList.get(i).getShop_name();
                String vicinity = nearbyPlacesList.get(i).getAddress();
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                setUpMapIfNeeded();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d("MapsActivity", "onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            Log.d("map", "Connection Failed");
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace, int distance) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + distance * 1000);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAGaiRVEwaNMzwXK5_OnOG_5PsanNumoMI");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Log.d("MapsActivity", "onLocationChanged");
        Log.d("mLastLocation", "mLastLocation" + mLastLocation);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Log.d("onLocationChanged", String.format("latitude:%.5f longitude:%.5f", latitude, longitude));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

        getSearchList(distance);
        setUpMapIfNeeded();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("map", "onConnectionFailed" + connectionResult);

        Log.d("MapsActivity", "onConnectionFailed");
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.d("MapsActivity", "onRequestPermissionsResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText(progress + " km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMap.clear();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        distance = seekBar.getProgress();
        Log.d("data", "dataString" + data);
        if (data.equalsIgnoreCase("search")) {

            clear();
            getSearchList(distance);
        } else {
            if (!google_shops_list.isEmpty()) {
                final int size = google_shops_list.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        google_shops_list.remove(0);
                    }

                    adapter.notifyItemRangeRemoved(0, size);
                }
            }
            getplacesBykm(distance, nearbyPlace);
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

    class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData = " ";
        String url;
        public String placeName = "";
        public String vicinity = "";

        private String opening_hours = "", icon = "";
        private String shopLong = "", shopLat = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
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
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(result);
            ShowNearbyPlaces(nearbyPlacesList);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (google_shops_list.size() != 0) {
                        data = "GoogleData";
                        adapter = new ShopsListAdpater(MapsActivity.this, "GoogleData", google_shops_list);
                        googlesearchnearbyrecyclerview.setLayoutManager(mLayoutManager);
                        googlesearchnearbyrecyclerview.setItemAnimator(new DefaultItemAnimator());
                        googlesearchnearbyrecyclerview.setAdapter(adapter);
                    } else {
                        txtemptylistnearbysearch.setText("No shops found!");
                    }

                }
            });
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
            double lng = 0, lat = 0;
            Log.d("nearbyPlacesList", "nearbyPlacesList" + nearbyPlacesList.toString());
            mMap.clear();

            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                GoogleShopList googleShopList = new GoogleShopList();
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                shopLat = googlePlace.get("lat");
                shopLong = googlePlace.get("lng");
                placeName = googlePlace.get("place_name");
                vicinity = googlePlace.get("vicinity");
                icon = googlePlace.get("icon");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                google_shops_list.add(googleShopList);
                if (lat != 0 && lng != 0) {
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    mMap.addMarker(markerOptions);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Geocoder geocoder = null;
                LatLng latLng = point;
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

                    if (addresses != null) {
                        Address address = addresses.get(0);

                        if (address != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i) + "\n");
                            }
                            Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (marker != null) {
                    marker.remove();
                }

              /*  marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
           */
            }
        });
    }

    public void setAdapter(ArrayList<ShopsListModel> shops_list) {
        if (shops_list.size() != 0) {
            String name = getIntent().getStringExtra("owner");
            adapter = new ShopsListAdpater(this, shops_list, name);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            searchnearbyrecyclerview.setLayoutManager(mLayoutManager);
            //  adapter.notifyDataSetChanged();
            searchnearbyrecyclerview.setAdapter(adapter);
            //    nearby_search_list_progressbar.setVisibility(View.GONE);
        } else {
            nearby_search_list_progressbar.setVisibility(View.GONE);
            txtemptylistnearbysearch.setText("No shops found!");
        }
    }


    public void getplacesBykm(int distance, String nearbyPlace) {
        String url = getUrl(latitude, longitude, nearbyPlace, distance);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        // Toast.makeText(MapsActivity.this, "Nearby Hospitals", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMainNearBy:
                searchnearbyrecyclerview.setVisibility(View.VISIBLE);
                googlesearchnearbyrecyclerview.setVisibility(View.GONE);
                clear();
                getSearchList(distance);
                break;
            case R.id.btnGoogleNearby:
                searchnearbyrecyclerview.setVisibility(View.GONE);
                googlesearchnearbyrecyclerview.setVisibility(View.VISIBLE);
                getplacesBykm(distance, nearbyPlace);
                break;
        }
    }
}
