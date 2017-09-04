/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.AsyncTasks;

import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Transmition.GET;

/**
 * The Delete a post task.
 */
public class DeletePost extends AsyncTask<String, String, String>{

    private AsyncResponse delegate;
    private boolean append;
    private String key1;
    private String key2;
    private Cookied cookied;
    private String setCookie;
    private String getCookie;

    /**
     * Constructor - init members.
     * @param delegate - whom to notify
     * @param key1 - what to notify.
     * @param cookied - used for GET
     * @param getCookie
     * @param setCookie
     */
    public DeletePost(AsyncResponse delegate, String key1, Cookied cookied
            , String getCookie, String setCookie){
        this.cookied = cookied;
        this.delegate = delegate;
        this.key1 = key1;
        this.getCookie = getCookie;
        this.setCookie = setCookie;
    }

    /**
     * The async task in background -
     * @param urls
     * @return output from server
     */
    @Override
    protected String doInBackground(String... urls) {
        GET post = new GET();
        String output = null;
        for(String url: urls){
            output =  post.getOutputFromURL(url, cookied, getCookie, setCookie);
        }
        return output;
    }
    /**
     * Notifying delegate async done.
     * @param result output from server.
     */
    @Override
    protected void onPostExecute(String result) {
        delegate.OnFinished(key1 + result);
    }
}
