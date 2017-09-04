/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Transmition.GETcheck;

/**
 * Created by Raz on 6/15/2016.
 */
public class GetEntityCheck extends AsyncTask<String, String, String>{

    private AsyncResponse delegate;
    private String key;
    private Cookied cookied;
    private String setCookie;
    private String getCookie;
    private Context context;

    /**
     * Constructor
     * @param delegate - whom to notify when done.
     * @param key - string to notify.
     * @param cookied - used for GET.
     */
    public GetEntityCheck(AsyncResponse delegate, String key, Cookied cookied
            , String getCookie, String setCookie, Context context){
        this.cookied = cookied;
        this.delegate = delegate;;
        this.key = key;
        this.getCookie = getCookie;
        this.setCookie = setCookie;
        this.context = context;
    }

    /**
     * The async task in background -
     * @param urls
     * @return output from server
     */
    @Override
    protected String doInBackground(String... urls) {
        GETcheck post = new GETcheck();
        String output = null;
        for(String url: urls){
            output =  post.getOutputFromURL(url, cookied, getCookie, setCookie,context);
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
