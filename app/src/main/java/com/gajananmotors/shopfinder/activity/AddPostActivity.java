package com.gajananmotors.shopfinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.gajananmotors.shopfinder.helper.Config;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.tedpicker.ImagePickerActivity;
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

public class AddPostActivity extends AppCompatActivity {
    TextView txtBusinessLocation;
    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    Place place;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    private static final String TAG = "TedPicker";
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;
    private MaterialBetterSpinner category, subcategory;
    EditText etBusinessName, etBusinessEmail, etBusinessLocation, etBusinessMobile, etBusinessWebUrl, etBusinessServices;
    private Toolbar toolbar;

    String getImages,state,city, area,strBusinessName,strCategory,strSubcategory, strBusinessEmail, strBusinessLocation, strBusinessMobile, strBusinessWebUrl, strBusinessServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] catList = {
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
        category = findViewById(R.id.spnBusinessCategory);
        etBusinessName = findViewById(R.id.etBusinessName);
        etBusinessLocation = findViewById(R.id.etBusinessLocation);
        etBusinessMobile = findViewById(R.id.etBusinessMobile);
        etBusinessEmail = findViewById(R.id.etBusinessEmail);
        etBusinessWebUrl = findViewById(R.id.etBusinessWebUrl);
        etBusinessServices = findViewById(R.id.etBusinessServices);
        subcategory = findViewById(R.id.spnBusinessSubcategory);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, catList);
        category.setAdapter(categoryAdapter);
        subcategory.setAdapter(categoryAdapter);
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
                    Toast.makeText(this, "State:" + state + "\nCity:" + city + "\nArea:" + area, Toast.LENGTH_SHORT).show();

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

}