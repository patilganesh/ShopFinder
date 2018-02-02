package com.gajananmotors.shopfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.MainActivity;
import com.gajananmotors.shopfinder.activity.SubCategoryActivity;
import com.gajananmotors.shopfinder.helper.CircleImageView;

import java.util.ArrayList;

public class CustomAdapterForVerticalGridViewAdapter extends RecyclerView.Adapter<CustomAdapterForVerticalGridViewAdapter.MyViewHolder> {
    private String[] name;
    private Context context;
    private int[] imageId;
    private ArrayList<String> imageList;
    private ArrayList<String> namesList;
    private ArrayList<Integer> categoryId;

    public CustomAdapterForVerticalGridViewAdapter(MainActivity mainActivity, String[] nameList, int[] imglist) {
        this.name = nameList;
        this.imageId = imglist;
        this.context = mainActivity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public CircleImageView images;

        public MyViewHolder(View rowView) {
            super(rowView);
            text = rowView.findViewById(R.id.category_name_vertical);
            images = rowView.findViewById(R.id.category_imgs_vertical);
            // setFadeAnimation(images,200);
            //  setFadeAnimation(text,200);
        }
    }

    public CustomAdapterForVerticalGridViewAdapter(MainActivity mainActivity, ArrayList<String> namesList, ArrayList<String> imagesList, ArrayList<Integer> categoryId) {

        this.namesList = namesList;
        this.imageList = imagesList;
        this.categoryId = categoryId;
        this.context = mainActivity;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_grid_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // holder.text.setText(name[position]);
        holder.text.setText(namesList.get(position));
        // holder.text.setText(namesList.get(position));
        /*String imagePath= APIClient.getImagePath()+imageList.get(position);
        Glide.with(context)
                .load(imagePath)
                .fitCenter()
                .centerCrop()
                .into(holder.images);*/
        // holder.images.setImageResource(imageId[position]);
        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("CategoryId", categoryId.get(position).intValue());
                context.startActivity(intent);

                //  context.startActivity(new Intent(context, SubCategoryActivity.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        return namesList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }
}