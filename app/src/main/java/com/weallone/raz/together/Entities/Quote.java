package com.weallone.raz.together.Entities;

/**
 * Represents a quote object in quote fragment
 */
public class Quote {
    private String num = "-1";
    private String content = null;
    private String creator = null;

    /**
     * get method
     * @return
     */
    public String getNum() {
        return num;
    }

    /**
     * set method
     * @param num
     */
    public void setNum(String num) {
        this.num = num;
    }

    /**
     * get method
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * set method
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * get method
     * @return
     */
    public String getCreator() {
        return creator;
    }

    /**
     * set method
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Construcotr - init members
     * @param num - place of quote in list
     * @param content - of quote
     * @param creator - author
     */
    public Quote(String num, String content, String creator) {

        this.num = num;
        this.content = content;
        this.creator = creator;
    }

    /**
     * Construcotr - init members
     * @param content - of quote
     * @param creator - author
     */
    public Quote(String content, String creator) {
        this.content = content;
        this.creator = creator;
    }
}
