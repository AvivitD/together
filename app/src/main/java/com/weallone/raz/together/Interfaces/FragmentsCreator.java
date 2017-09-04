package com.weallone.raz.together.Interfaces;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Map;

/**
 * Object with the ability to create fragments
 */
public interface FragmentsCreator extends Serializable{
    /**
     * creates fragment for this position
     * @param position - to create
     * @return the fragment matching
     */
    Fragment createFragment(int position);

    /**
     * Attaching name to fragment textviews
     * @param labels the mapping.
     */
    void putFragmentsNames(Map<Integer, TextView> labels);
}
