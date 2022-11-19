package com.ustc.app.studyabroad.models;

import com.ustc.app.studyabroad.userModels.User;

import java.util.List;

public class RecentlyViewed {

    private String uni_index;
    private String time;
    private int visited_times;
    private List<String> visitors;

    public RecentlyViewed() {
    }

    public RecentlyViewed(String uni_index, String time, int visited_times, List<String> visitors) {
        this.uni_index = uni_index;
        this.time = time;
        this.visited_times = visited_times;
        this.visitors = visitors;
    }

    public RecentlyViewed(String uni_index, String time, int visited_times) {
        this.uni_index = uni_index;
        this.time = time;
        this.visited_times = visited_times;
    }

    public String getUni_index() {
        return uni_index;
    }

    public void setUni_index(String uni_index) {
        this.uni_index = uni_index;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getVisited_times() {
        return visited_times;
    }

    public void setVisited_times(int visited_times) {
        this.visited_times = visited_times;
    }

    public List<String> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<String> visitors) {
        this.visitors = visitors;
    }
}
