package com.gajananmotors.shopfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.MultiViewTypeAdapter;
import com.gajananmotors.shopfinder.model.Model;
import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Model> list= new ArrayList<>();
        list.add(new Model(Model.ADVERTISEMENT_TYPE,"Hello. This is the Text-only View Type. Nice to meet you",0));
        list.add(new Model(Model.IMAGE_TYPE,"Hi. I display a cool image too besides the omnipresent TextView.",R.drawable.advertise));
        list.add(new Model(Model.IMAGE_TYPE,"Hi again. Another cool image here. Which one is better?",R.drawable.advertise));

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

    }
}
