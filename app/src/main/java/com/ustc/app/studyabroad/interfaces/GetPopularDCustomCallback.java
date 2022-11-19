package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.PopularD;
import com.ustc.app.studyabroad.models.University;

import java.util.List;

public interface GetPopularDCustomCallback {

    void onSuccess(List<PopularD> list);
    void onFailure();

}
