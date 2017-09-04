package com.weallone.raz.together.Entities;

/**
 * Shift object represntation fro Calendar fragment.
 */
public class Shift {
    private String id;
    private String start;
    private String end;
    private String psycho_id;
    private String psycho_deviceId;
    private String psycho_name;
    private String psycho_mail;

    /**
     * toString() override
     * @return
     */
    @Override
    public String toString() {
        return "Shift{" +
                "id='" + id + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", psycho_id='" + psycho_id + '\'' +
                ", psycho_deviceId='" + psycho_deviceId + '\'' +
                ", psycho_name='" + psycho_name + '\'' +
                ", psycho_mail='" + psycho_mail + '\'' +
                '}';
    }

    /**
     * Constructor - init members
     * @param id - of shift
     * @param start - time begins
     * @param end - time end
     * @param psycho_id - id of psycho
     * @param psycho_deviceId - deviceId of him
     * @param psycho_name - name
     * @param psycho_mail - email
     */
    public Shift(String id, String start, String end, String psycho_id,String psycho_deviceId,
                 String psycho_name, String psycho_mail) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.psycho_id = psycho_id;
        this.psycho_deviceId = psycho_deviceId;
        this.psycho_name = psycho_name;
        this.psycho_mail = psycho_mail;
    }

    /**
     * Constructor - init members
     * @param start - time begins
     * @param end - time end
     * @param psycho_id - id of psycho
     * @param psycho_deviceId - deviceId of him
     * @param psycho_name - name
     * @param psycho_mail - email
     */
    public Shift(String start, String end, String psycho_id,String psycho_deviceId,
                 String psycho_name, String psycho_mail) {
        this.start = start;
        this.end = end;
        this.psycho_id = psycho_id;
        this.psycho_deviceId = psycho_deviceId;
        this.psycho_name = psycho_name;
        this.psycho_mail = psycho_mail;
    }

    /**
     * get method
     * @return
     */
    public String getPsycho_deviceId() {
        return psycho_deviceId;
    }

    /**
     * set method
     * @param psycho_deviceId
     */
    public void setPsycho_deviceId(String psycho_deviceId) {
        this.psycho_deviceId = psycho_deviceId;
    }

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
    public String getStart() {
        return start;
    }

    /**
     * set method
     * @param start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * get method
     * @return
     */
    public String getEnd() {
        return end;
    }

    /**
     * set method
     * @param end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * get method
     * @return
     */
    public String getPsycho_id() {
        return psycho_id;
    }

    /**
     * set method
     * @param psycho_id
     */
    public void setPsycho_id(String psycho_id) {
        this.psycho_id = psycho_id;
    }

    /**
     * get method
     * @return
     */
    public String getPsycho_name() {
        return psycho_name;
    }

    /**
     * set method
     * @param psycho_name
     */
    public void setPsycho_name(String psycho_name) {
        this.psycho_name = psycho_name;
    }

    /**
     * get method
     * @return
     */
    public String getPsycho_mail() {
        return psycho_mail;
    }

    /**
     * set method
     * @param psycho_mail
     */
    public void setPsycho_mail(String psycho_mail) {
        this.psycho_mail = psycho_mail;
    }
}
