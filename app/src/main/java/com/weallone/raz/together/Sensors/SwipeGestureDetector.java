package com.weallone.raz.together.Sensors;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.weallone.raz.together.Interfaces.Callback;

/**
 * Created by Raz on 1/28/2017.
 */
public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private Callback cb;

    /**
     * Constructor
     * @param cb the listener to notify when swipe occur.
     */
    public SwipeGestureDetector(Callback cb, String duration){ this.cb = cb; LOGTAG = duration;}

    private static String LOGTAG;

    /**
     * When swipe occur, calles callback if its oriantion is down.
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        if(e1 ==null || e2==null) return false;
        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                if(this.cb!=null){
                    this.cb.Callback();
                }
                return true;
            case 4:
                return true;
        }
        return false;
    }

    /**
     * Calculate the degree of swipe.
     * @param x1 for calc
     * @param y1 for calc
     * @param x2 for calc
     * @param y2 for calc
     * @return
     */
    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle >= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;
    }
}