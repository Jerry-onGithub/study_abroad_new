package com.ustc.app.studyabroad.models;

public class Category {

    private String cat_name;
    private String cat_content;
    private String male;
    private String female;
    private String int_stud;
    private String toefl_mini;
    private String toefl_mean;
    private String ielts_mini;
    private String gre_verbal;
    private String gre_quant;
    private String gre_awa;
    private String fellowship;
    private String teaching_assist;
    private String research_assist;
    private String masters_acc_rate;
    private String phd_acc_rate;
    private String tuition;
    private String living_exp;
    private String financial_aid_officer;
    private String financial_aid_officer_contact;
    private String us_app_fee;
    private String us_app_deadline;
    private String int_app_fee;
    private String int_app_deadline;
    private String acc_rate;
    private String stud_rec_aid;
    private String private_;
    private String public_;
    private String bar_passage_rate;
    private String employed;
    private String total_enrollment;
    private String ave_gpa;
    private String gmat;
    private String lsat;

    public Category(String cat_name, String cat_content, String male, String female, String int_stud, String toefl_mini, String toefl_mean, String ielts_mini, String gre_verbal, String gre_quant, String gre_awa, String fellowship, String teaching_assist, String research_assist, String masters_acc_rate, String phd_acc_rate, String tuition, String living_exp, String financial_aid_officer, String financial_aid_officer_contact, String us_app_fee, String us_app_deadline, String int_app_fee, String int_app_deadline, String acc_rate, String stud_rec_aid, String private_, String public_, String bar_passage_rate, String employed, String total_enrollment, String ave_gpa, String gmat, String lsat) {
        this.cat_name = cat_name;
        this.cat_content = cat_content;
        this.male = male;
        this.female = female;
        this.int_stud = int_stud;
        this.toefl_mini = toefl_mini;
        this.toefl_mean = toefl_mean;
        this.ielts_mini = ielts_mini;
        this.gre_verbal = gre_verbal;
        this.gre_quant = gre_quant;
        this.gre_awa = gre_awa;
        this.fellowship = fellowship;
        this.teaching_assist = teaching_assist;
        this.research_assist = research_assist;
        this.masters_acc_rate = masters_acc_rate;
        this.phd_acc_rate = phd_acc_rate;
        this.tuition = tuition;
        this.living_exp = living_exp;
        this.financial_aid_officer = financial_aid_officer;
        this.financial_aid_officer_contact = financial_aid_officer_contact;
        this.us_app_fee = us_app_fee;
        this.us_app_deadline = us_app_deadline;
        this.int_app_fee = int_app_fee;
        this.int_app_deadline = int_app_deadline;
        this.acc_rate = acc_rate;
        this.stud_rec_aid = stud_rec_aid;
        this.private_ = private_;
        this.public_ = public_;
        this.bar_passage_rate = bar_passage_rate;
        this.employed = employed;
        this.total_enrollment = total_enrollment;
        this.ave_gpa = ave_gpa;
        this.gmat = gmat;
        this.lsat = lsat;
    }

    public Category(String cat_name, String cat_content) {
        this.cat_name = cat_name;
        this.cat_content = cat_content;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_content() {
        return cat_content;
    }

    public void setCat_content(String cat_content) {
        this.cat_content = cat_content;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    public String getInt_stud() {
        return int_stud;
    }

    public void setInt_stud(String int_stud) {
        this.int_stud = int_stud;
    }

    public String getToefl_mini() {
        return toefl_mini;
    }

    public void setToefl_mini(String toefl_mini) {
        this.toefl_mini = toefl_mini;
    }

    public String getToefl_mean() {
        return toefl_mean;
    }

    public void setToefl_mean(String toefl_mean) {
        this.toefl_mean = toefl_mean;
    }

    public String getIelts_mini() {
        return ielts_mini;
    }

    public void setIelts_mini(String ielts_mini) {
        this.ielts_mini = ielts_mini;
    }

    public String getGre_verbal() {
        return gre_verbal;
    }

    public void setGre_verbal(String gre_verbal) {
        this.gre_verbal = gre_verbal;
    }

    public String getGre_quant() {
        return gre_quant;
    }

    public void setGre_quant(String gre_quant) {
        this.gre_quant = gre_quant;
    }

    public String getGre_awa() {
        return gre_awa;
    }

    public void setGre_awa(String gre_awa) {
        this.gre_awa = gre_awa;
    }

    public String getFellowship() {
        return fellowship;
    }

    public void setFellowship(String fellowship) {
        this.fellowship = fellowship;
    }

    public String getTeaching_assist() {
        return teaching_assist;
    }

    public void setTeaching_assist(String teaching_assist) {
        this.teaching_assist = teaching_assist;
    }

    public String getResearch_assist() {
        return research_assist;
    }

    public void setResearch_assist(String research_assist) {
        this.research_assist = research_assist;
    }

    public String getMasters_acc_rate() {
        return masters_acc_rate;
    }

    public void setMasters_acc_rate(String masters_acc_rate) {
        this.masters_acc_rate = masters_acc_rate;
    }

    public String getPhd_acc_rate() {
        return phd_acc_rate;
    }

    public void setPhd_acc_rate(String phd_acc_rate) {
        this.phd_acc_rate = phd_acc_rate;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getLiving_exp() {
        return living_exp;
    }

    public void setLiving_exp(String living_exp) {
        this.living_exp = living_exp;
    }

    public String getFinancial_aid_officer() {
        return financial_aid_officer;
    }

    public void setFinancial_aid_officer(String financial_aid_officer) {
        this.financial_aid_officer = financial_aid_officer;
    }

    public String getFinancial_aid_officer_contact() {
        return financial_aid_officer_contact;
    }

    public void setFinancial_aid_officer_contact(String financial_aid_officer_contact) {
        this.financial_aid_officer_contact = financial_aid_officer_contact;
    }

    public String getUs_app_fee() {
        return us_app_fee;
    }

    public void setUs_app_fee(String us_app_fee) {
        this.us_app_fee = us_app_fee;
    }

    public String getUs_app_deadline() {
        return us_app_deadline;
    }

    public void setUs_app_deadline(String us_app_deadline) {
        this.us_app_deadline = us_app_deadline;
    }

    public String getInt_app_fee() {
        return int_app_fee;
    }

    public void setInt_app_fee(String int_app_fee) {
        this.int_app_fee = int_app_fee;
    }

    public String getInt_app_deadline() {
        return int_app_deadline;
    }

    public void setInt_app_deadline(String int_app_deadline) {
        this.int_app_deadline = int_app_deadline;
    }

    public String getAcc_rate() {
        return acc_rate;
    }

    public void setAcc_rate(String acc_rate) {
        this.acc_rate = acc_rate;
    }

    public String getStud_rec_aid() {
        return stud_rec_aid;
    }

    public void setStud_rec_aid(String stud_rec_aid) {
        this.stud_rec_aid = stud_rec_aid;
    }

    public String getPrivate_() {
        return private_;
    }

    public void setPrivate_(String private_) {
        this.private_ = private_;
    }

    public String getPublic_() {
        return public_;
    }

    public void setPublic_(String public_) {
        this.public_ = public_;
    }

    public String getBar_passage_rate() {
        return bar_passage_rate;
    }

    public void setBar_passage_rate(String bar_passage_rate) {
        this.bar_passage_rate = bar_passage_rate;
    }

    public String getEmployed() {
        return employed;
    }

    public void setEmployed(String employed) {
        this.employed = employed;
    }

    public String getTotal_enrollment() {
        return total_enrollment;
    }

    public void setTotal_enrollment(String total_enrollment) {
        this.total_enrollment = total_enrollment;
    }

    public String getAve_gpa() {
        return ave_gpa;
    }

    public void setAve_gpa(String ave_gpa) {
        this.ave_gpa = ave_gpa;
    }

    public String getGmat() {
        return gmat;
    }

    public void setGmat(String gmat) {
        this.gmat = gmat;
    }

    public String getLsat() {
        return lsat;
    }

    public void setLsat(String lsat) {
        this.lsat = lsat;
    }
}
