package com.ustc.app.studyabroad.userModels;

public class Profile {
    private String userId;
    private String pic;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String country;
    private String address;
    private String about;
    private String website;
    private String gender;
    private String birthday;
    private String degree_level;
    private String edu_country;
    private String edu_place_name;
    private String cgpa;
    private String research_paper;
    private String work_exp;
    private String target_uni_name;
    private String target_major;
    private String interested_term;
    private String interested_year;
    private String test_type;
    private String test_score;
    private String language_test_type;
    private String language_test_score;
    private String resume;

    public Profile() {
    }

    public Profile(String userId, String userName, String email, String firstName, String lastName, String phoneNumber, String country, String address, String about, String website, String gender, String birthday, String degree_level, String edu_country, String edu_place_name, String cgpa, String research_paper, String work_exp, String target_uni_name, String target_major, String interested_term, String interested_year, String test_type, String test_score, String language_test_type, String language_test_score) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.address = address;
        this.about = about;
        this.website = website;
        this.gender = gender;
        this.birthday = birthday;
        this.degree_level = degree_level;
        this.edu_country = edu_country;
        this.edu_place_name = edu_place_name;
        this.cgpa = cgpa;
        this.research_paper = research_paper;
        this.work_exp = work_exp;
        this.target_uni_name = target_uni_name;
        this.target_major = target_major;
        this.interested_term = interested_term;
        this.interested_year = interested_year;
        this.test_type = test_type;
        this.test_score = test_score;
        this.language_test_type = language_test_type;
        this.language_test_score = language_test_score;
    }

    public Profile(String userId, String pic, String userName, String email, String firstName, String lastName, String phoneNumber, String country, String address, String about, String website, String gender, String birthday, String degree_level, String edu_country, String edu_place_name, String cgpa, String research_paper, String work_exp, String target_uni_name, String target_major, String interested_term, String interested_year, String test_type, String test_score, String language_test_type, String language_test_score, String resume) {
        this.userId = userId;
        this.pic = pic;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.address = address;
        this.about = about;
        this.website = website;
        this.gender = gender;
        this.birthday = birthday;
        this.degree_level = degree_level;
        this.edu_country = edu_country;
        this.edu_place_name = edu_place_name;
        this.cgpa = cgpa;
        this.research_paper = research_paper;
        this.work_exp = work_exp;
        this.target_uni_name = target_uni_name;
        this.target_major = target_major;
        this.interested_term = interested_term;
        this.interested_year = interested_year;
        this.test_type = test_type;
        this.test_score = test_score;
        this.language_test_type = language_test_type;
        this.language_test_score = language_test_score;
        this.resume = resume;
    }

    public Profile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDegree_level() {
        return degree_level;
    }

    public void setDegree_level(String degree_level) {
        this.degree_level = degree_level;
    }

    public String getEdu_country() {
        return edu_country;
    }

    public void setEdu_country(String edu_country) {
        this.edu_country = edu_country;
    }

    public String getEdu_place_name() {
        return edu_place_name;
    }

    public void setEdu_place_name(String edu_place_name) {
        this.edu_place_name = edu_place_name;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getResearch_paper() {
        return research_paper;
    }

    public void setResearch_paper(String research_paper) {
        this.research_paper = research_paper;
    }

    public String getWork_exp() {
        return work_exp;
    }

    public void setWork_exp(String work_exp) {
        this.work_exp = work_exp;
    }

    public String getTarget_uni_name() {
        return target_uni_name;
    }

    public void setTarget_uni_name(String target_uni_name) {
        this.target_uni_name = target_uni_name;
    }

    public String getTarget_major() {
        return target_major;
    }

    public void setTarget_major(String target_major) {
        this.target_major = target_major;
    }

    public String getInterested_term() {
        return interested_term;
    }

    public void setInterested_term(String interested_term) {
        this.interested_term = interested_term;
    }

    public String getInterested_year() {
        return interested_year;
    }

    public void setInterested_year(String interested_year) {
        this.interested_year = interested_year;
    }

    public String getTest_type() {
        return test_type;
    }

    public void setTest_type(String test_type) {
        this.test_type = test_type;
    }

    public String getTest_score() {
        return test_score;
    }

    public void setTest_score(String test_score) {
        this.test_score = test_score;
    }

    public String getLanguage_test_type() {
        return language_test_type;
    }

    public void setLanguage_test_type(String language_test_type) {
        this.language_test_type = language_test_type;
    }

    public String getLanguage_test_score() {
        return language_test_score;
    }

    public void setLanguage_test_score(String language_test_score) {
        this.language_test_score = language_test_score;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
