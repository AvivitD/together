package com.weallone.raz.together.Entities.Statistics;

/**
 * represents the comment of statistics fragment.
 */
public class CommentSta {
    private String titlePost = null;
    private String msg = null;

    /**
     * get method
     * @return title post
     */
    public String getTitlePost() {
        return titlePost;
    }

    /**
     * set method
     * @param titlePost
     */
    public void setTitlePost(String titlePost) {
        this.titlePost = titlePost;
    }

    /**
     * get method
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Set method
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * get method
     * @param titlePost
     * @param msg
     */
    public CommentSta(String titlePost, String msg) {
        this.titlePost = titlePost;
        this.msg = msg;
    }
}
