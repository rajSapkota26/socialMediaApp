package com.technoabinash.mig33.model;

public class Post {
    private  String postId;
    private  String postImage;
    private  String postDescription;
    private  String postPublisher;

    public Post() {
    }

    public Post(String postId, String postImage, String postDescription, String postPublisher) {
        this.postId = postId;
        this.postImage = postImage;
        this.postDescription = postDescription;
        this.postPublisher = postPublisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostPublisher() {
        return postPublisher;
    }

    public void setPostPublisher(String postPublisher) {
        this.postPublisher = postPublisher;
    }
}
