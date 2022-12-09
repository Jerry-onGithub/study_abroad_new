package com.ustc.app.studyabroad.models;

import java.util.List;

public class PopularD {

    private String name;
    private String index;
    private Category category;
    //private List<Category> category;

    public PopularD(String name, String index, Category category) {//List<Category> category) {
        this.name = name;
        this.index = index;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
/*    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }*/
}
