package com.ustc.app.studyabroad.interfaces;

public interface PostReturnCallback {

    void onSuccess(String likes, String dislikes);
    void onFailure(String likes, String dislikes);

}
