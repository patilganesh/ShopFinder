package com.gajananmotors.shopfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.ItemDetailsActivity;
import com.gajananmotors.shopfinder.activity.ProfileActivity;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.SubCategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by sonu on 24/07/17.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemLabel;
        private CardView card_view_home_page;
        private ImageView subcategoryimage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = itemView.findViewById(R.id.item_label);
            card_view_home_page = itemView.findViewById(R.id.card_view_home_page);
            subcategoryimage = itemView.findViewById(R.id.subcategoryimage);
        }
    }
    private Context context;
    private ArrayList<SubCategoryModel> arrayList;
    private int cat_id;

    public ItemRecyclerViewAdapter(Context context, int id, ArrayList<SubCategoryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.cat_id = id;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_layout, parent, false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.itemLabel.setText(arrayList.get(position).getName());
      /*  Picasso.with(context)
                .load(arrayList.get(position).getImage())
                .fit()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(holder.subcategoryimage);

*/
        holder.card_view_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra("CategoryId", arrayList.get(position).getCategory_id());
                intent.putExtra("Sub_CategoryId", arrayList.get(position).getSub_category_id());
                intent.putExtra("owner", "user");
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}