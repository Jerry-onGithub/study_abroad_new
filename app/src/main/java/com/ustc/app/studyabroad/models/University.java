package com.ustc.app.studyabroad.models;


import java.util.List;

public class University {

    private String imgUrl;
    private String name;
    private String address;
    private String uniId;
    private String program;
    private String country;
    private String rank;
    private String uni_type;
    private String total_stud;
    private String total_int_stud;
    private String index;
    private String content;
    private List<Category> categories;

    public University() { }

    public University(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public University(String name, String imgUrl, String country, String rank, String index, String address) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.country = country;
        this.rank = rank;
        this.index = index;
        this.address = address;
    }

    public University(String name, String address, String imgUrl, String index) {
        this.name = name;
        this.address = address;
        this.imgUrl = imgUrl;
        this.index = index;
    }

    public University(String name, String address, String imgUrl, String program, String index) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.address = address;
        this.program = program;
        this.index = index;
    }
    public University(String name, String address, String country, String rank, String uni_type, String total_stud, String total_int_stud, String index, String content, List<Category> categories) {
        this.name=name;
        this.address = address;
        this.country = country;
        this.rank = rank;
        this.uni_type = uni_type;
        this.total_stud = total_stud;
        this.total_int_stud = total_int_stud;
        this.index = index;
        this.content = content;
        this.categories = categories;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUni_type() {
        return uni_type;
    }

    public void setUni_type(String uni_type) {
        this.uni_type = uni_type;
    }

    public String getTotal_stud() {
        return total_stud;
    }

    public void setTotal_stud(String total_stud) {
        this.total_stud = total_stud;
    }

    public String getTotal_int_stud() {
        return total_int_stud;
    }

    public void setTotal_int_stud(String total_int_stud) {
        this.total_int_stud = total_int_stud;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUniId() {
        return uniId;
    }

    public void setUniId(String uniId) {
        this.uniId = uniId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
