package com.weallone.raz.together.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.HelpRequestPack;
import com.weallone.raz.together.Transmition.POST;

/**
 * Send help request task
 */
public class SendHelpRequest extends AsyncTask<HelpRequestPack, String, String> {
    private AsyncResponse delegate;
    private String key;
    private Cookied cookied;
    private String setCookie;
    private String getCookie;
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
    public SendHelpRequest(AsyncResponse delegate, String key, Cookied cookied, String getCookie, String setCookie,
                           Activity activity){
        this.cookied = cookied;
        this.delegate = delegate;
        this.key = key;
        this.getCookie = getCookie;
        this.setCookie = setCookie;
        this.activity = activity;
    }
    /**
     * The async task in background -
     * @param packs
     * @return output from server
     */
    @Override
    protected String doInBackground(HelpRequestPack... packs) {
        POST post = new POST(activity);
        String output = null;
        for(HelpRequestPack pack: packs){
            output =  post.Response(pack.getUrl(), pack.Hash(), cookied, getCookie, setCookie);
        }
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
