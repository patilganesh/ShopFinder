package com.gajananmotors.shopfinder.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.model.CropingOptionModel;

import java.util.ArrayList;


public class CropingOptionAdapter extends ArrayAdapter {
    private ArrayList mOptions;
    private LayoutInflater mInflater;

    public CropingOptionAdapter(Context context, ArrayList options) {
        super(context, R.layout.croping_selector, options);

        mOptions = options;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.croping_selector, null);

        CropingOptionModel item = (CropingOptionModel) mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(R.id.img_icon)).setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(R.id.txt_name)).setText(item.title);

            return convertView;
        }

        return null;
    }
}
