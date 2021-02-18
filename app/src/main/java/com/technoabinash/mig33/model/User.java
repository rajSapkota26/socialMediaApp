package com.technoabinash.mig33.model;

public class User {
    private String username;
    private String fullName;
    private String email;
    private String Bio;
    private String ImageUrl;
    private String id;

    public User(String username, String fullName, String email, String bio, String imageUrl, String id) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        Bio = bio;
        ImageUrl = imageUrl;
        this.id = id;
    }

    public User() {
    }

    public User(String id, String imageUrl, String email) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
