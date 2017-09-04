package com.weallone.raz.together.Entities;

import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a post in post fragment
 */
public class Post implements Serializable{
    private String title;
    private String message;
    private String publisher;
    private String publisherID;
    private String publisherEntity;
    private String publisherIcon;
    private String date;
    private Boolean commentsCreated = false;
    private Boolean answered = false;
    private Boolean isPublic;
    private int shown_comments = 0;
    private String link = "";
    private String imageX = "";
    private String imageY = "";
    private Integer overallComments = 0;


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

    private String image = "";

    /**
     * get method
     * @return
     */
    public Boolean getAnswered() {
        return answered;
    }

    /**
     * set method
     * @param answered
     */
    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    private String searched = "";
    private Integer color = null;

    /**
     * get method
     * @return
     */
    public String getSearched() {
        return searched;
    }

    /**
     * get method
     * @return
     */
    public Integer getColor() {
        return color;
    }

    /**
     * set method
     * @param color
     */
    public void setColor(Integer color) {
        this.color = color;
    }

    /**
     * set method
     * @param searched
     */
    public void setSearched(String searched) {
        this.searched = searched;
    }

    /**
     * get method
     * @return
     */
    public int getShown_comments() {
        return shown_comments;
    }

    /**
     * set method
     * @param shown_comments
     */
    public void setShown_comments(int shown_comments) {
        this.shown_comments = shown_comments;
    }

    /**
     * get method
     * @return
     */
    public Boolean getIsPublic() {
        return isPublic;
    }

    /**
     * set method
     * @param isPublic
     */
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

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

    /**
     * get method
     * @return
     */
    public String getPublisherIcon() {
        return publisherIcon;
    }

    /**
     * set method
     * @param publisherIcon
     */
    public void setPublisherIcon(String publisherIcon) {
        this.publisherIcon = publisherIcon;
    }

    public Integer getOverallComments() {
        return overallComments;
    }

    public void setOverallComments(Integer overallComments) {
        this.overallComments = overallComments;
    }

    /**
     * Copy constructor. deep copy.
     * @param post to copy from
     */
    public Post(Post post) {
        this.title = post.getTitle();
        this.message = post.getMessage();
        this.publisher = post.getPublisher();
        this.publisherID = post.getPublisherID();
        this.date = post.getDate();
        this.id = post.getId();
        this.isPublic = post.getIsPublic();
        this.comments = post.getComments();
        this.shown_comments = post.getShown_comments();
        this.color = post.getColor();
        this.answered = post.getAnswered();
        this.image = post.getImage();
        this.link = post.getLink();
        this.imageX = post.imageX;
        this.imageY = post.imageY;
        this.publisherEntity = post.getPublisherEntity();
        this.publisherIcon = post.getPublisherIcon();
        this.overallComments = post.overallComments;


    }

    /**
     * get method
     * @return
     */
    public Boolean getCommentsCreated() {
        return commentsCreated;
    }

    /**
     * set method
     * @param commentsCreated
     */
    public void setCommentsCreated(Boolean commentsCreated) {
        this.commentsCreated = commentsCreated;
    }

    private ListView commentsListView = null;

    /**
     * toString() override
     * @return
     */
    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publisherID='" + publisherID + '\'' +
                ", date='" + date + '\'' +
                ", commentsCreated=" + commentsCreated +
                ", isPublic=" + isPublic +
                ", commentsListView=" + commentsListView +
                ", id='" + id + '\'' +
                ", comments=" + comments +
                '}';
    }

    /**
     * get method
     * @return
     */
    public ListView getCommentsListView() {
        return commentsListView;
    }

    /**
     * set method
     * @param commentsListView
     */
    public void setCommentsListView(ListView commentsListView) {
        this.commentsListView = commentsListView;
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
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * set method
     * @param comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    private String id;
    private List<Comment> comments = new ArrayList<>();

    /**
     * Construcotr - init members
     * @param title - title of post
     * @param message - content of psot
     * @param publisher - name of author
     * @param publisherID - id of author
     * @param date - when created
     * @param id - given by server
     * @param isPublic - public/private
     * @param color - of background
     * @param answered - did psychologist answer it yet
     * @param image - image of exists
     * @param link - link if exists
     * @param imageX - width
     * @param imageY - height
     * @param publisherEntity - author entity
     * @param publisherIcon - author icon
     * @param overallComments - of post
     */
    public Post(String title, String message, String publisher, String publisherID, String date,
                String id, String isPublic, Integer color,Boolean answered, String image, String link,
                String imageX, String imageY, String publisherEntity, String publisherIcon, Integer overallComments) {
        this.title = title;
        this.message = message;
        this.publisher = publisher;
        this.publisherID = publisherID;
        this.date = date;
        this.answered = answered;
        this.id = id;
        this.isPublic = (isPublic.equals("true"))?true:false;
        this.color = color;
        this.image = image;
        this.link = link;
        this.imageX = imageX;
        this.imageY = imageY;
        this.publisherEntity = publisherEntity;
        this.publisherIcon = publisherIcon;
        this.overallComments = overallComments;
    }

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
     * Construcotr - init members
     * @param title - title of post
     * @param message - content of psot
     * @param publisher - name of author
     * @param publisherID - id of author
     * @param date - when created
     * @param isPublic - public/private
     * @param color - of background
     * @param answered - did psychologist answer it yet
     * @param image - image of exists
     * @param link - link if exists
     * @param imageX - width
     * @param imageY - height
     * @param publisherEntity - author entity
     * @param publisherIcon - author icon
    *  @param overallComments- of post
     */
    public Post(String title, String message, String publisher, String publisherID, String date,
                String isPublic, Integer color,Boolean answered, String image, String link,
                String imageX, String imageY, String publisherEntity, String publisherIcon, Integer overallComments) {
        this.title = title;
        this.message = message;
        this.publisher = publisher;
        this.publisherID = publisherID;
        this.date = date;
        this.isPublic = (isPublic.equals("true"))?true:false;
        this.color = color;
        this.answered = answered;
        this.image = image;
        this.link = link;
        this.imageX = imageX;
        this.imageY = imageY;
        this.publisherEntity = publisherEntity;
        this.publisherIcon = publisherIcon;
        this.overallComments = overallComments;
    }

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
     * set method
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
}
