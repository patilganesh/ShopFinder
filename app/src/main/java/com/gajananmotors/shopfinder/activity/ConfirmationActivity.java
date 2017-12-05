package com.gajananmotors.shopfinder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.gajananmotors.shopfinder.R;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnConfirm, btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView tvShopName = findViewById(R.id.tvShopName);
        TextView tvMobile = findViewById(R.id.tvMobile);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvArea = findViewById(R.id.tvArea);
        ImageView imgShopName = findViewById(R.id.imgShop);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnEdit = findViewById(R.id.btnEdit);
        btnConfirm.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                Toast.makeText(getApplicationContext(), "Edit page", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnConfirm:
                Toast.makeText(getApplicationContext(), "api call", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
