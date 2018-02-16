package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.ItemDetailsActivity;
import com.gajananmotors.shopfinder.activity.SubCategoryActivity;

import java.util.ArrayList;

/**
 * Created by asus on 29-Nov-17.
 */
public class CustomAdapterForSubCategoryAdapter extends RecyclerView.Adapter<CustomAdapterForSubCategoryAdapter.MyViewHolder> {

    private ArrayList<String> subCategoryNames = new ArrayList<>();
    private ArrayList<String> subCategoryImages = new ArrayList<>();
    private ArrayList<Integer> subCatIds = new ArrayList<>();
    private int catId,subCatId;
   // private String[] name;
    private Context context;
    private int[] imageId;
    private String name;

  /*  public CustomAdapterForSubCategoryAdapter(Activity subCategory, String[] nameList, int[] imglist) {
        this.name = nameList;
        this.imageId = imglist;
        context = subCategory;
    }
*/
    public CustomAdapterForSubCategoryAdapter(SubCategoryActivity subCategoryActivity, ArrayList<String> subCategoryNames, ArrayList<String> subCategoryImages, int int_cat_id, ArrayList<Integer> subCatId, String name) {
        context = subCategoryActivity;
        Log.d("CustomAdapter","called");
        this.subCategoryNames = subCategoryNames;
        this.subCategoryImages = subCategoryImages;
        this.catId = int_cat_id;
        this.subCatIds = subCatId;
        this.name=name;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView images;


        public MyViewHolder(View rowView) {
            super(rowView);
            text = rowView.findViewById(R.id.txtsubcategory);
            images = rowView.findViewById(R.id.imgsubcategory);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // holder.text.setText(name[position]);
        holder.text.setText(subCategoryNames.get(position).toString());
        /*holder.images.setImageResource(imageId[position]);
        String imagePath= APIClient.getImagePath()+imageList.get(position);
        Glide.with(context)
                .load(imagePath)
                .fitCenter()
                .centerCrop()
                .into(holder.images);*/
        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCatId=subCatIds.get(position);
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra("CategoryId",catId );
                intent.putExtra("Sub_CategoryId",subCatId);
                intent.putExtra("owner",name);
                context.startActivity(intent);
            }
        });
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCatId=subCatIds.get(position);
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra("CategoryId",catId);
                intent.putExtra("Sub_CategoryId",subCatId);
                intent.putExtra("owner",name);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoryNames.size();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}