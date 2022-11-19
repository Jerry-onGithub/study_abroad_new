package com.ustc.app.studyabroad.models;

import java.util.List;

public class Countries {

    String name;
    List<University> universityList;

    public Countries(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<University> getUniversityList() {
        return universityList;
    }

    public void setUniversityList(List<University> universityList) {
        this.universityList = universityList;
    }
}
