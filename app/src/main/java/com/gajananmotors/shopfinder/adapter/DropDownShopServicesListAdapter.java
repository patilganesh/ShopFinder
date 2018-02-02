package com.gajananmotors.shopfinder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.AddPostActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 23-Jan-18.
 */

public class DropDownShopServicesListAdapter extends BaseAdapter {

    private ArrayList<String> mListItems;
    private LayoutInflater mInflater;
    private TextView mSelectedItems;
    private static int selectedCount = 0;
    private static String firstSelected = "";
    private ViewHolder holder;
    private static String selected = "";    //shortened selected values representation
    private String servicesCommaSeparated;
    private List selectedList;

    public static String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        DropDownShopServicesListAdapter.selected = selected;
    }

    public DropDownShopServicesListAdapter(Context context, ArrayList<String> items,
                                           TextView tv) {
        mListItems = new ArrayList<>();
        mListItems.addAll(items);
        mInflater = LayoutInflater.from(context);
        mSelectedItems = tv;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drop_down_list_row_shop_services, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.SelectOption);
            holder.chkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(mListItems.get(position));

        final int position1 = position;

        //whenever the checkbox is clicked the selected values textview is updated with new selected values
        holder.chkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setText(position1);
            }
        });

        if (AddPostActivity.checkSelected[position])
            holder.chkbox.setChecked(true);
        else
            holder.chkbox.setChecked(false);
        return convertView;
    }


    /*
     * Function which updates the selected values display and information(checkSelected[])
     * */
    private void setText(int position1) {
        if (!AddPostActivity.checkSelected[position1]) {
            AddPostActivity.checkSelected[position1] = true;
            selectedCount++;
        } else {
            AddPostActivity.checkSelected[position1] = false;
            selectedCount--;
        }
        selectedList = new ArrayList();
        if (selectedCount == 0) {

            mSelectedItems.setText(R.string.select_string);
        } else {

            for (int i = 0; i < AddPostActivity.checkSelected.length; i++) {
                if (AddPostActivity.checkSelected[i] == true) {
                    firstSelected = mListItems.get(i);
                    selectedList.add(firstSelected);
                }
            }
            if (selectedList != null) {

                servicesCommaSeparated = TextUtils.join(", ", selectedList);
            }

            mSelectedItems.setText(servicesCommaSeparated);
            setSelected(servicesCommaSeparated);

        }
    }

    private class ViewHolder {
        TextView tv;
        CheckBox chkbox;
    }
}
