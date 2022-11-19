package com.ustc.app.studyabroad.userModels;

public class Login {

    private String userId;
    private String userEmail;
    private String phoneNumber;
    private String password;

    public Login(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
