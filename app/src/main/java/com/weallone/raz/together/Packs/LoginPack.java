package com.weallone.raz.together.Packs;

import com.weallone.raz.together.Entities.Login;

import java.util.HashMap;

/**
 * Pack for POST messages.
 */
public class LoginPack {
    private Login login;
    private String url;
    private String[] keys;

    /**
     * Constructor - init members.
     * @param url - url
     * @param login - login object
     * @param keys - keys for hash.
     */
    public LoginPack(String url, Login login, String[] keys) {
        this.login = login;
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
        hash.put(keys[0], login.getEmail());
        hash.put(keys[1], login.getPass());
        return hash;
    }
}
