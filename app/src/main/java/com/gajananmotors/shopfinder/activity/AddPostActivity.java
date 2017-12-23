package com.gajananmotors.shopfinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

import java.util.ArrayList;

public class AddPostActivity extends AppCompatActivity {
    TextView txtBusinessLocation;
    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    Place place;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private Button  getImages;
    private static final String TAG = "TedPicker";
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSelectedImagesContainer = findViewById(R.id.selected_photos_container);
        View getImages = findViewById(R.id.btn_get_images);
        getImages.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                getImages(new Config());
            }
        });


        txtBusinessLocation = findViewById(R.id.txtBusinessLocation);
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
        confirmdetails();
    }

    private void confirmdetails() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View confirmDialog = inflater.inflate(R.layout.dialog_confirmatiom, null);
        TextView tvShopName = findViewById(R.id.tvShopName);
        TextView tvMobile = findViewById(R.id.tvMobile);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvArea = findViewById(R.id.tvArea);
        ImageView imgShopName = findViewById(R.id.imgShop);
        ImageButton btnEdit = confirmDialog.findViewById(R.id.btnEdit);
        ImageButton btnConfirm = confirmDialog.findViewById(R.id.btnConfirm);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm");
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(AddPostActivity.this, "Edit Form", Toast.LENGTH_SHORT).show();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddPostActivity.this, "call api", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                txtBusinessLocation.setText(place.getAddress());
            }
        }
       if (resultCode == Activity.RESULT_OK) {
           if (requestCode == INTENT_REQUEST_GET_IMAGES) {

               image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

               if (image_uris != null) {
                   showMedia();
               }


           }
       }
    }
    private void showMedia() {
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (Uri uri : image_uris) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            Glide.with(this)
                    .load(uri.toString())
                    .fitCenter()
                    .into(thumbnail);

            mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }

    }
}
