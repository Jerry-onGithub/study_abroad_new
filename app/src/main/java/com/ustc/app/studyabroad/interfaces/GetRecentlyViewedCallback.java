package com.ustc.app.studyabroad.interfaces;

import com.ustc.app.studyabroad.models.RecentlyViewed;

import java.util.List;

public interface GetRecentlyViewedCallback {

    void onSuccess(List<RecentlyViewed> rv);
    void onFailure(String str);

}
