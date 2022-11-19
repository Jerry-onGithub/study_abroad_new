package com.ustc.app.studyabroad.userModels;

public class Comment {
    private String postId;
    private String userId;
    private String commentId;
    private String name;
    private String commentMessage;
    private long likes;
    private long dislikes;
    private String time;

    public Comment() {
    }

    public Comment(String postId, String commentMessage, long likes, long dislikes, String time) {
        this.postId = postId;
        this.commentMessage = commentMessage;
        this.likes = likes;
        this.dislikes = dislikes;
        this.time = time;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
