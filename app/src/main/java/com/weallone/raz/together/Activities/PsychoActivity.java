package com.weallone.raz.together.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.Fragments.Calender;
import com.weallone.raz.together.Fragments.Chat;
import com.weallone.raz.together.Fragments.Posts;
import com.weallone.raz.together.Fragments.Tabs3_Container;
import com.weallone.raz.together.Interfaces.FragmentsCreator;
import com.weallone.raz.together.R;

import java.io.Serializable;
import java.util.Map;

/**
 * This class represents the activity of a psychologist user.
 * It contains 3 main fragments - chat, calendar, posts.
 */
public class PsychoActivity extends MyActionBarActivity implements FragmentsCreator, Serializable{

    private transient Tabs3_Container container = null;

    /**
     * Standard Oncreate Method.
     * Calling setFragment to set the page addapter.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psycho);
        setFragment();
    }

    /**
     * setting the chat titles.
     * @param num 0 - chat fragment position
     * @param id  - of conversation
     * @param name - of peer.
     */
    @Override
    public void setChatImmediateCall(String num, String id, String name){
        container.setChatImmediatCall(num,id,name);
        setConversation(id);
    }

    /**
     * Attaching the fragment with 3tab container fragment.
     */
    private void setFragment(){
        container = Tabs3_Container.newInstance(getResources().getString(R.string.fragment_creator_key),
                (FragmentsCreator) this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.psycho_cfp_coontainer, container);
        fragmentTransaction.commit();
    }

    /**
     * Returning the corret fragment by the position of 3Tab container.
     * @param position - inside the 3Tab container.
     * @return the correct fragment.
     */
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return Chat.newInstance(this);
            case 1:
                return Calender.newInstance(this, false);
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
        labels.get(0).setText(getResources().getString(R.string.chat_label));
        labels.get(1).setText(getResources().getString(R.string.calender_label));
        labels.get(2).setText(getResources().getString(R.string.posts_label));
    }
}
