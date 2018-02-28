package com.gajananmotors.shopfinder.activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.ShopImagesAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.helper.Constant;
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
    private String area = "", city = "", state = "", country = "", pincode = "", strPlaceSearch = "", strCategorySearch = "",str_cat_spinner="";
    private String str_subCat_spinner="",shop_name="",shop_time="",strShopLocation="",strShopServices="",strShopWebUrl="",strShopMobile="";
    private double latitude, longitude;
    private GoogleApiClient mGoogleApiClient;
    private int int_cat_id, int_subcat_id, owner_id,shop_id;
    private LinearLayout edit_post_layout;
    public static ArrayList<String> images = new ArrayList<>();
    private static ShopImagesAdapter adapter;
    private  int position;
    private SharedPreferences sharedpreferences;
    private ProgressBar editShopProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editShopProgressbar=findViewById(R.id.editShopProgressbar);
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
        Intent intent=getIntent();
        position=intent.getIntExtra("position",0);
        img_rcv = findViewById(R.id.img_rcv);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        img_rcv.setLayoutManager(mLayoutManager);
        adapter = new ShopImagesAdapter(EditPostActivity.this,AllPostsActivity.shops_list.get(position).getShop_id(),position);
        img_rcv.setAdapter(adapter);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        etShopName.setText(AllPostsActivity.shops_list.get(position).getShop_name());
        etAddress.setText(AllPostsActivity.shops_list.get(position).getAddress());
        etMobileNumber.setText(AllPostsActivity.shops_list.get(position).getShop_mob_no());
        btncategory.setText(AllPostsActivity.shops_list.get(position).getCategory_name());
        btnsubCategory.setText(AllPostsActivity.shops_list.get(position).getSub_category_name());
        etServicesOffered.setText(AllPostsActivity.shops_list.get(position).getShop_details());
        etShopOpeningHours.setText(AllPostsActivity.shops_list.get(position).getShop_timing());
        etWebsite.setText(AllPostsActivity.shops_list.get(position).getWebsite());
        shop_id=AllPostsActivity.shops_list.get(position).getShop_id();
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
    public static void refresh()
      {
          adapter.notifyDataSetChanged();

      }
    public void update() {
        strPlaceSearch = area + "," + city + "," + state + "," + country;
        str_cat_spinner=btncategory.getText().toString();
        str_subCat_spinner=btnsubCategory.getText().toString();
        shop_name=etShopName.getText().toString();
        shop_time=etShopOpeningHours.getText().toString();
        strShopLocation=etAddress.getText().toString();
        strShopServices=etServicesOffered.getText().toString();
        strCategorySearch = str_cat_spinner + "," + str_subCat_spinner;
        strShopWebUrl=etWebsite.getText().toString();
        strShopMobile=etMobileNumber.getText().toString();
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        owner_id = sharedpreferences.getInt(Constant.OWNER_ID, 00000);
        for (SubCategoryModel subCategoryModel : sub_category_list) {
            if (TextUtils.equals(btnsubCategory.getText().toString().toLowerCase(), subCategoryModel.getName().toString().toLowerCase()))
                int_subcat_id = subCategoryModel.getSub_category_id();
        }
        editShopProgressbar.setVisibility(View.VISIBLE);
        editShopProgressbar.setIndeterminate(true);
        editShopProgressbar.setProgress(500);
        strCategorySearch = btncategory.getText().toString() + btnsubCategory.getText().toString();
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<CreateShopModel> call=restInterface.updateShopforEmptyImage(shop_id,int_cat_id, int_subcat_id, str_cat_spinner, str_subCat_spinner, strCategorySearch, owner_id, shop_name,
                    shop_time, strShopLocation, strShopServices,
                    String.valueOf(latitude), String.valueOf(longitude), area, city, state, country, pincode,
                    strPlaceSearch, strShopWebUrl, strShopMobile
            );
        call.enqueue(new Callback<CreateShopModel>() {
            @Override
            public void onResponse(Call<CreateShopModel> call, Response<CreateShopModel> response) {
                if(response.isSuccessful())
                {
                    editShopProgressbar.setVisibility(View.GONE);
                    Intent intent=new Intent(EditPostActivity.this,AllPostsActivity.class);
                    intent.putExtra("owner","owner");
                    startActivity(intent);
                    finish();
                    Toast.makeText(EditPostActivity.this, "Shop Updated Success!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CreateShopModel> call, Throwable t) {

            }
        });

    }
    private void showDialog(ArrayList<String> categoryNames, final boolean flag, final String title) {
        final String[] categories = categoryNames.toArray(new String[categoryNames.size()]);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(title);
        dialog.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.category_list, null);
        ListView lv = view.findViewById(R.id.listview);
        lv.setAdapter(adapter);
        // Change MyActivity.this and myListOfItems to your own values
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                if (flag == true) {
                    btncategory.setText(categories[position]);
                    editShopProgressbar.setVisibility(View.VISIBLE);
                    editShopProgressbar.setIndeterminate(true);
                    editShopProgressbar.setProgress(500);
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
        str_cat_spinner = btncategory.getText().toString();
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
                    editShopProgressbar.setVisibility(View.GONE);
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
    @Override
    protected void onResume() {
        super.onResume();
        images.clear();
        images.add(AllPostsActivity.shops_list.get(position).getShop_pic());
        images.add(AllPostsActivity.shops_list.get(position).getImage1());
        images.add(AllPostsActivity.shops_list.get(position).getImage2());
        images.add(AllPostsActivity.shops_list.get(position).getImage3());
        images.add(AllPostsActivity.shops_list.get(position).getImage4());
        images.add(AllPostsActivity.shops_list.get(position).getImage5());
        images.add(AllPostsActivity.shops_list.get(position).getImage6());
    }
}
