package com.weallone.raz.together.Packs;

import com.weallone.raz.together.Entities.Shift;

import java.util.HashMap;

/**
 * Pack for POST messages.
 */
public class ShiftPack {
    private static final String TAG = "ShiftPack";
    private Shift shift;
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
     * @param url
     * @param shift
     * @param keys
     */
    public ShiftPack(String url, Shift shift, String[] keys) {
        this.shift = shift;
        this.url = url;
        this.keys = keys;
    }

    /**
     * Creation of hashmap for POST.
     * @return
     */
    public HashMap<String, String> Hash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put(keys[1], shift.getStart());
        hash.put(keys[2], shift.getEnd());
        hash.put(keys[3], shift.getPsycho_id());
        hash.put(keys[4], shift.getPsycho_deviceId());
        hash.put(keys[5], shift.getPsycho_name());
        hash.put(keys[6], shift.getPsycho_mail());
        return hash;
    }
}
