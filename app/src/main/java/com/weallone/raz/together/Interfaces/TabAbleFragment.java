package com.weallone.raz.together.Interfaces;

/**
 * fragment inside tabs containers
 */
public interface TabAbleFragment {
    /**
     * executed when the fragment is shown
     * @param first - if it appear first when activity is viewed
     */
    void Shown(Boolean first);

    /**
     * executed when fragment get swiped to background.
     */
    void Hidden();

    /**
     * get method
     * @return
     */
    Boolean isShown();
}
