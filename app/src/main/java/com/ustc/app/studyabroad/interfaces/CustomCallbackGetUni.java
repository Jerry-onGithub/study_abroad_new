package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.University;

public interface CustomCallbackGetUni {

    void onSuccess(University university);
    void onFailure();

}
