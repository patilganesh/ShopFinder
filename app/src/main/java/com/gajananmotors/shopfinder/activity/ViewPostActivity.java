package com.gajananmotors.shopfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CustomGalleryAdapter;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
import static com.gajananmotors.shopfinder.common.GeoAddress.getAddress;

public class ViewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout shopDirectionLayout,shopShareLayout,shopEditLayout,shopGallaryLayout;
    private LinearLayout gallaryLayout,viewPostLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        viewPostLayout = findViewById(R.id.viewPostLayout);
        shopDirectionLayout = findViewById(R.id.shopDirectionLayout);
        shopDirectionLayout.setOnClickListener(this);
        shopGallaryLayout=findViewById(R.id.shopGallaryLayout);
        shopGallaryLayout.setOnClickListener(this);
        shopEditLayout=findViewById(R.id.shopEditLayout);
        shopEditLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shopDirectionLayout:
                if (!isNetworkAvailable(this)) {
                    displayPromptForEnablingData(this);
                } else {
                    String address = getAddress(18.50284, 73.9255587, this);
                    // Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
                    Log.d("MultiViewType", "address" + address);
                    Uri gmmIntentUri = Uri.parse("geo:18.50284,73.92555?q=" + address);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                //startActivity(new Intent(this, MapsActivity.class));
                return;
            case R.id.shopGallaryLayout:
                startActivity(new Intent(this, GallaryActivity.class));
                return;
            case R.id.shopEditLayout:
                Toast.makeText(this, "Edit Post", Toast.LENGTH_SHORT).show();
                return;
            case R.id.shopShareLayout:
                Toast.makeText(this, "Share Post", Toast.LENGTH_SHORT).show();
                return;



        }
    }
}
