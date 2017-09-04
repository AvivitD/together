/*************************************************************
 * Raz Ronen
 * 201410669
 * 89-211-05
 ************************************************************/
package com.weallone.raz.together.Packs;

import com.weallone.raz.together.Entities.Post;

import java.util.HashMap;

/**
 * Pack for POST messages.
 */
public class PostPack {
    private Post post;
    private String url;
    private String[] keys;
    private Boolean edit = false;

    /**
     * Construcutor- init members
     * @param url - url
     * @param post - post object
     * @param keys - keys for hash
     * @param edit - influence the hash
     */
    public PostPack(String url, Post post, String[] keys, Boolean edit) {
        this.post = post;
        this.url = url;
        this.keys = keys;
        this.edit = edit;
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
        hash.put(keys[0], post.getTitle());
        hash.put(keys[1], post.getMessage());
        hash.put(keys[2], post.getPublisher());
        hash.put(keys[3], post.getPublisherID());
        hash.put(keys[4], post.getDate());
        if(edit) return hash;
        hash.put(keys[5], String.valueOf(post.getIsPublic()));
        hash.put(keys[6], String.valueOf(post.getColor()));
        hash.put(keys[7], String.valueOf(post.getAnswered()));
        hash.put(keys[8], post.getImage());
        hash.put(keys[9], post.getLink());
        hash.put(keys[10], post.getImageX());
        hash.put(keys[11], post.getImageY());
        hash.put(keys[12], post.getPublisherEntity());
        hash.put(keys[13], post.getPublisherIcon());
        return hash;
    }
}
