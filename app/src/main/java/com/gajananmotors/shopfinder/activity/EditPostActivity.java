package com.gajananmotors.shopfinder.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Toast;
import com.gajananmotors.shopfinder.R;
public class EditPostActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private int shop_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shop_id = getIntent().getIntExtra("shop_id", 00000);
        Toast.makeText(this, "Shop_id:" + shop_id, Toast.LENGTH_LONG).show();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
