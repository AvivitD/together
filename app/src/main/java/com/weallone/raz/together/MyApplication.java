package com.weallone.raz.together;

/**
 * Created by Raz on 5/13/2017.
 */
import android.app.Application;
import android.content.Context;

/**
 * Handles app crashes report.
 */
public class MyApplication extends Application {
    /**
     * Standard attachBaseContext method
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}