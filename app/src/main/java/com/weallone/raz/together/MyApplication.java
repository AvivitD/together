package com.weallone.raz.together;

/**
 * Created by Raz on 5/13/2017.
 */
import android.app.Application;
import android.content.Context;

import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(mailTo = "razronen1@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
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


        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}