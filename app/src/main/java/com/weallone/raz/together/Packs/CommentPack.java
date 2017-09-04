package com.weallone.raz.together.Packs;

import android.util.Log;

import com.weallone.raz.together.Entities.Comment;

import java.util.HashMap;

/**
 * Comment Pack for POST messages.
 */
public class CommentPack {
    private Comment comment;
    private String url;
    private String[] keys;
    private Boolean edit = false;

    /**
     * Constructor - init members
     * @param url -url
     * @param comment - -comment object
     * @param keys - keys for hash
     * @param edit - influence the hash.
     */
    public CommentPack(String url, Comment comment, String[] keys, Boolean edit) {
        this.comment = comment;
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
        Log.d("IMAGE", comment.getImage());
        HashMap<String, String> hash = new HashMap<>();
        hash.put(keys[0], comment.getMessage());
        hash.put(keys[1], comment.getPublisher());
        hash.put(keys[2], comment.getPublisherID());
        hash.put(keys[3], comment.getDate());
        hash.put(keys[4], comment.getPostID());
        if(edit) return hash;
        hash.put(keys[5], comment.getImage());
        hash.put(keys[6], comment.getLink());
        hash.put(keys[7], comment.getImageX());
        hash.put(keys[8], comment.getImageY());
        hash.put(keys[9], comment.getPublisherEntity());
        return hash;
    }
}
