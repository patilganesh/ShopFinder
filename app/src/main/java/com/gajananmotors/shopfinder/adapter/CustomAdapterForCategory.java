package com.gajananmotors.shopfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.MainActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by asus on 29-Nov-17.
 */

public class CustomAdapterForCategory extends RecyclerView.Adapter<CustomAdapterForCategory.MyViewHolder> {


    String[] name;
    Context context;
    String[] imageId;
    public ImageView images;

    private static LayoutInflater inflater = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;


        public MyViewHolder(View rowView) {
            super(rowView);

            text = rowView.findViewById(R.id.category_name);
            images = rowView.findViewById(R.id.category_imgs);

        }
    }

    public CustomAdapterForCategory(MainActivity mainActivity, String[] namelist, String[] imglist) {
        // TODO Auto-generated constructor stub
        this.name = namelist;
        this.imageId = imglist;
        context = mainActivity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_gridlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(name[position]);
        Picasso.with(context).load("http://192.168.2.3/event/images/category/"+imageId[position]).into(images);
      //  holder.images.setImageResource(imageId[position]);

    }


    @Override
    public int getItemCount() {
        return name.length;
    }

    @Override
    public int getItemViewType(int position) {

        //Toast.makeText(context, "You Clicked " + name[position], Toast.LENGTH_SHORT).show();

        return super.getItemViewType(position);
    }

}