package com.weallone.raz.together.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.QuotePack;
import com.weallone.raz.together.Transmition.POST;

/**
 * Edit a quote task
 */
public class EditQuote extends AsyncTask<QuotePack, String, String> {
    private AsyncResponse delegate;
    private String key;
    private Cookied cookied;
    private String setCookie;
    private String getCookie;
    private Context context;

    /**
     * Construcotr - init members.
     * @param delegate - whom to notify
     * @param key - what to notify
     * @param cookied - used for request
     * @param getCookie
     * @param setCookie
     * @param context
     */
    public EditQuote(AsyncResponse delegate, String key, Cookied cookied, String getCookie, String setCookie,
                     Context context){
        this.cookied = cookied;
        this.delegate = delegate;
        this.key = key;
        this.getCookie = getCookie;
        this.setCookie = setCookie;
        this.context = context;
    }
    /**
     * The async task in background -
     * @param packs
     * @return output from server
     */
    @Override
    protected String doInBackground(QuotePack... packs) {
        POST post = new POST(context);
        String output = null;
        for(QuotePack pack: packs){
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
