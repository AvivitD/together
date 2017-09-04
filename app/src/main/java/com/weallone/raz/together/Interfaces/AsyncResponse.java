/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.Interfaces;

/**
 * notifyed when async task is done.
 */
public interface AsyncResponse {
    /**
     * Processing the callback from async task
     * @param result - string from server.
     */
    void OnFinished(String result);
}
