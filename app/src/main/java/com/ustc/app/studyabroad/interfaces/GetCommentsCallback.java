package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.userModels.Comment;
import com.ustc.app.studyabroad.userModels.Post;

import java.util.List;

public interface GetCommentsCallback {

    void onSuccess(List<Comment> comments);
    void onFailure(String message);

}
