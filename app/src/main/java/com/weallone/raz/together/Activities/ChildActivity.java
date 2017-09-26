package com.weallone.raz.together.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.Fragments.Chat;
import com.weallone.raz.together.Fragments.Posts;
import com.weallone.raz.together.Fragments.Quotes;
import com.weallone.raz.together.Fragments.Tabs3_Container;
import com.weallone.raz.together.Interfaces.FragmentsCreator;
import com.weallone.raz.together.R;

import java.io.Serializable;
import java.util.Map;

/**
 * This class represents the activity of a child user.
 * It contains 3 main fragments - chat, how do you feel, posts.
 */
public class ChildActivity extends MyActionBarActivity implements FragmentsCreator, Serializable{

    /**
     * Standard Oncreate Method.
     * Calling setFragment to set the page addapter.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        setFragment();
    }

    /**
     * Settings the main fragment of this activity as 3Tab container fragment
     * so it withold the chat,quote, posts fragments.
     */
    private void setFragment(){
        Fragment container = Tabs3_Container.newInstance(getResources().getString(R.string.fragment_creator_key),
                (FragmentsCreator) this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.child_cfp_coontainer,container);
        fragmentTransaction.commit();
    }

    /**
     * Calling the corret fragment instance foor each position
     * @param position - in 3Tab container.
     * @return - corret fragment.
     */
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return Quotes.newInstance(this);
            case 1:
                return Chat.newInstance(this);
            case 2:
                return Posts.newInstance(this);
            default:
                return new Chat();
        }
    }

    /**
     * Setting the labels of the 3Tab container.
     * @param labels - the mapping to the three tabs fragment.
     */
    @Override
    public void putFragmentsNames(Map<Integer, TextView> labels) {
        labels.get(0).setText(getResources().getString(R.string.support_label));
        labels.get(1).setText(getResources().getString(R.string.chat_label));
        labels.get(2).setText(getResources().getString(R.string.posts_label));
    }
}
