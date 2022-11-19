package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.Category;
import com.ustc.app.studyabroad.models.Program;

import java.util.List;

public interface CustomCallbackGetCat {

    void onSuccess(List<Category> categoryListList);
    void onFailure();

}
