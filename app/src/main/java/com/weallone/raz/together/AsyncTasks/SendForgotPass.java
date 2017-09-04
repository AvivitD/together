package com.weallone.raz.together.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.ForgotPassPack;
import com.weallone.raz.together.Transmition.POST;

/**
 * Send forgot password task.
 */
public class SendForgotPass extends AsyncTask<ForgotPassPack, String, String> {
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
    public SendForgotPass(AsyncResponse delegate, String key, Cookied cookied, String getCookie, String setCookie,
                           Activity activity){
        this.cookied = cookied;
        this.delegate = delegate;
        this.key = key;
        this.getCookie = getCookie;
        this.setCookie = setCookie;
        this.activity = activity;
    }

    /**
     * The async task in background
     * @param packs - contains url, hash.
     * @return the callback string from server.
     */
    @Override
    protected String doInBackground(ForgotPassPack... packs) {
        POST post = new POST(activity);
        String output = null;
        for(ForgotPassPack pack: packs){
            output =  post.Response(pack.getUrl(), pack.Hash(), cookied, getCookie, setCookie);
        }
        return output;
    }
    /**
     * Notifying delegate async done.
     * @param result output from server.
     */
    protected void onPostExecute(String result) {
        delegate.OnFinished(key + result);
    }
}
