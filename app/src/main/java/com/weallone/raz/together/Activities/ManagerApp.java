package com.weallone.raz.together.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
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
 * This class represents the activity of a manager user - APP option.
 * It contains 3 main fragments - chat, how do you feel, posts.
 */
public class ManagerApp extends MyActionBarActivity implements FragmentsCreator, Serializable{

    private transient Tabs3_Container container = null;

    /**
     * Standard on create method. setting fragment.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_app);

        setFragment();
    }

    /**
     * Attaching fragment with the 3-TabContainer fragment -
     * It withold the chat,calendar, post fragments.
     */
    private void setFragment(){
        container = Tabs3_Container.newInstance(getResources().getString(R.string.fragment_creator_key),
                (FragmentsCreator) this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.manager_app_cfp_coontainer, container);
        fragmentTransaction.commit();
    }

    /**
     * Definig the correct fragments to the 3Tab postion
     * @param position - number of place.
     * @return - the fragment object.
     */
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return Chat.newInstance(this);
            case 1:
                return Calender.newInstance(this);
            case 2:
                return Posts.newInstance(this);
            default:
                return new Chat();
        }
    }

    /**
     * Used for Chat fragment option.
     * Setting peer title with name.
     * @param num - 0 - chat fragment position
     * @param id - of conversation.
     * @param name - of peer.
     */
    @Override
    public void setChatImmediateCall(String num, String id, String name){
        container.setChatImmediatCall(num,id,name);
        setConversation(id);
    }

    /**
     * Setting the title of 3Tab container title
     * @param labels - the mapping between the Textview object and position in 3tab.
     */
    @Override
    public void putFragmentsNames(Map<Integer, TextView> labels) {
        labels.get(0).setText(getResources().getString(R.string.chat_label));
        labels.get(1).setText(getResources().getString(R.string.calender_label));
        labels.get(2).setText(getResources().getString(R.string.posts_label));
    }
}
