package com.gajananmotors.shopfinder.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.DeleteShopModel;
import com.gajananmotors.shopfinder.model.LinkShopModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
import static com.gajananmotors.shopfinder.common.GeoAddress.getAddress;

public class ViewPostActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout viewPostLayout, shopDirectionLayout, shopDeleteLayout, shopShareLayout, shopEditLayout, shopGallaryLayout, shopCallLayout, shopMsgLayout;
    private TextView mob;
    private String shopCoverpic = "";
    private ImageView shopCoverphoto;
    private ArrayList<String> allimages = new ArrayList<>();
    private TextView tvShopName, tvAddress, tvMobile, tvCategory, tvSubcategory, tvWebsite, tvShopTime, tvServices;
    private ViewShopList viewShopList;
    private int shop_id = 0;
    private Toolbar toolbar;
    private ProgressBar viewpost_progressbar;
    private LinearLayout linearLayout;
    private int position;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPostLayout = findViewById(R.id.viewPostLayout);
        shopDirectionLayout = findViewById(R.id.shopDirectionLayout);
        shopDirectionLayout.setOnClickListener(this);
        shopGallaryLayout = findViewById(R.id.shopGallaryLayout);
        shopCoverphoto = findViewById(R.id.shopCoverphoto);
        tvShopName = findViewById(R.id.tvShopName);
        tvAddress = findViewById(R.id.tvAddress);
        tvMobile = findViewById(R.id.tvMobile);
        tvShopTime = findViewById(R.id.tvShopTime);
        tvCategory = findViewById(R.id.tvCategory);
        tvSubcategory = findViewById(R.id.tvSubcategory);
        linearLayout = findViewById(R.id.lin1);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvServices = findViewById(R.id.tvServices);
        shopGallaryLayout.setOnClickListener(this);
        shopEditLayout = findViewById(R.id.shopEditLayout);
        shopShareLayout = findViewById(R.id.shopShareLayout);
        shopDeleteLayout = findViewById(R.id.shopDeleteLayout);
        viewpost_progressbar = findViewById(R.id.viewpost_progressbar);
        shopEditLayout = findViewById(R.id.shopEditLayout);
        shopEditLayout.setOnClickListener(this);
        shopCallLayout = findViewById(R.id.shopCallLayout);
        shopCallLayout.setOnClickListener(this);
        shopMsgLayout = findViewById(R.id.shopMsgLayout);
        shopMsgLayout.setOnClickListener(this);
        shopDeleteLayout.setOnClickListener(this);
        shopShareLayout.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent i = getIntent();
        position = Integer.parseInt(i.getStringExtra("position"));
        name = i.getStringExtra("owner");
        shopEditLayout.setVisibility(View.GONE);
        shopDeleteLayout.setVisibility(View.GONE);
        shopCallLayout.setVisibility(View.VISIBLE);
        shopMsgLayout.setVisibility(View.VISIBLE);
        if (!name.isEmpty()) {
            if (name.equals("owner")) {
                shopEditLayout.setVisibility(View.VISIBLE);
                shopDeleteLayout.setVisibility(View.VISIBLE);
                shopCallLayout.setVisibility(View.GONE);
                shopMsgLayout.setVisibility(View.GONE);

                try {


                    tvShopName.setText(AllPostsActivity.shops_list.get(position).getShop_name());
                    tvAddress.setText(AllPostsActivity.shops_list.get(position).getAddress());
                    tvCategory.setText(AllPostsActivity.shops_list.get(position).getCategory_name());
                    tvServices.setText(AllPostsActivity.shops_list.get(position).getShop_details());
                    if (!AllPostsActivity.shops_list.get(position).getWebsite().isEmpty()) {
                        linearLayout.setVisibility(View.VISIBLE);
                        tvWebsite.setText(AllPostsActivity.shops_list.get(position).getWebsite());
                    }
                    tvSubcategory.setText(AllPostsActivity.shops_list.get(position).getSub_category_name());
                    tvMobile.setText(AllPostsActivity.shops_list.get(position).getShop_mob_no());
                    tvShopTime.setText("Open : " + AllPostsActivity.shops_list.get(position).getShop_mob_no());
                    shopCoverpic = AllPostsActivity.shops_list.get(position).getShop_pic();
                    shop_id = AllPostsActivity.shops_list.get(position).getShop_id();

                } catch (Exception e) {
                    Toast.makeText(this, "" + position + "     " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                viewShopList = getIntent().getParcelableExtra("shop_list");
                shopEditLayout.setVisibility(View.GONE);
                shopDeleteLayout.setVisibility(View.GONE);
                shopCallLayout.setVisibility(View.VISIBLE);
                shopMsgLayout.setVisibility(View.VISIBLE);
                tvShopName.setText(viewShopList.getStrShop_name());
                tvAddress.setText(viewShopList.getStrAddress());
                tvCategory.setText(viewShopList.getStrCategory());
                tvServices.setText(viewShopList.getStrservices());
                if (!viewShopList.getStrWeburl().isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                    tvWebsite.setText(viewShopList.getStrWeburl());
                }
                tvSubcategory.setText(viewShopList.getStrSub_category());
                tvMobile.setText(viewShopList.getStrMobile());
                tvShopTime.setText("Open : " + viewShopList.getStrShopTime());
                shopCoverpic = viewShopList.getStrShop_pic();
                allimages = viewShopList.getArrayList();
                shop_id = viewShopList.getShop_id();
            }
            Picasso.with(ViewPostActivity.this)
                    .load("http://findashop.in/images/shop_profile/" + shop_id + "/" + shopCoverpic)
                    .fit()
                    .placeholder(R.drawable.background_splashscreen)
                    .into(shopCoverphoto);
        }
       /* else*//*{
           Uri data = i.getData();
            LinkShopServices(shop_id);
        }*/
    }

    private void LinkShopServices(final int shop_id) {
        Retrofit retrofit;
        retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<LinkShopModel> linkShopModel = restInterface.linkShop(shop_id);
        viewpost_progressbar.setVisibility(View.VISIBLE);
        viewpost_progressbar.setIndeterminate(true);
        viewpost_progressbar.setProgress(500);
        linkShopModel.enqueue(new Callback<LinkShopModel>() {
            @Override
            public void onResponse(Call<LinkShopModel> call, Response<LinkShopModel> response) {
                if (response.isSuccessful()) {
                    LinkShopModel linkShopModel = response.body();
                    viewpost_progressbar.setVisibility(View.INVISIBLE);
                    String msg = linkShopModel.getCity();
                    tvShopName.setText(linkShopModel.getShop_name());
                    tvAddress.setText(linkShopModel.getAddress());
                    tvCategory.setText(linkShopModel.getCategory_name());
                    if (linkShopModel.getWebsite().isEmpty()) {
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        tvWebsite.setText(linkShopModel.getWebsite());
                    }
                    tvSubcategory.setText(linkShopModel.getSub_category_name());
                    tvMobile.setText(linkShopModel.getShop_mob_no());
                    shopCoverpic = linkShopModel.getShop_pic();
                    tvShopTime.setText("Open : " + linkShopModel.getShop_timing());
                    allimages = viewShopList.getArrayList();
                    Picasso.with(ViewPostActivity.this)
                            .load("http://findashop.in/images/shop_profile/" + "shop_id" + "/" + shop_id)
                            .fit()
                            .placeholder(R.drawable.background_splashscreen)
                            .into(shopCoverphoto);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(ViewPostActivity.this, AllPostsActivity.class));
                }
            }

            @Override
            public void onFailure(Call<LinkShopModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopDirectionLayout:
                //   viewShopList.getLatitude();
                if (!isNetworkAvailable(this)) {
                    displayPromptForEnablingData(this);
                } else {
                    if (name.equals("owner")) {
                        String address = getAddress(Double.parseDouble(AllPostsActivity.shops_list.get(position).getShop_lat()), Double.parseDouble(AllPostsActivity.shops_list.get(position).getShop_long()), this);
                        // Log.d("MultiViewType", "address" + address);
                        Uri gmmIntentUri = Uri.parse("geo:" + Double.parseDouble(AllPostsActivity.shops_list.get(position).getShop_lat()) + "," + Double.parseDouble(AllPostsActivity.shops_list.get(position).getShop_lat()) + "?q=" + address);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } else {
                        String address = getAddress(viewShopList.getLatitude(), viewShopList.getLongitude(), this);
                        Log.d("MultiViewType", "address" + address);
                        Uri gmmIntentUri = Uri.parse("geo:" + viewShopList.getLatitude() + "," + viewShopList.getLatitude() + "?q=" + address);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
                break;
            case R.id.shopGallaryLayout:
                Intent i = new Intent(this, GallaryActivity.class);
                i.putExtra("shop_id", shop_id);
                if (name.equals("owner")) {
                    i.putExtra("position", position);
                    i.putExtra("owner", "owner");
                } else {
                    i.putStringArrayListExtra("images", allimages);
                    i.putExtra("shopCoverphoto", shopCoverpic);
                    i.putExtra("owner", "user");
                }
                startActivity(i);

                break;
            case R.id.shopCallLayout:

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvMobile.getText().toString()));
                startActivity(callIntent);
                break;
            case R.id.shopDeleteLayout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPostActivity.this);
                alertDialog.setMessage("Are you sure you want to delete your shop post? ");
                alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //   Toast.makeText(activity,shop_id,Toast.LENGTH_LONG).show();

                        deleteShopServices(shop_id);
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.shopMsgLayout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", tvMobile.getText().toString(), null)));
                break;
            case R.id.shopEditLayout:
                Intent intent = new Intent(this, EditPostActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                break;
            case R.id.shopShareLayout:
                Toast.makeText(getApplicationContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
               /* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "http://www.findashop.in/index.php/mobile_api/upload_shop_images"+shop_id;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));*/
                break;
        }
    }

    private void deleteShopServices(int shop_id) {
        Retrofit retrofit;
        DeleteShopModel deleteShopModel;
        deleteShopModel = new DeleteShopModel();
        retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<DeleteShopModel> deleteShop = restInterface.deleteShop(shop_id);
        viewpost_progressbar.setVisibility(View.VISIBLE);
        viewpost_progressbar.setIndeterminate(true);
        viewpost_progressbar.setProgress(500);
        deleteShop.enqueue(new Callback<DeleteShopModel>() {
            @Override
            public void onResponse(Call<DeleteShopModel> call, Response<DeleteShopModel> response) {
                if (response.isSuccessful()) {
                    viewpost_progressbar.setVisibility(View.INVISIBLE);
                    String msg = response.body().getMsg();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(ViewPostActivity.this, AllPostsActivity.class));
                }
            }

            @Override
            public void onFailure(Call<DeleteShopModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!name.equals("owner")) {
            allimages.clear();
        }
        finish();
    }
}