package com.weallone.raz.together.Entities;

/**
 * Representing help request (from quotes fragment)
 */
public class HelpRequest {
    private String id;

    /**
     * get method
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * set method
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get method
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * set method
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * get method
     * @return
     */
    public String getChild_id() {
        return child_id;
    }

    /**
     * set method
     * @param child_id
     */
    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    /**
     * get method
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * set method
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Construcotr - init members
     * @param time - when created
     * @param child_id - that wrote it
     * @param message - content of it
     * @param child_name - name of child
     */
    public HelpRequest(String time, String child_id, String message, String child_name) {
        this.time = time;
        this.child_id = child_id;
        this.message = message;
        this.child_name = child_name;
    }

    private String time;
    private String child_id;
    private String message;
    private String child_name;

    /**
     * get method
     * @return
     */
    public String getChild_name() {
        return child_name;
    }

    /**
     * set method.
     * @param child_name
     */
    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }
}
