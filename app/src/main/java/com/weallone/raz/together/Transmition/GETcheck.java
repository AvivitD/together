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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Responsible for all get Request from android app.
 */
public class GETcheck {

    private static final String TAG = "GETcheck";
    private Cookied cookied;
    private String getCookie;
    private String saveCookie;

    /**
     * Get the string result from request.
     * @param url - to get.
     * @param cookied - to enter.
     * @return the return of server.
     */
    public String getOutputFromURL(String url, Cookied cookied, String getCookie, String saveCookie, Context context){
        this.getCookie = getCookie;
        this.saveCookie = saveCookie;
        this.cookied = cookied;
        StringBuffer output = new StringBuffer();
        try{
            InputStream stream = getHttpConnection(context, url);
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
        } catch (NoSuchProviderException e) {
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
    private InputStream getHttpConnection(Context context, String urlString) throws IOException, NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException, CertificateException, NoSuchProviderException {
        Log.d("GET:",urlString);
        Log.d("GETcheck", ">>>>>>>>>>>");
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
        if(cookied!= null && !cookied.getCookie().equals("")) {
            urlConnection.setRequestProperty(saveCookie, cookied.getCookie());
        }
        InputStream stream = urlConnection.getInputStream();
        if(urlConnection.getHeaderField(getCookie) != null) {
            cookied.setCookie(urlConnection.getHeaderField(getCookie));
        }
        return new BufferedInputStream(urlConnection.getInputStream());
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
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
