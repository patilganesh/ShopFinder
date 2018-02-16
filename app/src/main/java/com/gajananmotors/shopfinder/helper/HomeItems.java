package com.gajananmotors.shopfinder.helper;

import java.util.ArrayList;


public class HomeItems {

    private String sectionLabel;
    private ArrayList<String> itemArrayList;

    public HomeItems(String sectionLabel, ArrayList<String> itemArrayList) {
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<String> getItemArrayList() {
        return itemArrayList;
    }
}
