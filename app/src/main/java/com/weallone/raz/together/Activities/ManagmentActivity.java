package com.weallone.raz.together.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.Fragments.Quotes;
import com.weallone.raz.together.Fragments.Statistics;
import com.weallone.raz.together.Fragments.Tabs3_Container;
import com.weallone.raz.together.Fragments.Updates;
import com.weallone.raz.together.Interfaces.FragmentsCreator;
import com.weallone.raz.together.R;

import java.io.Serializable;
import java.util.Map;

/**
 * This class represents the activity of a manager user - MANAGMENT option.
 * It contains 3 main fragments - statistics, quotes, post.
 */
public class ManagmentActivity extends MyActionBarActivity implements FragmentsCreator, Serializable {

    /**
     * Standarad onCreate method.
     * Creating Set fragment to set the 3TabContainer.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managment);
        setFragment();
    }

    /**
     * Attaching the fragment exists in activity to the 3Tab-Container fragment.
     */
    private void setFragment(){
        Fragment container = Tabs3_Container.newInstance(getResources().getString(R.string.fragment_creator_key),
                (FragmentsCreator) this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.manager_managment_cfp_coontainer, container);
        fragmentTransaction.commit();
    }

    /**
     * Returning correct fragment for each tab position.
     * @param position - in 3Tab container.
     * @return - the fragment object.
     */
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return Statistics.newInstance(this);
            case 1:
                return Quotes.newInstance(this);
            case 2:
                return Updates.newInstance(this);
            default:
                return Statistics.newInstance(this);
        }
    }

    /**
     * Setting the title of 3Tab container title
     * @param labels - the mapping between the Textview object and position in 3tab.
     */
    @Override
    public void putFragmentsNames(Map<Integer, TextView> labels) {
        labels.get(0).setText(getResources().getString(R.string.managment_fragment_label));
        labels.get(1).setText(getResources().getString(R.string.solutions_fragment_label));
        labels.get(2).setText(getResources().getString(R.string.update_fragment_label));
    }
}
