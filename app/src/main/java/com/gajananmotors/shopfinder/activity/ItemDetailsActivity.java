package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.MultiViewTypeAdapter;
import com.gajananmotors.shopfinder.adapter.ShopsListAdpater;
import com.gajananmotors.shopfinder.common.SlideAnimationUtil;
import com.gajananmotors.shopfinder.model.Model;
import com.gajananmotors.shopfinder.model.ShopsList;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity{

    private ArrayList<ShopsList> shops_list = new ArrayList<>();
    private ShopsListAdpater adapter;
    private LinearLayout viewLayout;
    private LinearLayout shopDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewLayout = findViewById(R.id.viewLayout);

/*      ArrayList<Model> list = new ArrayList<>();
        list.add(new Model(Model.ADVERTISEMENT_TYPE, "Hello. This is the Text-only View Type. Nice to meet you", 0));
        list.add(new Model(Model.IMAGE_TYPE, "Hi. I display a cool image too besides the omnipresent TextView.", R.drawable.advertise));
        list.add(new Model(Model.IMAGE_TYPE, "Hi again. Another cool image here. Which one is better?", R.drawable.advertise));

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, this);*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        SlideAnimationUtil.slideInFromRight(getApplicationContext(), mRecyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.setAdapter(adapter);
        shops_list.add(new ShopsList("Gajanan Motors Pvt.Ltd.", "500.00 m", "Vinayak Residencey,near DMart,Baner", "Opens 24 Hours", "http:/www.informedevice.com", "Hotel", "9856237845"));
        shops_list.add(new ShopsList("Aloha Technology Pvt.Ltd.", "400.00 m", "2nd & 3rd Floor, Kumar Crystals, New D P Road, Opposite Converses, Aundh, Baner, Pune", "Opens 9Am-10PM", "http:/www.aloha.com", "IT", "7812345645"));
        shops_list.add(new ShopsList("Xoriant Technology", "200.00 m", "501-502, 5th Floor, Amar Paradigm, Baner Road, Near D-Mart, Baner, Pune", "Opens 9.30Am-6PM", "http:/www.xoriant.com", "IT", "8185868231"));
        shops_list.add(new ShopsList("Veritas Technology", "800.00 m", "East Middlefield Road Mountain View, CA 94043", "Opens 9.30Am-7PM", "http:/www.veritas.com", "Hospital", "9095969314"));
        shops_list.add(new ShopsList("MNM Solutions", "5.00 km", "UNIT no 802, Tower no. 7, SEZ Magarpatta city, Hadapsar, Pune, Maharashtra 411013", "Opens 24 Hours", "http:/www.mnm.com", "Business", "8794156568"));
        shops_list.add(new ShopsList("Infosys Solutions", "100.00 km", "UNIT no 802, Tower no. 7, SEZ Magarpatta city, Hadapsar, Pune, Maharashtra 411013", "Opens 24 Hours", "http:/www.infosys.com", "Business", "7856123245"));
        adapter = new ShopsListAdpater(this,viewLayout,shops_list);
        mRecyclerView.setAdapter(adapter);

       /* DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);*/


       /* ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(alphaAdapter);*/
    }


}

