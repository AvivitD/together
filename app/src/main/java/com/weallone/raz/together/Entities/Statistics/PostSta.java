package com.weallone.raz.together.Entities.Statistics;

/**
 * Reperesents the post object of statistics fragment
 */
public class PostSta {
    private String title = null;
    private String msg = null;

    /**
     * get method
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * set method
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * constructor - init members
     * @param title
     * @param msg
     */
    public PostSta(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }
}
