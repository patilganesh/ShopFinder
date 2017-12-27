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
import com.gajananmotors.shopfinder.activity.SubCategory;
import com.gajananmotors.shopfinder.helper.CircleImageView;


public class CustomAdapterForVerticalGridView extends RecyclerView.Adapter<CustomAdapterForVerticalGridView.MyViewHolder> {

    String[] name;
    Context context;
    int[] imageId;

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
    public CustomAdapterForVerticalGridView(MainActivity mainActivity, String[] namelist, int[] imglist) {
        this.name = namelist;
        this.imageId = imglist;
        context = mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_grid_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(name[position]);
        holder.images.setImageResource(imageId[position]);

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent=new Intent(context, SubCategory.class);
                    context.startActivity( intent,
                            ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());
                }*/

            context.startActivity(new Intent(context, SubCategory.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return name.length;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}