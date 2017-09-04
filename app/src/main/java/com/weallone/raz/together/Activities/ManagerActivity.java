package com.weallone.raz.together.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the activity of a manager user.
 * It contains 2 options: Mangment and App.
 */
public class ManagerActivity extends MyActionBarActivity implements Serializable{
    private Map<Integer,Button> btns = new HashMap<>();

    /**
     * Standard OnCreate method- Attaching UI.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        InitUI();
    }

    /**
     * Attaching UI and defineing Click listeners.
     */
    private void InitUI() {
        btns.put(0, (Button)findViewById(R.id.manager_managment_button));
        btns.put(1, (Button) findViewById(R.id.manager_app_button));

        btns.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, ManagmentActivity.class));
            }
        });
        btns.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, ManagerApp.class));
            }
        });
    }
}
