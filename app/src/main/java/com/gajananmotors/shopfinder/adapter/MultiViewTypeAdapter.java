package com.gajananmotors.shopfinder.adapter;

/**
 * Created by Gajanan Motars on 12/9/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.model.Model;

import java.util.ArrayList;

import static com.gajananmotors.shopfinder.common.CheckSetting.displayPromptForEnablingData;
import static com.gajananmotors.shopfinder.common.CheckSetting.isNetworkAvailable;
import static com.gajananmotors.shopfinder.common.GeoAddress.getAddress;

public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    private String mobNo;
    private String address;

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
                    break;
            }
        }
    }

    private void showDetails() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View confirmDialog = inflater.inflate(R.layout.dialog_shopdetails, null);
        final TextView tvShopName = confirmDialog.findViewById(R.id.tvShopName);
        final TextView tvMobile = confirmDialog.findViewById(R.id.tvMobile);
        final TextView tvAddress = confirmDialog.findViewById(R.id.tvAddress);
        final TextView tvUrlLink = confirmDialog.findViewById(R.id.tvUrlLink);

        ImageButton btnCancel = confirmDialog.findViewById(R.id.btnCancel);
        ImageButton btnMap = confirmDialog.findViewById(R.id.btnMap);
        ImageButton btnMsg = confirmDialog.findViewById(R.id.btnMessage);
        ImageButton btnCall = confirmDialog.findViewById(R.id.btncall);
        mobNo = tvMobile.getText().toString();

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
        });
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + mobNo)));
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable(mContext)) {
                    displayPromptForEnablingData((Activity) mContext);
                } else {
                    address = getAddress(18.50284, 73.9255587, mContext);
                   // Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
                    Log.d("MultiViewType","address"+address);
                    Uri gmmIntentUri = Uri.parse("geo:18.5590,73.7868?q=" + address);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    mContext.startActivity(mapIntent);
                }
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobNo));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mContext.startActivity(callIntent);
            }



        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}