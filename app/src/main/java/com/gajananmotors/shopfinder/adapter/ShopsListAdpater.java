package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.AllPostsActivity;
import com.gajananmotors.shopfinder.activity.MainActivity;
import com.gajananmotors.shopfinder.activity.ProfileActivity;
import com.gajananmotors.shopfinder.activity.UserViewPostActivity;
import com.gajananmotors.shopfinder.activity.ViewPostActivity;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.holder.ShopsListHolder;
import com.gajananmotors.shopfinder.model.DeleteShopModel;
import com.gajananmotors.shopfinder.model.DeleteUserModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListHolder> {
    Activity activity;
    ArrayList<ShopsListModel> list = new ArrayList<>();
    private LinearLayout viewPostLayout;
    private ArrayList<String> images=new ArrayList<>();
    private int shop_id;
    private int index;
    ViewShopList viewShopList=new ViewShopList();
    private boolean b;


    public ShopsListAdpater(Activity activity, LinearLayout viewPostLayout, ArrayList<ShopsListModel> shops_list) {
        this.list = shops_list;
        this.activity = activity;
        this.viewPostLayout = viewPostLayout;
    }

    public ShopsListAdpater(ArrayList<ShopsListModel> shops_list) {
        this.list = shops_list;
    }

    public ShopsListAdpater(AllPostsActivity allPostsActivity, LinearLayout viewPostLayout, ArrayList<ShopsListModel> shops_list, boolean b) {
        this.activity=allPostsActivity;
        this.viewPostLayout=viewPostLayout;
        this.list=shops_list;
        this.b=b;
    }

    @Override
    public ShopsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_shops_list, parent, false);
        return new ShopsListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopsListHolder holder, final int position) {
        holder.name.setText(list.get(position).getShop_name());
        holder.address.setText(list.get(position).getAddress());
        holder.distance.setText(list.get(position).getCity()+"/"+list.get(position).getState()+"/"+list.get(position).getCountry());
        holder.timing.setText(list.get(position).getShop_timing());
        holder.type.setText(list.get(position).getShop_mob_no());
        holder.weburl.setText(list.get(position).getWebsite());

        Picasso.with(activity)
                .load("http://findashop.in/images/shop_profile/"+list.get(position).getShop_id()+"/"+list.get(position).getShop_pic())
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(holder.image);

        if(b==true) {
            holder.btn_Shop_delete.setVisibility(View.VISIBLE);
        }
        holder.btn_Shop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setMessage("Are you sure you want to delete your shop post? ");
                alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteShopServices();
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewShopList.setShop_id(list.get(position).getShop_id());
                viewShopList.setStrShop_name(list.get(position).getShop_name());
                viewShopList.setStrAddress(list.get(position).getAddress());
                viewShopList.setLatitude(Double.parseDouble(list.get(position).getShop_lat()));
                viewShopList.setLongitude(Double.parseDouble(list.get(position).getShop_long()));
                viewShopList.setStrCategory(list.get(position).getCategory_name());
                viewShopList.setStrSub_category(list.get(position).getSub_category_name());
                viewShopList.setStrWeburl(list.get(position).getWebsite());
                viewShopList.setStrMobile(list.get(position).getShop_mob_no());

                images.add(index++,list.get(position).getImage1());
                images.add(index++,list.get(position).getImage2());
                images.add(index++,list.get(position).getImage3());
                images.add(index++,list.get(position).getImage4());
                images.add(index++,list.get(position).getImage5());
                images.add(index++,list.get(position).getImage6());


                viewShopList.setArrayList(images);
                viewShopList.setStrShop_pic(list.get(position).getShop_pic());
                transition();
            }
        });

    }

    private void deleteShopServices() {
            Retrofit retrofit;
        DeleteShopModel deleteShopModel;
        deleteShopModel=new DeleteShopModel();
            retrofit = APIClient.getClient();
            RestInterface restInterface = retrofit.create(RestInterface.class);
            Call<DeleteShopModel> deleteShop= restInterface.deleteShop(viewShopList.getShop_id());
            deleteShop.enqueue(new Callback<DeleteShopModel>() {
                @Override
                public void onResponse(Call<DeleteShopModel> call, Response<DeleteShopModel> response) {
                    if (response.isSuccessful()){
                        String msg=  response.body().getMsg();
                        Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
                        activity.startActivity(new Intent(activity,AllPostsActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<DeleteShopModel> call, Throwable t) {

                }
            });
    }

    private void transition() {
        Log.d("Allpost", "transition");
        Intent intent = new Intent(activity, UserViewPostActivity.class);
        intent.putExtra("shop_list",viewShopList);
        Pair<View, String> p1 = Pair.create((View) viewPostLayout, "view");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, p1);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public void setFilter(ArrayList<ShopsListModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}