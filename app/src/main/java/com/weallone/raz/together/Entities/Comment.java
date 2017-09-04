package com.weallone.raz.together.Entities;

/**
 * Represent the comment object in post fragment
 */
public class Comment {
    private String postID;
    private String message;
    private String publisher;
    private String publisherID;
    private String publisherEntity;

    /**
     * get method
     * @return
     */
    public String getPublisherEntity() {
        return publisherEntity;
    }

    /**
     * set method
     * @param publisherEntity
     */
    public void setPublisherEntity(String publisherEntity) {
        this.publisherEntity = publisherEntity;
    }

    private String date;
    private String id;
    private String image = "";
    private String link = "";
    private String imageX = "";
    private String imageY = "";

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
    public String getPostID() {
        return postID;
    }

    /**
     * set metod
     * @param postID
     */
    public void setPostID(String postID) {
        this.postID = postID;
    }

    /**
     * Constructor - init members.
     * @param message - text in comment
     * @param publisher - name of author
     * @param publisherID - id of author
     * @param date - when the comment created
     * @param postID - the post id containing it
     * @param image - image in comment if exists
     * @param link - link if exists
     * @param imageX - width of image
     * @param imageY - height of image
     * @param publisherEntity - author entity.
     */
    public Comment(String message, String publisher, String publisherID, String date, String postID, String image, String link,
                   String imageX, String imageY, String publisherEntity) {
        this.message = message;
        this.publisher = publisher;
        this.publisherID = publisherID;
        this.date = date;
        this.postID = postID;
        this.image = image;
        this.link = link;
        this.imageX = imageX;
        this.imageY = imageY;
        this.publisherEntity = publisherEntity;
    }

    /**
     * Constructor - init members.
     * @param message - text in comment
     * @param publisher - name of author
     * @param publisherID - id of author
     * @param date - when the comment created
     * @param postID - the post id containing it
     * @param image - image in comment if exists
     * @param link - link if exists
     * @param imageX - width of image
     * @param imageY - height of image
     * @param publisherEntity - author entity.
     */
    public Comment(String message, String publisher, String publisherID, String date, String postID, String id, String image,
                   String link, String imageX, String imageY, String publisherEntity) {

        this.message = message;
        this.publisher = publisher;
        this.publisherID = publisherID;
        this.date = date;
        this.postID = postID;
        this.id = id;
        this.image = image;
        this.link = link;
        this.imageX = imageX;
        this.imageY = imageY;
        this.publisherEntity = publisherEntity;
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
    public String getPublisher() {
        return publisher;
    }

    /**
     * set method
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * get method
     * @return
     */
    public String getPublisherID() {
        return publisherID;
    }

    /**
     * set metod
     * @param publisherID
     */
    public void setPublisherID(String publisherID) {
        this.publisherID = publisherID;
    }

    /**
     * get method
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * set method
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
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
}
