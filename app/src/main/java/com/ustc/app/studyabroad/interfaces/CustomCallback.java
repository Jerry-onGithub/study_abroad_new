package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.University;

import java.util.List;

public interface CustomCallback {

    void onSuccess(List<University> universityList);
    void onFailure();

}
