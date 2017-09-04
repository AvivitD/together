package com.weallone.raz.together.Packs;

import com.weallone.raz.together.Entities.Message;

import java.util.HashMap;

/**
 * Pack for POST messages.
 */
public class MessagePack {
    private Message message;
    private String url;

    /**
     * get method
     * @return
     */
    public String getUrl() {
        return url;
    }

    private String[] keys;

    /**
     * Constructor - init members
     * @param url - url
     * @param message - message object
     * @param keys - for hash
     */
    public MessagePack(String url,Message message, String[] keys) {
        this.message = message;
        this.url = url;
        this.keys = keys;
    }

    /**
     * Creating hash object for POST message
     * @return the Hashmap.
     */
    public HashMap<String, String> Hash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put(keys[0], message.getChildID());
        hash.put(keys[1], String.valueOf(message.getNum()));
        hash.put(keys[2], message.getTime());
        hash.put(keys[3], message.getMessage());
        hash.put(keys[4], message.getImage());
        hash.put(keys[5], message.getLink());
        hash.put(keys[6], message.getAuthorID());
        hash.put(keys[7], message.getAuthorName());
        hash.put(keys[8], message.getAuthorEntity());
        hash.put(keys[9], message.getImageX());
        hash.put(keys[10], message.getImageY());
        return hash;
    }
}
