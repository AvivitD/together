package com.weallone.raz.together.Packs;

import com.weallone.raz.together.Entities.ForgotPass;

import java.util.HashMap;

/**
 * Pack for POST messages.
 */
public class ForgotPassPack {
    private ForgotPass forgotPass;
    private String url;
    private String[] keys;

    /**
     * Constructor - init members
     * @param url - url
     * @param forgotPass - forgot object
     * @param keys - for hash
     */
    public ForgotPassPack(String url, ForgotPass forgotPass, String[] keys) {
        this.forgotPass = forgotPass;
        this.url = url;
        this.keys = keys;
    }

    /**
     * get method
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Creating hashmap from properties
     * @return the hashmap
     */
    public HashMap<String, String> Hash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put(keys[0], forgotPass.getEmail());
        hash.put(keys[1], forgotPass.getCode());
        return hash;
    }
}
