package com.gajananmotors.shopfinder.helper;


import com.gajananmotors.shopfinder.model.SubCategoryModel;

import java.util.ArrayList;

public class HomeItems {

    private String sectionLabel;
    private int id;
    private ArrayList<SubCategoryModel> itemArrayList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HomeItems(int id, String sectionLabel, ArrayList<SubCategoryModel> itemArrayList) {
        this.id = id;
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }
    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<SubCategoryModel> getItemArrayList() {
        return itemArrayList;
    }
}
