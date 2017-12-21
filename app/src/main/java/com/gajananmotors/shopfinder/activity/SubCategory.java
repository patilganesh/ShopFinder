package com.gajananmotors.shopfinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.helper.CircleImageView;

public class SubCategory extends AppCompatActivity {

    private TextView tvHospital;
    private CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvHospital=findViewById(R.id.tvHospital);
        imageView=findViewById(R.id.imgHospital);
        tvHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategory.this,ItemDetailsActivity.class));
               // Toast.makeText(SubCategory.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }); imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategory.this,ItemDetailsActivity.class));
            }
        });
    }
}
