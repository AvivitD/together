package com.weallone.raz.together.Entities.Updates;

import org.json.JSONObject;

import java.util.Comparator;

/**
 * Represents an update object in updates fragment
 */
public class Update implements Comparator<Update>{
    public static final String NEW_SHIFT = "SHIFT_UPDATE";
    public static final String NEW_USER = "USER_UPDATE";
    private String update = null;
    private String time = null;
    private Long num = null;

    /**
     * Constrcutor - init members
     * @param update - string representation
     * @param time - when created
     * @param num - num in update list
     * @param json - unique information about it
     */
    public Update(String update, String time, Long num, JSONObject json) {
        this.update = update;
        this.time = time;
        this.num = num;
        this.json = json;
    }

    /**
     * Default contstructor.
     */
    public Update() {
    }

    /**
     * Get method
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * Set method
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Get method
     * @return
     */
    public Long getNum() {
        return num;
    }

    /**
     * set method
     * @param num
     */
    public void setNum(Long num) {
        this.num = num;
    }

    /**
     * get method
     * @return
     */
    public JSONObject getJson() {
        return json;
    }

    /**
     * set method
     * @param json
     */
    public void setJson(JSONObject json) {
        this.json = json;
    }

    private JSONObject json = null;

    /**
     * get method
     * @return
     */
    public String getUpdate() {
        return update;
    }

    /**
     * set method
     * @param update
     */
    public void setUpdate(String update) {
        this.update = update;
    }

    /**
     * Comparing between two updates by their position in updates list.
     * @param lhs - left
     * @param rhs - right
     * @return - 0 for equal.
     */
    @Override
    public int compare(Update lhs, Update rhs) {
        return (int)(lhs.getNum() - rhs.getNum());
    }
}
