package com.weallone.raz.together.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Transmition.POST;

import java.util.HashMap;

/**
 * Send post request task
 */
public class SendPostRequest extends AsyncTask<Void, String, String> {
    private AsyncResponse delegate;
    private String key;
    private Cookied cookied;
    private String setCookie;
    private String getCookie;
    private String url;
    private HashMap<String, String> hash = null;
    private Activity activity;
    /**
     * Constructor - init members.
     * @param delegate - whom to notify
     * @param key - what to notify
     * @param cookied - used for request
     * @param getCookie
     * @param setCookie
     * @param activity
     */
    public SendPostRequest(AsyncResponse delegate, String key, Cookied cookied, String getCookie, String setCookie,
                           String url, HashMap<String, String> hash, Activity activity){
        this.cookied = cookied;
        this.delegate = delegate;
        this.key = key;
        this.getCookie = getCookie;
        this.setCookie = setCookie;
        this.url = url;
        this.hash = hash;
        this.activity = activity;
    }
    /**
     * The async task in background
     * @param params - contains url, hash.
     * @return the callback string from server.
     */
    @Override
    protected String doInBackground(Void... params) {
        POST post = new POST(activity);
        String output = post.Response(url, hash, cookied, getCookie, setCookie);
        return output;
    }

    /**
     * Notify delegate async task is done.
     * @param result from server.
     */
    protected void onPostExecute(String result) {
        delegate.OnFinished(key + result);
    }
}
