package com.weallone.raz.together.Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weallone.raz.together.Adapters.SectionsPagerAdapter;
import com.weallone.raz.together.Interfaces.FragmentsCreator;
import com.weallone.raz.together.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 3Tab Container- can be expanded to 4 with easily small changes.
 */
public class Tabs3_Container extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener, Serializable {
    private static final String TAG = "...Tabs3_Container";
    private SectionsPagerAdapter mySectionsPagerAdapter;
    private static ViewPager viewPager;
    private Map<Integer,TextView> labels;
    private Map<Integer,LinearLayout> lines;
    private FragmentsCreator creator;
    private SharedPreferences settings;
    private Integer shown = null;

    /**
     * setting the fragment pager, so this can come back to same fragment in callback.
     */
    @Override
    public void onPause() {
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(getResources().getString(R.string.key_last_fragment_3container),
                viewPager.getCurrentItem()).apply();
        super.onPause();
    }

    /**
     * default constructor
     */
    public Tabs3_Container() {
        // Required empty public constructor
    }

    /**
     * setting shared prefences
     */
    private void SetSharedPrefences() {
        settings = getActivity().getSharedPreferences(getResources().getString(R.string.data),
                getContext().MODE_PRIVATE);
        settings = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
    }

    /**
     * tab3 creation method
     * @param key
     * @param creator
     * @return
     */
    public static Tabs3_Container newInstance(String key, FragmentsCreator creator){
        Tabs3_Container fragment = new Tabs3_Container();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (Serializable) creator);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * onCreateView standard method - creating UI and viewPager - attaching adapter.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view edited.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        if(activity != null){

            FragmentsCreator creator = (FragmentsCreator) getArguments().
                    getSerializable(getResources().getString(R.string.fragment_creator_key));
            SetSharedPrefences();
            this.creator = creator;
            View view = inflater.inflate(R.layout.fragment_tab3__container, container, false);
            InitUI(view);
            mySectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), labels,
                    lines,getContext(),creator);
            viewPager = (ViewPager) view.findViewById(R.id.tab3_cfp_container);
            viewPager.setAdapter(mySectionsPagerAdapter);
            viewPager.setOnPageChangeListener(this);
            setLastFragmentAppear();
            return view;
        }
        return null;
    }

    /**
     * setting the last fragment (in case we came back from background).
     */
    private void setLastFragmentAppear() {
        switch(settings.getInt(getResources().getString(R.string.key_last_fragment_3container), 0)){
            case 0:
                viewPager.setCurrentItem(0, false);
                break;
            case 1:
                viewPager.setCurrentItem(1, false);
                break;
            case 2:
                viewPager.setCurrentItem(2, false);
                break;
            default:;
        }
    }

    /**
     * Setting the labels listeners.
     * @param view
     */
    private void InitUI(View view) {
        labels = new HashMap<>();
        labels.put(0, (TextView) view.findViewById(R.id.tab3_label1_fragment_label));
        labels.put(1, (TextView) view.findViewById(R.id.tab3_label2_fragment_label));
        labels.put(2, (TextView) view.findViewById(R.id.tab3_label3_fragment_label));
        labels.get(0).setOnClickListener(this);
        labels.get(1).setOnClickListener(this);
        labels.get(2).setOnClickListener(this);
        creator.putFragmentsNames(labels);
        lines = new HashMap<>();
        lines.put(0, (LinearLayout) view.findViewById(R.id.tab3_label1_line_label));
        lines.put(1, (LinearLayout) view.findViewById(R.id.tab3_label2_line_label));
        lines.put(2, (LinearLayout) view.findViewById(R.id.tab3_label3_line_label));
    }

    /**
     * Changing theh page by the viewpager selection clicked.
     * @param v
     */
    @Override
    public void onClick(View v) {
        for(Map.Entry<Integer,TextView> entry: labels.entrySet()){
            if(entry.getValue() == v){
                viewPager.setCurrentItem(entry.getKey());
            }
        }
    }

    /**
     * Standard onPageScrolled method.
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(shown!=null) mySectionsPagerAdapter.Hidden(shown);
        shown = position;
        mySectionsPagerAdapter.Shown(position);
    }

    /**
     * not implemented
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * not implemeneted
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * setting the pager to chat fragment(0)
     * @param num - of chat fragment
     * @param id - id of conversation
     * @param name - of peer conversation.
     */
    public void setChatImmediatCall(String num, String id, String name) {
        viewPager.setCurrentItem(0);
    }
}
