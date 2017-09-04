/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.Transmition;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;
import com.google.gson.Gson;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles all POST request from app.
 */
public class POST {

    private Cookied cookied;
    private String getCookie;
    private String saveCookie;
    private Context context;

    public POST(Context context){
        this.context = context;
    }
    /**
     * Sends and ge the result from POST request.
     * @param url - to post to.
     * @param msg - to put in paramters
     * @param cookied - to send with request
     * @return - return of server.
     */
    public String Response(String url, HashMap<String, String> msg, Cookied cookied,
                                String getCookie, String saveCookie){
        this.getCookie = getCookie;
        this.saveCookie = saveCookie;
        this.cookied = cookied;
        StringBuffer output = new StringBuffer();
        try{
            InputStream stream = getHttpConnection(url, msg);
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(stream));
            String s = "";
            while((s = buffer.readLine()) != null)
                output.append(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * Get the input stream for post.
     * @param urlString - to psot
     * @param msg - to enter to post
     * @return - the input stream.
     * @throws IOException
     */
    private InputStream getHttpConnection(String urlString,
                                                 HashMap<String, String> msg) throws IOException{
        URL url = new URL(urlString);
        Log.d("POST:",urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if(cookied != null && !cookied.getCookie().equals("")) {
            connection.setRequestProperty(saveCookie, cookied.getCookie());
        }
        connection.setRequestMethod("POST");
        OutputStream os = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(msg));

        writer.flush();
        writer.close();
        os.close();
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStream stream = connection.getInputStream();
            if(connection.getHeaderField(getCookie) != null) {
                cookied.setCookie(connection.getHeaderField(getCookie));
            }
            Log.d("RECIEVED:",urlString);
            return new BufferedInputStream(stream);
        }
        return null;
    }

    /**
     * Insert the paramaters to string and returns it.
     * @param params
     * @return paramaters organiized.
     * @throws UnsupportedEncodingException
     */
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        addAuthentication(params);
        Log.d("POST",params.toString());
        for(Map.Entry<String,String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private void addAuthentication(HashMap<String, String> params) {
        Gson gson = new Gson();
        SharedPreferences settings = null;
        settings = context.getSharedPreferences(context.getResources().getString(R.string.data),
                context.MODE_PRIVATE);
        settings = context.getApplicationContext().getSharedPreferences(
                context.getResources().getString(R.string.prefs), 0);
        String json = settings.getString("User", "");
        if(json.equals("")){
            return;
        } else {
            UserInfo userInfo = gson.fromJson(json, UserInfo.class);
            params.put("email",userInfo.getEmail());
            params.put("pass",userInfo.getPass());
        }
    }

}
