package com.weallone.raz.together.AsyncTasks;

import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Transmition.GET;

/**
 * Represent the Delete a comment task.
 */
public class DeleteComment extends AsyncTask<String, String, String> {

    private AsyncResponse delegate;
    private boolean append;
    private String key;
    private Cookied cookied;
    private String setCookie;
    private String getCookie;
    /**
     * Constructor - init members.
     * @param delegate - who to notify when done
     * @param key - what to notify
     * @param cookied used for get.
     * @param getCookie
     * @param setCookie
     */
    public DeleteComment(AsyncResponse delegate, String key, Cookied cookied
            , String getCookie, String setCookie){
        this.cookied = cookied;
        this.delegate = delegate;
        this.key = key;
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
        delegate.OnFinished(key + result);
    }
}
