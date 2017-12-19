package com.gajananmotors.shopfinder.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AddPostActivity extends AppCompatActivity {
    TextView txtBusinessLocation;
    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtBusinessLocation = findViewById(R.id.txtBusinessLocation);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
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

    public void getPhotos(View view) {
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
    }
}
