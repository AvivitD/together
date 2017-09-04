/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.Transmition;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;

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
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Handles all POST request from app.
 */
public class POSTcheck {

    private Cookied cookied;
    private String getCookie;
    private String saveCookie;
    /**
     * Sends and ge the result from POST request.
     * @param url - to post to.
     * @param msg - to put in paramters
     * @param cookied - to send with request
     * @return - return of server.
     */
    public String Response(Context context, String url, HashMap<String, String> msg, Cookied cookied,
                                String getCookie, String saveCookie){
        this.getCookie = getCookie;
        this.saveCookie = saveCookie;
        this.cookied = cookied;
        StringBuffer output = new StringBuffer();
        try{
            InputStream stream = getHttpConnection(context, url, msg);
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
     * Get the input stream for post.
     * @param urlString - to psot
     * @param msg - to enter to post
     * @return - the input stream.
     * @throws IOException
     */
    private InputStream getHttpConnection(Context context, String urlString,
                                                 HashMap<String, String> msg) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        Log.d("POST>>>>>>:",urlString);
        trustEveryone();
        URL url = new URL(urlString);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager am = context.getAssets();
        InputStream caInput = context.getResources().openRawResource(R.raw.client);

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        Certificate ca = null;

        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);


        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);


        // Create an SSLContext that uses our TrustManager
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, tmf.getTrustManagers(), null);


        urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
        if(cookied != null && !cookied.getCookie().equals("")) {
            urlConnection.setRequestProperty(saveCookie, cookied.getCookie());
        }
        urlConnection.setRequestMethod("POST");
        OutputStream os = urlConnection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(msg));

        writer.flush();
        writer.close();
        os.close();
        int responseCode = urlConnection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStream stream = urlConnection.getInputStream();
            if(urlConnection.getHeaderField(getCookie) != null) {
                cookied.setCookie(urlConnection.getHeaderField(getCookie));
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


    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }
}
