package com.gajananmotors.shopfinder.activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import android.view.LayoutInflater;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopImagesAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.model.CategoryListModel;
import com.gajananmotors.shopfinder.model.CategoryModel;
import com.gajananmotors.shopfinder.model.CreateShopModel;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
public class EditPostActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewShopList viewShopList;
    private EditText etShopName, etAddress, etMobileNumber, etServicesOffered, etShopOpeningHours, etWebsite;
    private Button btncategory, btnsubCategory, btnUpdate;
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<String> subCategoryNames = new ArrayList<>();
    private ArrayList<CategoryModel> category_Model_list = new ArrayList<>();
    private ArrayList<SubCategoryModel> sub_category_list = new ArrayList<>();
    private RecyclerView img_rcv;
    private RecyclerView.LayoutManager mLayoutManager;
    private int PLACE_PICKER_REQUEST = 1;
    private Place place;
    private String area = "", city = "", state = "", country = "", pincode = "", strPlaceSearch = "", strCategorySearch = "";
    private double latitude, longitude;
    private GoogleApiClient mGoogleApiClient;
    private int int_cat_id, int_subcat_id, owner_id;
    // private TextView txtnetwork_error_massege;
    private LinearLayout edit_post_layout;
    private ArrayList<String> images = new ArrayList<>();
    private ShopImagesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      txtnetwork_error_massege=findViewById(R.id.network_error_massege);
        edit_post_layout = findViewById(R.id.edit_post_layout);
        etShopName = findViewById(R.id.etShopName);
        etAddress = findViewById(R.id.etAddress);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etServicesOffered = findViewById(R.id.etServicesOffered);
        etShopOpeningHours = findViewById(R.id.etShopOpeningHours);
        etWebsite = findViewById(R.id.etWebsite);
        btncategory = findViewById(R.id.etCategory);
        btnsubCategory = findViewById(R.id.etSubCategory);
        btnUpdate = findViewById(R.id.btnUpdate);
        viewShopList = getIntent().getParcelableExtra("shop_list");
        img_rcv = findViewById(R.id.img_rcv);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        img_rcv.setLayoutManager(mLayoutManager);
        images = viewShopList.getArrayList();
        images.add(0, viewShopList.getStrShop_pic());
        ShopImagesAdapter adapter = new ShopImagesAdapter(EditPostActivity.this, images, viewShopList.getShop_id());
        img_rcv.setAdapter(adapter);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       /* ConnectionDetector detector = new ConnectionDetector(this);
        if (!detector.isConnectingToInternet()) {
           txtnetwork_error_massege.setVisibility(View.VISIBLE);
            edit_post_layout.setVisibility(View.GONE);
        }*/
        // txtnetwork_error_massege.setVisibility(View.GONE);
        etShopName.setText(viewShopList.getStrShop_name());
        etAddress.setText(viewShopList.getStrAddress());
        etMobileNumber.setText(viewShopList.getStrMobile());
        btncategory.setText(viewShopList.getStrCategory());
        btnsubCategory.setText(viewShopList.getStrSub_category());
        etServicesOffered.setText(viewShopList.getStrservices());
        etShopOpeningHours.setText(viewShopList.getStrShopTime());
        etWebsite.setText(viewShopList.getStrWeburl());
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<CategoryListModel> call = restInterface.getCategoryList();
        call.enqueue(new Callback<CategoryListModel>() {
            @Override
            public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                if (response.isSuccessful()) {
                    CategoryListModel categoryListModel = response.body();
                    category_Model_list = categoryListModel.getCategories();
                    for (int i = 0; i < category_Model_list.size(); i++) {
                        categoryNames.add(category_Model_list.get(i).getName().toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoryListModel> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Fail to Load Category", Toast.LENGTH_SHORT).show();
            }
        });
        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(categoryNames, true, "Choose Category");
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

    }

    /*  public void refresh()
      {
          viewShopList = getIntent().getParcelableExtra("shop_list");
          adapter= new ShopImagesAdapter(EditPostActivity.this, images, viewShopList.getShop_id());
          img_rcv.setAdapter(adapter);
      }*/
    public void update() {
        strPlaceSearch = area + "," + city + "," + state + "," + country;
        for (SubCategoryModel subCategoryModel : sub_category_list) {
            if (TextUtils.equals(btnsubCategory.getText().toString().toLowerCase(), subCategoryModel.getName().toString().toLowerCase()))
                int_subcat_id = subCategoryModel.getSub_category_id();
        }
        strCategorySearch = btncategory.getText().toString() + btnsubCategory.getText().toString();
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
       /* Call<CreateShopModel> call=restInterface.createShopforEmptyImage(int_cat_id, int_subcat_id, str_cat_spinner, str_subCat_spinner, strCategorySearch, owner_id, strBusinessName,
                    strBusinessHour, strBusinessLocation, strBusinessServices,
                    String.valueOf(latitude), String.valueOf(longitude), area, city, state, country, pincode,
                    strPlaceSearch, strBusinessWebUrl, strBusinessMobile
            );
        call.enqueue(new Callback<CreateShopModel>() {
            @Override
            public void onResponse(Call<CreateShopModel> call, Response<CreateShopModel> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(EditPostActivity.this, "Shop Edit Success!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateShopModel> call, Throwable t) {

            }
        });
 */
    }

    private void showDialog(ArrayList<String> categoryNames, final boolean flag, String title) {
        final String[] categories = categoryNames.toArray(new String[categoryNames.size()]);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(title);
        dialog.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.category_list, null);
        ListView lv = (ListView) view.findViewById(R.id.listview);
        lv.setAdapter(adapter);
        // Change MyActivity.this and myListOfItems to your own values
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                if (flag == true) {
                    btncategory.setText(categories[position]);
                    getSubCategory();
                } else {
                    btnsubCategory.setText(categories[position]);
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void getSubCategory() {
        String str_cat_spinner = btncategory.getText().toString();
        for (CategoryModel cat : category_Model_list) {
            if (TextUtils.equals(cat.getName().toString().toLowerCase(), str_cat_spinner.toLowerCase())) {
                int_cat_id = cat.getCategory_id();
            }
        }
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<SubCategoryListModel> sub_cat_list = restInterface.getSubCategoryList(int_cat_id);
        subCategoryNames.clear();
        sub_cat_list.enqueue(new Callback<SubCategoryListModel>() {
            @Override
            public void onResponse(Call<SubCategoryListModel> call, Response<SubCategoryListModel> response) {
                if (response.isSuccessful()) {
                    // subcategory_progressbar.setVisibility(View.INVISIBLE);
                    SubCategoryListModel list = response.body();
                    sub_category_list = list.getSubcategory();
                    for (int i = 0; i < sub_category_list.size(); i++) {
                        subCategoryNames.add(sub_category_list.get(i).getName().toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<SubCategoryListModel> call, Throwable t) {
            }
        });
        btnsubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(subCategoryNames, false, "Choose SubCategory:");
            }
        });
    }
    public void getEditAddress(View view) {
        ConnectionDetector detector = new ConnectionDetector(this);
        if (!detector.isConnectingToInternet())
            Toast.makeText(this, "Please check your data Connection.", Toast.LENGTH_LONG).show();
        else {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                place = PlacePicker.getPlace(data, this);
                etAddress.setText(place.getAddress());
                String address1 = place.getName().toString();
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    state = addresses.get(0).getAdminArea().toString();
                    city = addresses.get(0).getLocality().toString();
                    area = addresses.get(0).getSubLocality().toString();
                    country = addresses.get(0).getCountryName();
                    pincode = addresses.get(0).getPostalCode();
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    // Toast.makeText(this, "State:" + state + "\nCity:" + city + "\nArea:" + area+"\nCountry:"+country, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
