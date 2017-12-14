package com.gajananmotors.shopfinder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;




import android.widget.TextView;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.model.Model;

import java.util.ArrayList;


public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;



    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = itemView.findViewById(R.id.type);
            this.image = itemView.findViewById(R.id.background);

        }

    }

    public static class AdvertisementTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        ImageView image;

        public AdvertisementTypeViewHolder(View itemView) {
            super(itemView);
            this.txtType = itemView.findViewById(R.id.type);
            this.image = itemView.findViewById(R.id.background);

        }

    }

    public MultiViewTypeAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.ADVERTISEMENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_type, parent, false);
                return new AdvertisementTypeViewHolder(view);
            case Model.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_type, parent, false);
                return new ImageTypeViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.ADVERTISEMENT_TYPE;
            case 1:
                return Model.IMAGE_TYPE;

            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.ADVERTISEMENT_TYPE:
                    ((AdvertisementTypeViewHolder) holder).txtType.setText(object.text);
                    ((AdvertisementTypeViewHolder) holder).image.setImageResource(object.data);
                    ((AdvertisementTypeViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            showDetails();
                        }
                    });
                    break;

                case Model.IMAGE_TYPE:
                    ((ImageTypeViewHolder) holder).txtType.setText(object.text);
                    ((ImageTypeViewHolder) holder).image.setImageResource(object.data);
                    ((ImageTypeViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            showDetails();
                        }
                    });
                    break;}
            }}

    private void showDetails() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View confirmDialog = inflater.inflate(R.layout.dialog_shopdetails, null);
        final TextView tvShopName = confirmDialog.findViewById(R.id.tvShopName);
        final TextView tvMobile = confirmDialog.findViewById(R.id.tvMobile);
        final TextView tvAddress = confirmDialog.findViewById(R.id.tvAddress);
        final TextView tvUrlLink = confirmDialog.findViewById(R.id.tvUrlLink);
        ImageButton btnCancel = confirmDialog.findViewById(R.id.btnCancel);
        ImageButton btncall = confirmDialog.findViewById(R.id.btncall);
        ImageButton btnMessage = confirmDialog.findViewById(R.id.btnMessage);
        ImageButton btnMap = confirmDialog.findViewById(R.id.btnMap);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Details");
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}