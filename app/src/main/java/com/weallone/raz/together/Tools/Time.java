package com.weallone.raz.together.Tools;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.weallone.raz.together.R;

/**
 * Time tool. enable setting time for textview.
 */
public class Time {

    /**
     * Setting time to text view
     * @param activity
     * @param view - to attach to
     * @param toWhen - time
     * @param published - extra string
     * @param before - time
     * @param after - extra string.
     */
    public static void setTime(Activity activity, TextView view,long toWhen, String published, String before,String after){
        try {
            long diff = toWhen - Long.parseLong(published);
            int seconds = (int) (diff / 1000);
            int mins = (int) (diff / (1000 * 60));
            int hours = (int) (diff / (1000 * 60 * 60));
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            int weeks = (int) (diff / (1000 * 60 * 60 * 24 * 7));
            int months = (int) (diff / (1000 * 60 * 60 * 24 * 7 * 4));
            int years = (int) (diff / (1000 * 60 * 60 * 24 * 7 * 4 * 12));
            if (seconds >= 0)
                view.setText(before + " " + String.valueOf(seconds) + " " + activity.getResources().getString(R.string.seconds) + " " + after);
            if (mins > 0)
                view.setText(before + " " + String.valueOf(mins) + " " + activity.getResources().getString(R.string.mins) + " " + after);
            if (hours > 0)
                view.setText(before + " " + String.valueOf(hours) + " " + activity.getResources().getString(R.string.hours) + " " + after);
            if (days > 0)
                view.setText(before + " " + String.valueOf(days) + " " + activity.getResources().getString(R.string.days) + " " + after);
            if (weeks > 0)
                view.setText(before + " " + String.valueOf(weeks) + " " + activity.getResources().getString(R.string.weeks) + " " + after);
            if (months > 0)
                view.setText(before + " " + String.valueOf(months) + " " + activity.getResources().getString(R.string.months) + " " + after);
            if (years > 0)
                view.setText(before + " " + String.valueOf(years) + " " + activity.getResources().getString(R.string.years) + " " + after);
        }catch (NumberFormatException e ){
            Log.d("Time","NumberFormatException");
        }
    }

}
