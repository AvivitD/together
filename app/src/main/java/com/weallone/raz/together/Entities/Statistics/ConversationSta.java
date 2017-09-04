package com.weallone.raz.together.Entities.Statistics;

/**
 * represents the conversation(char) of statistics fragment.
 */
public class ConversationSta {
    private String peer = null;
    private String msg = null;

    /**
     * constructor
     * @param peer - whom wrote
     * @param msg - what wrote
     */
    public ConversationSta(String peer, String msg) {
        this.peer = peer;
        this.msg = msg;
    }

    /**
     * get method
     * @return
     */
    public String getPeer() {
        return peer;
    }

    /**
     * set method
     * @param peer
     */
    public void setPeer(String peer) {
        this.peer = peer;
    }

    /**
     * get method
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * set method
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
