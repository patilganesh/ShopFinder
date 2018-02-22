package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.AllPostsActivity;
import com.gajananmotors.shopfinder.activity.ItemDetailsActivity;
import com.gajananmotors.shopfinder.activity.MapsActivity;
import com.gajananmotors.shopfinder.activity.SearchActivity;
import com.gajananmotors.shopfinder.activity.ViewPostActivity;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.holder.ShopsListHolder;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListHolder> {
    Activity activity;
    ArrayList<ShopsListModel> list = new ArrayList<>();
    private LinearLayout viewPostLayout;
    private ArrayList<String> images = new ArrayList<>();
    private int shop_id;
    private int index=0;
    ViewShopList viewShopList = new ViewShopList();
    private String name;


    public ShopsListAdpater(AllPostsActivity itemDetailsActivity, ArrayList<ShopsListModel> shops_list, String name) {
        this.list = shops_list;
        this.activity = itemDetailsActivity;
        this.name=name;
    }

    public ShopsListAdpater(ItemDetailsActivity activity, ArrayList<ShopsListModel> shops_list, String name) {

        this.activity = activity;
        this.list = shops_list;
        this.name=name;
    }

    public ShopsListAdpater(SearchActivity activity, ArrayList<ShopsListModel> shops_list, String name) {

        this.activity = activity;
        this.list = shops_list;
        this.name = name;
    }

    public ShopsListAdpater(MapsActivity mapsActivity, ArrayList<ShopsListModel> shops_list) {
        this.activity = mapsActivity;
        this.list = shops_list;
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
        holder.distance.setText(list.get(position).getCity() + "/" + list.get(position).getState() + "/" + list.get(position).getCountry());
        holder.timing.setText(list.get(position).getShop_timing());
        holder.type.setText(list.get(position).getShop_mob_no());
        holder.weburl.setText(list.get(position).getWebsite());
        Picasso.with(activity)
                .load("http://findashop.in/images/shop_profile/" + list.get(position).getShop_id() + "/" + list.get(position).getShop_pic())
                .fit()
                .placeholder(R.drawable.background_splashscreen)
                .into(holder.image);
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
                viewShopList.setStrShopTime(list.get(position).getShop_timing());
                viewShopList.setStrservices(list.get(position).getServices());
                images.clear();
                index = 0;
                if (list.get(position).getImage1() != null) {
                    images.add(index++, list.get(position).getImage1());
                }
                if (list.get(position).getImage2() != null) {
                    images.add(index++, list.get(position).getImage2());
                }
                if (list.get(position).getImage3() != null) {
                    images.add(index++, list.get(position).getImage3());
                }
                if (list.get(position).getImage4() != null) {
                    images.add(index++, list.get(position).getImage4());
                }
                if (list.get(position).getImage5() != null) {
                    images.add(index++, list.get(position).getImage5());
                }
                if (list.get(position).getImage6() != null) {
                    images.add(index++, list.get(position).getImage6());
                }
                viewShopList.setArrayList(images);
                viewShopList.setStrShop_pic(list.get(position).getShop_pic());
                transition();
                //activity.finish();
                Log.e("updatedsize", String.valueOf(images.size()));
            }
        });


    }
    private void transition() {
        Log.d("Allpost", "transition");
        Intent intent = new Intent(activity, ViewPostActivity.class);
        intent.putExtra("shop_list", viewShopList);
        intent.putExtra("owner",name);
        activity.startActivity(intent);
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