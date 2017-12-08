package com.gajananmotors.shopfinder.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gajananmotors.shopfinder.R;

public class SplashActivity extends AppCompatActivity {
    TextView txtscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_splash);
        txtscreen = findViewById(R.id.txtscreen);
        getSupportActionBar().hide();
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);

        Thread thread = new Thread() {
            public void run() {
                try {
                    txtscreen.startAnimation(animation);
                    //animation.start();
                    Thread.sleep(2500);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(getApplicationContext(), MainActivity.class));
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

}

