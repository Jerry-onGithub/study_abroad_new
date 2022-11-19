package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.userModels.Decisions;
import com.ustc.app.studyabroad.userModels.Post;

import java.util.List;

public interface GetPostsCallback {

    void onSuccess(List<Post> posts);
    void onFailure(String message);

}
