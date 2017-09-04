package com.weallone.raz.together.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.R;
import com.google.gson.Gson;

/**
 * This activity is used to decided which activity(Login,Regist,Child,Psycho,Manager) to run.
 */
public class SplashActivity extends MyActionBarActivity {

    private static final String TAG = "SplashActivity";
    // Manages key valued pairs associated with stock symbols
    private SharedPreferences settings;

    /**
     * Standard onCreate Method. Awaits 1000secinds - option if video or prompt images is wanted.
     * Then starting the corret user activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSharedPrefences();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StartUserActivity();
    }

    /**
     * Init shared prefences.
     */
    private void initSharedPrefences(){
        settings = getSharedPreferences(getResources().getString(R.string.data),MODE_PRIVATE);
        settings = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
    }

    /**
     * Starting the correct activity.
     * By pulling user object from memory and decided which one is siutable.
     */
    private void StartUserActivity(){
        Gson gson = new Gson();
        String json = settings.getString(getResources().getString(R.string.key_user), "");
        UserInfo user = gson.fromJson(json, UserInfo.class);
        Log.d(TAG, "LOAD USER: " + ((user==null)?"null":user.toString()));
        String activityType = null;
        if(user==null || json.equals("")){
            activityType = "";
        } else {
            activityType = user.getAccount().toString();
        }
        if(activityType.equals(getResources().getString(R.string.child_acount)))
            startActivity(new Intent(SplashActivity.this, ChildActivity.class));
        else if(activityType.equals(getResources().getString(R.string.psycho_acount)))
            startActivity(new Intent(SplashActivity.this, PsychoActivity.class));
        else if(activityType.equals(getResources().getString(R.string.manager_acount)))
            startActivity(new Intent(SplashActivity.this, ManagerActivity.class));
        else if(isApproved())
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
        else
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    }

    /**
     * Checks if this device Id is approved - perhaps registeration activity is needed.
     * @return
     */
    private Boolean isApproved() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
        if(settings.contains(getResources().getString(R.string.key_user_approve))){
            return true;
        } else {
            return false;
        }
    }
}
