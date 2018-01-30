package com.gajananmotors.shopfinder.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
import static com.gajananmotors.shopfinder.common.GeoAddress.getAddress;

public class ViewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout viewPostLayout, shopDirectionLayout, shopShareLayout, shopEditLayout, shopGallaryLayout, shopCallLayout, shopMsgLayout;
    private TextView mob;
    private String mob_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        viewPostLayout = findViewById(R.id.viewPostLayout);
        shopDirectionLayout = findViewById(R.id.shopDirectionLayout);
        shopDirectionLayout.setOnClickListener(this);
        shopGallaryLayout = findViewById(R.id.shopGallaryLayout);
        shopGallaryLayout.setOnClickListener(this);
        shopEditLayout = findViewById(R.id.shopEditLayout);
        shopEditLayout.setOnClickListener(this);
        shopCallLayout = findViewById(R.id.shopCallLayout);
        shopCallLayout.setOnClickListener(this);
        shopMsgLayout = findViewById(R.id.shopMsgLayout);
        shopMsgLayout.setOnClickListener(this);
        mob = findViewById(R.id.tvMobile);
        mob_number = mob.getText().toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.shopCallLayout:

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mob_number));
                startActivity(callIntent);
                return;
            case R.id.shopMsgLayout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mob_number, null)));
                return;
            case R.id.shopEditLayout:
                startActivity(new Intent(this, AddPostActivity.class));
                return;
            case R.id.shopShareLayout:
                Toast.makeText(this, "Share Post", Toast.LENGTH_SHORT).show();
                return;
        }
    }

}
