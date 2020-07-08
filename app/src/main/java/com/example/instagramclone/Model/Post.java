package com.example.instagramclone.Model;

public class Post {

    private String description;
    private String publisher;
    private String imageurl;
    private String postid;

    public Post(String description, String publisher, String imageurl, String postid) {
        this.description = description;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.postid = postid;
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
