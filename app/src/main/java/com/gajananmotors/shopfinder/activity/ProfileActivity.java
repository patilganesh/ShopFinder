package com.gajananmotors.shopfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gajananmotors.shopfinder.R;



public class ProfileActivity extends AppCompatActivity {

    EditText etName,etEmail,etMobile,etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etMobile=findViewById(R.id.etMobile);
        etDate=findViewById(R.id.etDate);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageButton imgProfile = findViewById(R.id.imgProfile);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
