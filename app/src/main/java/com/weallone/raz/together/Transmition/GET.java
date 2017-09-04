/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.Transmition;

import android.util.Log;

import com.weallone.raz.together.Interfaces.Cookied;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Responsible for all get Request from android app.
 */
public class GET {

    private static final String TAG = "GET";
    private Cookied cookied;
    private String getCookie;
    private String saveCookie;

    /**
     * Get the string result from request.
     * @param url - to get.
     * @param cookied - to enter.
     * @return the return of server.
     */
    public String getOutputFromURL(String url, Cookied cookied, String getCookie, String saveCookie){
        this.getCookie = getCookie;
        this.saveCookie = saveCookie;
        this.cookied = cookied;
        StringBuffer output = new StringBuffer();
        try{
            InputStream stream = getHttpConnection(url);
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(stream));
            String s = "";
            while((s = buffer.readLine()) != null)
                output.append(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * Get the input stream from that url.
     * @param urlString - url
     * @return input stream.
     * @throws IOException
     */
    private InputStream getHttpConnection(String urlString) throws IOException, NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException, CertificateException {
        Log.d("URL",urlString);
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if(cookied!= null && !cookied.getCookie().equals("")) {
            urlConnection.setRequestProperty(saveCookie, cookied.getCookie());
        }
        InputStream stream = urlConnection.getInputStream();
        if(urlConnection.getHeaderField(getCookie) != null) {
            cookied.setCookie(urlConnection.getHeaderField(getCookie));
        }
        return new BufferedInputStream(urlConnection.getInputStream());
    }
}
