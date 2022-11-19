package com.ustc.app.studyabroad.userModels;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable{
    private String postId;
    private String userId;
    private String username;
    private String photo;
    private String topic;
    private String message;
    private String location;
    private String time;
    private int likes;
    private long comments;

    public Post() {
    }

    public Post(String topic, String message, String location, String time, int likes, long comments) {
        this.topic = topic;
        this.message = message;
        this.location = location;
        this.time = time;
        this.likes = likes;
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
