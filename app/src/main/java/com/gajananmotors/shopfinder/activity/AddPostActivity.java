package com.gajananmotors.shopfinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.Config;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.Category;
import com.gajananmotors.shopfinder.model.CategoryList;
import com.gajananmotors.shopfinder.model.SubCategoryList;
import com.gajananmotors.shopfinder.model.SubCategoryModel;
import com.gajananmotors.shopfinder.tedpicker.ImagePickerActivity;
import com.gajananmotors.shopfinder.utility.Validation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddPostActivity extends AppCompatActivity {
    private TextView txtBusinessLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private Place place;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final String TAG = "TedPicker";
    private ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ArrayList<Category> category_list = new ArrayList<>();
    private ArrayList<SubCategoryModel> sub_category_list = new ArrayList<>();
    //private AllCategory allCategory;
    private ViewGroup mSelectedImagesContainer;
    private MaterialBetterSpinner category, subcategory;
    private EditText etBusinessName, etBusinessEmail, etBusinessLocation, etBusinessMobile, etBusinessWebUrl, etBusinessServices;
    private Toolbar toolbar;
    private String getImages, area, city, state, strBusinessName, strCategory, strSubcategory, strBusinessEmail, strBusinessLocation, strBusinessMobile, strBusinessWebUrl, strBusinessServices;
    private String str_cat_spinner, str_subCat_spinner;
    private int int_cat_id, int_subcat_id;
    private boolean flag = false;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private SharedPreferences sharedpreferences;
    private int Owner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //allCategory = new AllCategory();
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        Owner_id=sharedpreferences.getInt(Constant.OWNER_ID,0);
        Toast.makeText(getApplicationContext(),""+Owner_id,Toast.LENGTH_LONG);
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
         /*StringCallback stringCallback = new StringCallback() {
            @Override
            public void StringCallback(String s) {
                if (TextUtils.equals(s,"1")){
                    for(int i=0;i<category_list.size();i++)
                        categoryNames.add(category_list.get(i).getName().toString());
                    category_list.clear();
                    flag=true;
                }
            }
        };
        category_list = AllCategory.getCategories(AddPostActivity.this,stringCallback);*/
        Call<CategoryList> call = restInterface.getCategoryList();
        call.enqueue(new Callback<CategoryList>() {
            ArrayList<Category> categoryArrayList = new ArrayList<>();
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (response.isSuccessful()) {
                    CategoryList categoryList = response.body();
                    category_list = categoryList.getCategories();
                    getCategoryData();
                }
            }
            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {

            }
        });
        category = findViewById(R.id.spnBusinessCategory);
        etBusinessName = findViewById(R.id.etBusinessName);
        etBusinessLocation = findViewById(R.id.etBusinessLocation);
        etBusinessMobile = findViewById(R.id.etBusinessMobile);
        etBusinessEmail = findViewById(R.id.etBusinessEmail);
        etBusinessWebUrl = findViewById(R.id.etBusinessWebUrl);
        etBusinessServices = findViewById(R.id.etBusinessServices);
        subcategory = findViewById(R.id.spnBusinessSubcategory);
        mSelectedImagesContainer = findViewById(R.id.selected_photos_container);
        View getImages = findViewById(R.id.btn_get_images);
        getImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImages(new Config());
            }
        });
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }
    public void getCategoryData() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (int i = 0; i < category_list.size(); i++) {
            categoryNames.add(category_list.get(i).getName().toString());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, categoryNames);
        category.setAdapter(categoryAdapter);
        category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                str_cat_spinner = category.getText().toString();
                for (Category cat : category_list) {
                    if (TextUtils.equals(cat.getName(), str_cat_spinner)) {
                        int_cat_id = cat.getCategory_id();
                    }
                }
               /* Toast.makeText(AddPostActivity.this, "Selected Category:"+str_cat_spinner, Toast.LENGTH_SHORT).show();
                Toast.makeText(AddPostActivity.this, "Selected Index:"+int_cat_id, Toast.LENGTH_SHORT).show();*/
                Call<SubCategoryList> sub_cat_list = restInterface.getSubCategoryList(int_cat_id);
                sub_cat_list.enqueue(new Callback<SubCategoryList>() {
                    @Override
                    public void onResponse(Call<SubCategoryList> call, Response<SubCategoryList> response) {
                        if (response.isSuccessful()) {
                            SubCategoryList list = response.body();
                          sub_category_list=list.getSubcategory();
                            getSubCategoryData();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubCategoryList> call, Throwable t) {
                    }
                });
            }
        });
        // subcategory.setAdapter(categoryAdapter);
    }

    public void getSubCategoryData() {
        ArrayList<String> SubCategoryNames = new ArrayList<>();
        for (int i = 0; i < sub_category_list.size(); i++) {
            SubCategoryNames.add(sub_category_list.get(i).getName().toString());
        }
        ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, SubCategoryNames);
        subcategory.setAdapter(subCategoryAdapter);
    }

    private void getImages(Config config) {
        ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(this, ImagePickerActivity.class);
        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }
    public void getAddress(View view) {
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
    public void submit(View view) {
        strBusinessName = etBusinessName.getText().toString().trim();
        strBusinessLocation = etBusinessLocation.getText().toString().trim();
        strBusinessMobile = etBusinessMobile.getText().toString().trim();
        strBusinessWebUrl = etBusinessWebUrl.getText().toString().trim();
        strBusinessServices = etBusinessServices.getText().toString().trim();
        strBusinessEmail = etBusinessEmail.getText().toString().trim();
        if (checkValidation())
            confirmdetails();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                place = PlacePicker.getPlace(data, this);
                etBusinessLocation.setText(place.getAddress());
                String address1 = place.getName().toString();
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    state = addresses.get(0).getAdminArea().toString();
                    city = addresses.get(0).getLocality().toString();
                    area = addresses.get(0).getSubLocality().toString();
                    String country = addresses.get(0).getCountryName();
                    // Toast.makeText(this, "State:" + state + "\nCity:" + city + "\nArea:" + area+"\nCountry:"+country, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris != null) {
                    HorizontalScrollView hview = findViewById(R.id.hori_scroll_view);
                    TextView tvHeading = findViewById(R.id.tvHeading);
                    hview.setVisibility(View.VISIBLE);
                    tvHeading.setVisibility(View.VISIBLE);

                    showMedia();
                }
            }
        }
    }
    private void showMedia() {
        mSelectedImagesContainer.removeAllViews();

        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);

        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (final Uri uri : image_uris) {

            final View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            final ImageView thumbnail = imageHolder.findViewById(R.id.media_image);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            AddPostActivity.this);
                    alertDialog.setTitle("Confirm ");
                    alertDialog.setMessage("Set your shop profile picture");
                    alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getBaseContext(), " successfully set your shop profile", Toast.LENGTH_SHORT).show();
                            getImages = uri.toString();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),
                                    "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            });
            Glide.with(this)
                    .load(uri.toString())
                    .fitCenter()
                    .centerCrop()
                    .into(thumbnail);
            mSelectedImagesContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }
    private void confirmdetails() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View confirmDialog = inflater.inflate(R.layout.dialog_confirmatiom, null);
        TextView tvShopName = confirmDialog.findViewById(R.id.tvShopName);
        TextView tvMobile = confirmDialog.findViewById(R.id.tvMobile);
        TextView tvCategory = confirmDialog.findViewById(R.id.tvCategory);
        TextView tvAddress = confirmDialog.findViewById(R.id.tvAddress);
        TextView tvArea = confirmDialog.findViewById(R.id.tvArea);
        ImageView imgShopProfile = confirmDialog.findViewById(R.id.imgShop_dialog);
        TextView tvEdit = confirmDialog.findViewById(R.id.tvEdit);
        TextView tvConfirm = confirmDialog.findViewById(R.id.tvConfirm);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm");
        alert.setView(confirmDialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        tvShopName.setText(strBusinessName);
        tvArea.setText(city + "," + state);
        tvMobile.setText(strBusinessMobile);
        tvAddress.setText(strBusinessLocation);
        Glide.with(AddPostActivity.this)
                .load(getImages)
                .fitCenter()
                .centerCrop()
                .into(imgShopProfile);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(AddPostActivity.this, "Edit Form", Toast.LENGTH_SHORT).show();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddPostActivity.this, "call api", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }
    private boolean checkValidation() {

        boolean ret = true;
        if (!Validation.hasText(etBusinessName)) ret = false;
        if (!Validation.hasText(etBusinessLocation)) ret = false;
        if (!Validation.isEmailAddress(etBusinessEmail, true)) ret = false;
        if (!Validation.isPhoneNumber(etBusinessMobile, true)) ret = false;
        if (!Validation.hasText(category)) ret = false;
        if (!Validation.hasText(subcategory)) ret = false;
        if(mSelectedImagesContainer.equals("")){
            Toast.makeText(getApplicationContext(),"Click Select Photo button",Toast.LENGTH_SHORT).show();
        }

        return ret;
    }
}