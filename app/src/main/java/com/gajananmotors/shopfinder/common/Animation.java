package com.gajananmotors.shopfinder.common;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by asus on 21-Dec-17.
 */

public class Animation {
    public static void setFadeAnimation(View view, long duration) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(duration);
        view.startAnimation(anim);
    }
}
