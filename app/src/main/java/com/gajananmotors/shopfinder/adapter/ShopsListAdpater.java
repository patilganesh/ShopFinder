package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashwin on 1/1/2018.
 */

public class ShopsListAdpater extends RecyclerView.Adapter<ShopsListHolder>  implements Filterable{
    Activity activity;
    ArrayList<ShopsListModel> list = new ArrayList<>();
    private LinearLayout viewPostLayout;
    private ArrayList<String> images = new ArrayList<>();
    private int shop_id;
    private int index=0;
    public static ViewShopList viewShopList = new ViewShopList();
    private String name;
    private ArrayList<ShopsListModel> mFilteredList;
    public ShopsListAdpater(MapsActivity itemDetailsActivity, ArrayList<ShopsListModel> shops_list, String name) {
        this.list = shops_list;
        mFilteredList = shops_list;
        this.activity = itemDetailsActivity;
        this.name=name;
    }
    public ShopsListAdpater(ItemDetailsActivity activity, ArrayList<ShopsListModel> shops_list, String name) {

        this.activity = activity;
        this.list = shops_list;
        mFilteredList = shops_list;
        this.name=name;
    }
    public ShopsListAdpater(SearchActivity activity, ArrayList<ShopsListModel> shops_list, String name) {
        this.activity = activity;
        this.list = shops_list;
        mFilteredList = shops_list;
        this.name = name;
    }
    public ShopsListAdpater(AllPostsActivity allPostsActivity, ArrayList<ShopsListModel> shops_list, String name) {
        this.list = shops_list;
        mFilteredList = shops_list;
        this.activity = allPostsActivity;
        this.name=name;
    }
    @Override
    public ShopsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_shops_list, parent, false);
        return new ShopsListHolder(view);
    }
    @Override
    public void onBindViewHolder(final ShopsListHolder holder, final int position) {
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
                transition(position);
                //activity.finish();
                Log.e("updatedsize", String.valueOf(images.size()));
            }
        });
    }
    private void transition(int position) {
        Log.d("Allpost", "transition");
        Intent intent = new Intent(activity, ViewPostActivity.class);
        intent.putExtra("position",""+position);
        if(!name.equalsIgnoreCase("owner")) {
            intent.putExtra("shop_list", viewShopList);
        }
        intent.putExtra("owner",name);
        activity.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if(mFilteredList==null){
            return list.size();
        }
        else{
            return mFilteredList.size();
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

                    mFilteredList= list;
                } else {

                    ArrayList<ShopsListModel> filteredList = new ArrayList<>();

                    for (ShopsListModel s : list) {

                        if (s.getShop_name().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getArea().toLowerCase().contains(newText) || s.getSub_category_name().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText) || s.getState().toLowerCase().contains(newText) || s.getCountry().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getShop_timing().toLowerCase().contains(newText) || s.getWebsite().toLowerCase().contains(newText)){
                            filteredList.add(s);
                        }
//                        if (s.getShop_name().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getArea().toLowerCase().contains(newText) || s.getSub_category_name().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText) || s.getState().toLowerCase().contains(newText) || s.getCountry().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getShop_timing().toLowerCase().contains(newText) || s.getWebsite().toLowerCase().contains(newText))
//
//                            filteredList.add(s);
//                        if (s.getShop_name().toLowerCase().startsWith(newText) || s.getAddress().toLowerCase().startsWith(newText) || s.getAddress().toLowerCase().startsWith(newText) || s.getCity().toLowerCase().startsWith(newText) || s.getCategory_name().toLowerCase().startsWith(newText) || s.getShop_mob_no().toLowerCase().startsWith(newText))
//                            filteredList.add(s);
//                        else if (s.getShop_name().toLowerCase().endsWith(newText) || s.getAddress().toLowerCase().endsWith(newText) || s.getAddress().toLowerCase().endsWith(newText) || s.getCity().toLowerCase().endsWith(newText) || s.getCategory_name().toLowerCase().endsWith(newText) || s.getShop_mob_no().toLowerCase().endsWith(newText))
//                            filteredList.add(s);
//                        else if (s.getShop_name().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getAddress().toLowerCase().contains(newText) || s.getCity().toLowerCase().contains(newText) || s.getCategory_name().toLowerCase().contains(newText) || s.getShop_mob_no().toLowerCase().contains(newText))
//                            filteredList.add(s);
                    }
                    mFilteredList= filteredList;
                }

                //mFilteredList = list;
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList= (ArrayList<ShopsListModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}