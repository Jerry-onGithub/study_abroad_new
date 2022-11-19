package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.Program;

import java.util.List;

public interface CustomCallbackGetProg {

    void onSuccess(List<Program> programList);
    void onFailure();

}
