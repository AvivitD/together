package com.weallone.raz.together.Adapters;

/**
 * Created by Raz on 1/10/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weallone.raz.together.Interfaces.FragmentsCreator;
import com.weallone.raz.together.Interfaces.TabAbleFragment;
import com.weallone.raz.together.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the page changing of view pager fragment.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter implements Serializable{
    private Map<Integer,TextView> labels = new HashMap<>();
    private Map<Integer,LinearLayout> lines = new HashMap<>();
    private FragmentsCreator creator;
    private Object lastPrimary = null;

    private Context context;

    /**
     * Constructor - init members.
     * @param fragmentManager
     * @param labels - of each tab.
     * @param lines - of each tab
     * @param context
     * @param creator - the creator.
     */
    public SectionsPagerAdapter(FragmentManager fragmentManager,Map<Integer,TextView> labels,
                      Map<Integer,LinearLayout> lines, Context context, FragmentsCreator creator){
        super(fragmentManager);
        this.context = context;
        this.labels = labels;
        this.lines = lines;
        this.creator = creator;
    }

    /**
     * overide method. returns the fragment.
     * @param position of item.
     * @return the fragment that was loaded.
     */
    @Override
    public Fragment getItem(int position) {
        resetLabels(position);
        return creator.createFragment(position);
    }

    /**
     * Sets the item in the view pager.
     * @param container
     * @param position to set
     * @param object to apply
     */
    public void setPrimaryItem(ViewGroup container, int position, Object object){
        super.setPrimaryItem(container, position, object);
        resetLabels(position);

        if (object instanceof TabAbleFragment) {
            if(object != lastPrimary){
                if(lastPrimary!=null) ((TabAbleFragment) lastPrimary).Hidden();
                if(lastPrimary==null) ((TabAbleFragment) object).Shown(true);
                else ((TabAbleFragment) object).Shown(false);
                lastPrimary = object;
            }
        }
    }

    /**
     * Amount of viewpager items.
     * @return 3
     */
    @Override
    public int getCount() {
        return labels.size();
    }

    /**
     * Setting the labels style.
     * @param position - position to set.
     */
    public void resetLabels(int position) {
        for(int i = 0; i < labels.size();i++)
            labels.get(i).setTypeface(null, Typeface.NORMAL);
        for(int i = 0; i < lines.size();i++)
            lines.get(i).setBackgroundColor(context.getResources().getColor(R.color.color_activity_bar));
        lines.get(position).setBackgroundColor(context.getResources().getColor(R.color.white));
        labels.get(position).setTypeface(null, Typeface.BOLD);
    }

    /**
     * Not used.
     * @param shown
     */
    public void Hidden(Integer shown) {

    }

    /**
     * Not used.
     * @param position
     */
    public void Shown(int position) {


    }
}
