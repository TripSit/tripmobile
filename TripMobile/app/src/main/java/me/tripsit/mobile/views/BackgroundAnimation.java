package me.tripsit.mobile.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by alex on 10/07/16.
 */
public class BackgroundAnimation {

    Thread animation = null;

    int startColor = Color.TRANSPARENT;
    int endColor = Color.TRANSPARENT;

    long startTime = -1;
    long duration = 3000;

    List<View> viewsToAnimate = null;

    public BackgroundAnimation(List<View> viewsToAnimate) {
        this.viewsToAnimate = viewsToAnimate;
    }

    public void init(Context ctx, AttributeSet attrs) {
    }

    public synchronized void setBackgroundColor(int i) {
        setColorImmediate(i);

        // finish the rest off later

        /*if ( animation != null && animation.isAlive() && !animation.isInterrupted() ) {
            // Any pending animation should be told to stop, and the end background should be set.
            if ( endColor != -1 ) setColorImmediate(i);
            animation.interrupt();
        }
        // Start animating towards the end point provided from the current end point
        if ( i == endColor ) {
            // no change
            Log.e("BackgroundAnimation", "Ignoring due to new and end color being identical: " + i + " end: " + endColor);
            return;
        }
        startColor = endColor;
        endColor = i;
        startTime = System.currentTimeMillis();

        final Thread thisAnimation = new Thread(new Runnable() {
            @Override
            public void run() {
                /*while ( !thisAnimation.isInterrupted() ) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( animateColor() ) {
                        return;
                    }
                }
            }
        });
        thisAnimation.start();*/
    }

    // Return true if start time was a while ago
    private boolean animateColor() {
        if ( startTime+duration > System.currentTimeMillis() ) {
            setColorImmediate(100.0f, endColor);
            return true;
        } else {
            setColorImmediate(0.0f, Color.TRANSPARENT);
            return false;
        }
        //invalidate();
    }

    public void setColorImmediate(int colorImmediate) {
        for (View v : viewsToAnimate) {
            v.setBackgroundColor(colorImmediate);
        }
    }

    public void setColorImmediate(float progress, int colorImmediate) {
        setColorImmediate(colorImmediate);
    }
}
