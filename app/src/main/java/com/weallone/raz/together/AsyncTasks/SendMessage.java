package com.weallone.raz.together.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.MessagePack;
import com.weallone.raz.together.Transmition.POST;

/**
 * Send message task (chat)
 */
public class SendMessage extends AsyncTask<MessagePack, String, String> {
    private static final String TAG = "com.example.raz.togther.AsyncTasks.SendMessage";
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
    public SendMessage(AsyncResponse delegate, String key, Cookied cookied, String getCookie, String setCookie,
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
    protected String doInBackground(MessagePack... packs) {
        POST post = new POST(activity);
        String output = null;
        for(MessagePack pack: packs){
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
