package com.ustc.app.studyabroad.models;

import java.util.List;

public class Program {

    private String name;
    private String index;
    private List<Category> categories;

    public Program(String name) {
        this.name = name;
    }

    public Program(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
