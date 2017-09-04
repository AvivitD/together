package com.weallone.raz.together.Entities;

/**
 * Represents the message object in chat fragment
 */
public class Message {
    private String childID;
    private Integer num;
    private String time;
    private String message;
    private String image = "";
    private String link = "";
    private String authorID;
    private String authorName;
    private String authorEntity;

    /**
     * get method
     * @return
     */
    public String getImageX() {
        return imageX;
    }

    /**
     * set method
     * @param imageX
     */
    public void setImageX(String imageX) {
        this.imageX = imageX;
    }

    /**
     * get method
     * @return
     */
    public String getImageY() {
        return imageY;
    }

    /**
     * set method
     * @param imageY
     */
    public void setImageY(String imageY) {
        this.imageY = imageY;
    }

    private String imageX = "";
    private String imageY = "";

    /**
     * Tostring override
     * @return
     */
    @Override
    public String toString() {
        return "Message{" +
                "childID='" + childID + '\'' +
                ", num=" + num +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", authorID='" + authorID + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorEntity='" + authorEntity + '\'' +
                '}';
    }

    /**
     * Construcotr - init members
     * @param childID - child id.
     * @param num - num of message in conversation
     * @param time - time it created
     * @param message - content of message
     * @param image - image if exists
     * @param link - link if exists
     * @param authorID - author id
     * @param authorName - name of message author
     * @param authorEntity - entity of msg author
     * @param imageX - width of image
     * @param imageY - height ofimage.
     */
    public Message(String childID, Integer num, String time, String message, String image,
                   String link, String authorID, String authorName, String authorEntity, String imageX,
                   String imageY) {
        this.childID = childID;
        this.num = num;
        this.time = time;
        this.message = message;
        this.image = image;
        this.link = link;
        this.authorID = authorID;
        this.authorName = authorName;
        this.authorEntity = authorEntity;
        this.imageX = imageX;
        this.imageY = imageY;
    }

    /**
     * get method
     * @return
     */
    public String getChildID() {

        return childID;
    }

    /**
     * set method
     * @param childID
     */
    public void setChildID(String childID) {
        this.childID = childID;
    }

    /**
     * get method
     * @return
     */
    public Integer getNum() {
        return num;
    }

    /**
     * set method
     * @param num
     */
    public void setNum(Integer num) {
        this.num = num;
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
     * get method
     * @return
     */
    public String getImage() {
        return image;
    }

    /**
     * set method
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * get method
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     * set method
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * get method
     * @return
     */
    public String getAuthorID() {
        return authorID;
    }

    /**
     * set method
     * @param authorID
     */
    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    /**
     * get method
     * @return
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * set method
     * @param authorName
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * get method
     * @return
     */
    public String getAuthorEntity() {
        return authorEntity;
    }

    /**
     * set method
     * @param authorEntity
     */
    public void setAuthorEntity(String authorEntity) {
        this.authorEntity = authorEntity;
    }

}
