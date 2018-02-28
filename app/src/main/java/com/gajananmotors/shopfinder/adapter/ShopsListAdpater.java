package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.AllPostsActivity;
import com.gajananmotors.shopfinder.activity.ItemDetailsActivity;
import com.gajananmotors.shopfinder.activity.MapsActivity;
import com.gajananmotors.shopfinder.activity.SearchActivity;
import com.gajananmotors.shopfinder.activity.ViewPostActivity;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.holder.ShopsListHolder;
import com.gajananmotors.shopfinder.model.GoogleShopList;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
import static com.gajananmotors.shopfinder.common.GeoAddress.getAddress;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListHolder> implements Filterable {
    ArrayList<GoogleShopList> google_list = new ArrayList<>();
    ArrayList<String> timingList;
    ArrayList<String> nameList;
    ArrayList<String> latList;
    ArrayList<String> longList;
    ArrayList<String> addressList;
    ArrayList<String> iconList;
    Activity activity;
    ArrayList<ShopsListModel> list = new ArrayList<>();

    private LinearLayout viewPostLayout;
    private ArrayList<String> images = new ArrayList<>();
    private int shop_id;
    private int index = 0;
    ViewShopList viewShopList = new ViewShopList();
    private String name;
    private ArrayList<ShopsListModel> mFilteredList;
    private double lat, lng;


    public ShopsListAdpater(MapsActivity itemDetailsActivity, ArrayList<ShopsListModel> shops_list, String name) {
        this.list = shops_list;
        mFilteredList = shops_list;
        this.activity = itemDetailsActivity;
        this.name = name;
    }

    public ShopsListAdpater(ItemDetailsActivity activity, ArrayList<ShopsListModel> shops_list, String name) {

        this.activity = activity;
        this.list = shops_list;
        mFilteredList = shops_list;
        this.name = name;
    }

    public ShopsListAdpater(SearchActivity activity, ArrayList<ShopsListModel> shops_list, String name) {
        this.activity = activity;
        this.list = shops_list;
        mFilteredList = shops_list;
        this.name = name;
    }


    public ShopsListAdpater(MapsActivity mapsActivity, String googleData, ArrayList<GoogleShopList> google_shops_list) {
        this.activity = mapsActivity;
        this.name = googleData;
        this.google_list = google_shops_list;
    }

    public ShopsListAdpater(AllPostsActivity allPostsActivity, ArrayList<ShopsListModel> shops_list, String name) {
        this.activity = allPostsActivity;
        this.list = shops_list;
        mFilteredList = shops_list;
        this.name = name;
    }

    @Override
    public ShopsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_shops_list, parent, false);
        return new ShopsListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopsListHolder holder, final int position) {
        if (name.equalsIgnoreCase("GoogleData")) {

            holder.lDirectionLayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.weburl.setVisibility(View.GONE);
            holder.name.setText(google_list.get(position).getShopName());
            holder.address.setText(google_list.get(position).getShopAddress());
            holder.timing.setText(google_list.get(position).getShopOpeningHours());
            holder.lDirectionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lattitude = google_list.get(position).getShopLat();
                    String longitude = google_list.get(position).getShopLong();
                    if (!lattitude.isEmpty() && !longitude.isEmpty()) {
                        lat = Double.parseDouble(lattitude);
                        lng = Double.parseDouble(longitude);
                    }

                    if (!isNetworkAvailable(activity)) {
                        displayPromptForEnablingData(activity);
                    } else {
                        String shopName=holder.name.getText().toString();
                        String uriBegin = "geo:" + viewShopList.getLatitude() + "," + viewShopList.getLongitude();
                        String query = viewShopList.getLatitude() + "," + viewShopList.getLongitude() + "(" + name + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        String address = getAddress(lat, lng, activity);
                        Log.d("MultiViewType", "address" + address);
                      Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng +"?q=" +shopName+"+" +address);
                       // Uri gmmIntentUri = Uri.parse("geo:navigation" + 0 + "," + 0 + "?q=" + address);
                        Log.d("uriIntent","gmmIntentUri"+gmmIntentUri);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        activity.startActivity(mapIntent);
                    }
                }
            });

          /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
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

                }
            });*/
        } else {

            holder.lDirectionLayout.setVisibility(View.GONE);
            holder.name.setText(mFilteredList.get(position).getShop_name());
            holder.address.setText(mFilteredList.get(position).getAddress());
            holder.distance.setText(mFilteredList.get(position).getCity() + "/" + mFilteredList.get(position).getState() + "/" + mFilteredList.get(position).getCountry());
            holder.timing.setText(mFilteredList.get(position).getShop_timing());
            holder.type.setText(mFilteredList.get(position).getShop_mob_no());
            holder.weburl.setText(mFilteredList.get(position).getWebsite());
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
    }

    private void transition() {
        Log.d("Allpost", "transition");
        Intent intent = new Intent(activity, ViewPostActivity.class);
        intent.putExtra("shop_list", viewShopList);
        intent.putExtra("owner", name);
        activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (name.equalsIgnoreCase("GoogleData")) {
            return google_list.size();
        } else {
            if (mFilteredList == null) {
                return list.size();
            } else {

                return mFilteredList.size();
            }
        }


    }

    public void setFilter(ArrayList<ShopsListModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String newText = charSequence.toString();

                if (newText.isEmpty()) {

                    mFilteredList = list;
                } else {

                    ArrayList<ShopsListModel> filteredList = new ArrayList<>();

                    for (ShopsListModel s : list) {

                        if (s.getShop_name().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getArea().toLowerCase().contains(newText) || s.getSub_category_name().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText) || s.getState().toLowerCase().contains(newText) || s.getCountry().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getShop_timing().toLowerCase().contains(newText) || s.getWebsite().toLowerCase().contains(newText)) {
                            filteredList.add(s);
                        }
                    }
                    mFilteredList = filteredList;
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<ShopsListModel>) results.values;
                notifyDataSetChanged();
            }


        };
    }
}