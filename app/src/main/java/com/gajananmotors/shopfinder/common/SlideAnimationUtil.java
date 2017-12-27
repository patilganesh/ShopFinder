package com.gajananmotors.shopfinder.common;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.gajananmotors.shopfinder.R;

/**
 * Created by asus on 23-Dec-17.
 */

public class SlideAnimationUtil {

    public static void slideInFromLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_from_left);
    }

    public static void slideOutToLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_to_left);
    }

    public static void slideInFromRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_from_right);
    }


    public static void slideOutToRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_to_right);
    }


    public static void slideUp(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_up);
    }

    private static void runSimpleAnimation(Context context, View view, int animationId) {
        view.startAnimation(AnimationUtils.loadAnimation(context, animationId));
    }

}