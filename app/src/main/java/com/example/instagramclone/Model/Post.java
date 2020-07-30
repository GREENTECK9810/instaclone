package com.example.instagramclone.Model;

public class Post {

    private String description;
    private String publisher;
    private String imageurl;
    private String postid;
    private int likescount;
    private int commentscount;
    private Long createdAt;

    public int getLikescount() {
        return likescount;
    }

    public int getCommentscount() {
        return commentscount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
