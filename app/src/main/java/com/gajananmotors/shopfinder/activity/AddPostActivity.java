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
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.Config;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.CategoryModel;
import com.gajananmotors.shopfinder.model.CategoryListModel;
import com.gajananmotors.shopfinder.model.CreateShopModel;
import com.gajananmotors.shopfinder.model.SubCategoryListModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;
import com.gajananmotors.shopfinder.model.UploadShopImagesModel;
import com.gajananmotors.shopfinder.tedpicker.ImagePickerActivity;
import com.gajananmotors.shopfinder.utility.Validation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private ArrayList<CategoryModel> category_Model_list = new ArrayList<>();
    private ArrayList<SubCategoryModel> sub_category_list = new ArrayList<>();
    //private AllCategory allCategory;
    private ViewGroup mSelectedImagesContainer;
    private MaterialBetterSpinner category, subcategory;
    private EditText etBusinessName, etBusinessEmail, etBusinessLocation, etBusinessMobile, etBusinessWebUrl, etBusinessServices, etBusinessHour;
    private Toolbar toolbar;
    private String getImages = "", area = "", city = "", state = "", strBusinessName = "", strBusinessLocation = "", strBusinessMobile = "", strBusinessHour = "", country = "", pincode = "";
    private String strBusinessWebUrl = "", strBusinessServices = "", strBusinessEmail = "", strPlaceSearch = "";
    private String str_cat_spinner = "", str_subCat_spinner = "", strCategorySearch = "";
    private int int_cat_id, int_subcat_id, owner_id;
    double latitude, longitude;
    private boolean flag = false;
    private Retrofit retrofit;
    private RestInterface restInterface;
    private SharedPreferences sharedpreferences;
    private int count = 0;
    private CreateShopModel shop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrofit = APIClient.getClient();
        restInterface = retrofit.create(RestInterface.class);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        owner_id = sharedpreferences.getInt(Constant.OWNER_ID, 00000);
         /*StringCallback stringCallback = new StringCallback() {
            @Override
            public void StringCallback(String s) {
                if (TextUtils.equals(s,"1")){
                    for(int i=0;i<category_Model_list.size();i++)
                        categoryNames.add(category_Model_list.get(i).getName().toString());
                    category_Model_list.clear();
                    flag=true;
                }
            }
        };
        category_Model_list = AllCategory.getCategories(AddPostActivity.this,stringCallback);*/
        Call<CategoryListModel> call = restInterface.getCategoryList();
        call.enqueue(new Callback<CategoryListModel>() {
            ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
            @Override
            public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                if (response.isSuccessful()) {
                    CategoryListModel categoryListModel = response.body();
                    category_Model_list = categoryListModel.getCategories();
                    getCategoryData();
                }
            }
            @Override
            public void onFailure(Call<CategoryListModel> call, Throwable t) {

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
        etBusinessHour = findViewById(R.id.etBusinessHour);
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
        for (int i = 0; i < category_Model_list.size(); i++) {
            categoryNames.add(category_Model_list.get(i).getName().toString());
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
                for (CategoryModel cat : category_Model_list) {
                    if (TextUtils.equals(cat.getName().toString().toLowerCase(), str_cat_spinner.toLowerCase())) {
                        int_cat_id = cat.getCategory_id();
                    }
                }
               /* Toast.makeText(AddPostActivity.this, "Selected CategoryModel:"+str_cat_spinner, Toast.LENGTH_SHORT).show();
                Toast.makeText(AddPostActivity.this, "Selected Index:"+int_cat_id, Toast.LENGTH_SHORT).show();*/
                Call<SubCategoryListModel> sub_cat_list = restInterface.getSubCategoryList(int_cat_id);
                sub_cat_list.enqueue(new Callback<SubCategoryListModel>() {
                    @Override
                    public void onResponse(Call<SubCategoryListModel> call, Response<SubCategoryListModel> response) {
                        if (response.isSuccessful()) {
                            SubCategoryListModel list = response.body();
                            sub_category_list = list.getSubcategory();
                            getSubCategoryData();
                        }
                    }
                    @Override
                    public void onFailure(Call<SubCategoryListModel> call, Throwable t) {
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
        subcategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_subCat_spinner = subcategory.getText().toString();
                for (SubCategoryModel subCategoryModel : sub_category_list) {
                    if (TextUtils.equals(str_subCat_spinner.toLowerCase(), subCategoryModel.getName().toString().toLowerCase()))
                        int_subcat_id = subCategoryModel.getSub_category_id();
                }
                Toast.makeText(AddPostActivity.this, "\nId:" + int_subcat_id + "\nName:" + str_subCat_spinner, Toast.LENGTH_LONG).show();
            }
        });
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
        strBusinessHour = etBusinessHour.getText().toString().trim();
        strCategorySearch = str_cat_spinner + "," + str_subCat_spinner;
        strPlaceSearch = area + "," + city + "," + state + "," + country;
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
        int index = 1;
        mSelectedImagesContainer.removeAllViews();

        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);

        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (final Uri uri : image_uris) {
            if (index <= 7) {
                Log.i("path", "\nImages Path: " + uri.getPath().toString());
                final View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
                final ImageView thumbnail = imageHolder.findViewById(R.id.media_image);
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                AddPostActivity.this);
                        alertDialog.setTitle("Confirm ");
                        alertDialog.setMessage("Set your shop cover picture!");
                        alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), " Successfully set your shop cover photo!", Toast.LENGTH_LONG).show();
                                getImages = uri.toString();
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        "Photo selection rejected!", Toast.LENGTH_LONG).show();
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
                index++;
            } else
                break;
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
        Toast.makeText(this, "Get Images Size:" + getImages.length(), Toast.LENGTH_LONG).show();
        Glide.with(AddPostActivity.this)
                .load(getImages)
                .fitCenter()
                .centerCrop()
                .into(imgShopProfile);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createShop();//calling web services for create shop
                alertDialog.dismiss();
            }
        });
    }
    public void createShop() {
        MultipartBody.Part fileToUpload = null;
        if (!getImages.equals("")) {
            File filePath = new File(getImages);
            try {
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), filePath);
                fileToUpload = MultipartBody.Part.createFormData("shop_pic", filePath.getName(), mFile);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<CreateShopModel> shopModelCall = restInterface.createShop(
                int_cat_id, int_subcat_id, strCategorySearch, owner_id, strBusinessName,
                strBusinessHour, strBusinessLocation, strBusinessServices,
                String.valueOf(latitude), String.valueOf(longitude), area, city, state, country, pincode,
                strPlaceSearch, strBusinessWebUrl, fileToUpload, strBusinessMobile);
        shopModelCall.enqueue(new Callback<CreateShopModel>() {
            @Override
            public void onResponse(Call<CreateShopModel> call, Response<CreateShopModel> response) {
                if (response.isSuccessful()) {
                    shop = response.body();
                    if (shop.getResult() == 1) {
                        Toast.makeText(AddPostActivity.this, "Shop Create Success..." + shop.getMsg(), Toast.LENGTH_LONG).show();
                        uploadShopImages(count);
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateShopModel> call, Throwable t) {
            }
        });
    }

    public void uploadShopImages(final int index) {
        if (image_uris.size() > index) {
            //int size=image_uris.size();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    File file_path = new File(image_uris.get(index).getPath().toString());
                    MultipartBody.Part fileToUpload = null;
                    if (file_path != null) {
                        try {
                            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_path);
                            fileToUpload = MultipartBody.Part.createFormData("image", file_path.getName(), mFile);
                        } catch (Exception e) {
                            Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    Retrofit retrofit = APIClient.getClient();
                    RestInterface restInterface = retrofit.create(RestInterface.class);
                    Call<UploadShopImagesModel> call = restInterface.uploadShopImages(
                            shop.getShop_id(), fileToUpload, shop.getShop_mob_no(), "Create", index);
                    call.enqueue(new Callback<UploadShopImagesModel>() {
                        @Override
                        public void onResponse(Call<UploadShopImagesModel> call, Response<UploadShopImagesModel> response) {
                            if (response.isSuccessful()) {
                                UploadShopImagesModel uploadShopImagesModel = response.body();
                                if (uploadShopImagesModel.getResult() == 1)
                                    uploadShopImages(uploadShopImagesModel.getCount());
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadShopImagesModel> call, Throwable t) {

                        }
                    });
                }
            }, 4000);
        }
    }
/*
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(etBusinessName,"Business Name")) ret = false;
        if (!Validation.hasText(etBusinessLocation,"Business Location")) ret = false;
        if (!Validation.isEmailAddress(etBusinessEmail, true,"Email")) ret = false;
        if (!Validation.isPhoneNumber(etBusinessMobile, true,"Mobile Number")) ret = false;
        if (!Validation.hasText(category,"Category")) ret = false;
        if (!Validation.hasText(subcategory,"Subcategory")) ret = false;
        if (mSelectedImagesContainer.equals("")) {
            Toast.makeText(getApplicationContext(), "Click Select Photo button", Toast.LENGTH_SHORT).show();
        }

        return ret;
    }*/


    private boolean checkValidation() {
        boolean ret=true;

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        LinearLayout linear_layout=findViewById(R.id.linear_layout);

        String name = etBusinessName.getText().toString();
        String email = etBusinessEmail.getText().toString();
        String location = etBusinessLocation.getText().toString();
        String mob = etBusinessMobile.getText().toString();
        String categoryType = category.getText().toString();
        String subcategoryType = subcategory.getText().toString();

        if(name.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Name", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(email.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Email", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (!email.matches(emailPattern)){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Invalid Email", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(location.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Location", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(mob.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Mobile Number", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(mob.length()<=9){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Invalid Mobile Number", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(categoryType.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please select Category ", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(subcategoryType.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please select Subcategory", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }

        if (mSelectedImagesContainer.equals("")) {
            Toast.makeText(getApplicationContext(), "Click Select Photo button", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

}