package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.userModels.Decisions;

import java.util.List;

public interface GetDecisionsCallback {

    void onSuccess(List<Decisions> decisions);
    void onFailure(String message);

}
