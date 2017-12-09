package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;

public class SubCategory extends AppCompatActivity {

    private TextView tvHospital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        tvHospital=findViewById(R.id.tvHospital);
        tvHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategory.this,ItemDetailsActivity.class));
            }
        });
    }
}
