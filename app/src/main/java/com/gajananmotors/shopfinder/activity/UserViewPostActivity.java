package com.gajananmotors.shopfinder.activity;



        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.gajananmotors.shopfinder.R;
        import com.gajananmotors.shopfinder.common.ViewShopList;
        import com.gajananmotors.shopfinder.helper.Constant;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

        import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
        import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
        import static com.gajananmotors.shopfinder.common.GeoAddress.getAddress;

public class UserViewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout viewPostLayout, shopDirectionLayout, shopShareLayout, shopEditLayout, shopGallaryLayout, shopCallLayout, shopMsgLayout;
    private TextView mob;
    private String shopCoverpic;
    private ImageView shopCoverphoto;
    private ArrayList<String> allimages = new ArrayList<>();
    private TextView tvShopName, tvAddress, tvMobile, tvCategory, tvSubcategory, tvWebsite;
    ViewShopList viewShopList;
    int shop_id;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);


        viewPostLayout = findViewById(R.id.viewPostLayout);
        shopDirectionLayout = findViewById(R.id.shopDirectionLayout);
        shopDirectionLayout.setOnClickListener(this);
        shopGallaryLayout = findViewById(R.id.shopGallaryLayout);
        shopCoverphoto = findViewById(R.id.shopCoverphoto);
        tvShopName = findViewById(R.id.tvShopName);
        tvAddress = findViewById(R.id.tvAddress);
        tvMobile = findViewById(R.id.tvMobile);
        tvCategory = findViewById(R.id.tvCategory);
        tvSubcategory = findViewById(R.id.tvSubcategory);
        tvWebsite = findViewById(R.id.tvWebsite);
        shopGallaryLayout.setOnClickListener(this);
        shopEditLayout = findViewById(R.id.shopEditLayout);
        viewShopList = (ViewShopList) getIntent().getParcelableExtra("shop_list");
        tvShopName.setText(viewShopList.getStrShop_name());
        tvAddress.setText(viewShopList.getStrAddress());
        tvCategory.setText(viewShopList.getStrCategory());
        tvWebsite.setText(viewShopList.getStrWeburl());
        tvSubcategory.setText(viewShopList.getStrSub_category());
        tvMobile.setText(viewShopList.getStrMobile());
        shopCoverpic = viewShopList.getStrShop_pic();
        allimages = viewShopList.getArrayList();
        shop_id = viewShopList.getShop_id();
        shopEditLayout = findViewById(R.id.shopEditLayout);
        shopEditLayout.setOnClickListener(this);
        shopCallLayout = findViewById(R.id.shopCallLayout);
        shopCallLayout.setOnClickListener(this);
        shopMsgLayout = findViewById(R.id.shopMsgLayout);
        shopMsgLayout.setOnClickListener(this);
        if (!sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
            shopEditLayout.setVisibility(View.VISIBLE);
            shopCallLayout.setVisibility(View.GONE);
            shopMsgLayout.setVisibility(View.GONE);
        }
        Picasso.with(UserViewPostActivity.this)
                .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + viewShopList.getStrShop_pic())
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(shopCoverphoto);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopDirectionLayout:
                viewShopList.getLatitude();
                if (!isNetworkAvailable(this)) {
                    displayPromptForEnablingData(this);
                } else {
                    String address = getAddress(viewShopList.getLatitude(), viewShopList.getLongitude(), this);
                    // Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
                    Log.d("MultiViewType", "address" + address);
                    Uri gmmIntentUri = Uri.parse("geo:" + viewShopList.getLatitude() + "," + viewShopList.getLatitude() + "?q=" + address);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                //startActivity(new Intent(this, MapsActivity.class));
                return;
            case R.id.shopGallaryLayout:
                Intent i = new Intent(this, UserGallaryActivity.class);
                // i.putExtra("images", allimages );
                i.putStringArrayListExtra("images", allimages);
                i.putExtra("shopCoverphoto", shopCoverpic);
                i.putExtra("shop_id", shop_id);
                startActivity(i);


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
                callIntent.setData(Uri.parse("tel:" + tvMobile.getText().toString()));
                startActivity(callIntent);
                return;
            case R.id.shopMsgLayout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", tvMobile.getText().toString(), null)));
                return;
            case R.id.shopEditLayout:
                startActivity(new Intent(this, EditPostActivity.class));
                return;
            case R.id.shopShareLayout:
                Toast.makeText(this, "Share Post", Toast.LENGTH_SHORT).show();
                return;
        }
    }


}
