package com.gajananmotors.shopfinder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gajananmotors.shopfinder.R;

public class EditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
