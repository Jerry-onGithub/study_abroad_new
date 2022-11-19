package com.ustc.app.studyabroad.interfaces;

import java.util.List;

public interface ListStringCallback {

    void onSuccess(List<String> data);
    void onFailure(String msg);

}
