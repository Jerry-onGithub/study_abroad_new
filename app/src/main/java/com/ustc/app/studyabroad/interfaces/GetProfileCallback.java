package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.University;
import com.ustc.app.studyabroad.userModels.Profile;

import java.util.List;

public interface GetProfileCallback {

    void onSuccess(Profile profile);
    void onFailure(String error);

}
