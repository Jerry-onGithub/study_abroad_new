package com.ustc.app.studyabroad.userModels;

import java.io.Serializable;

public class Decisions implements Serializable {

    private String decisionId;
    private String userId;
    private String uni_name;
    private String major;
    private String app_date;
    private String dec_date;
    private String fund_amount;
    private String fund_type;
    private String status;

    public Decisions() {
    }

    public Decisions(String userId, String uni_name, String major, String app_date, String status) {
        this.userId = userId;
        this.uni_name = uni_name;
        this.major = major;
        this.app_date = app_date;
        this.status = status;
    }

    public Decisions(String userId, String uni_name, String major, String app_date, String dec_date, String fund_amount, String fund_type, String status) {
        this.userId = userId;
        this.uni_name = uni_name;
        this.major = major;
        this.app_date = app_date;
        this.dec_date = dec_date;
        this.fund_amount = fund_amount;
        this.fund_type = fund_type;
        this.status = status;
    }

    public Decisions(String userId, String uni_name, String major, String app_date, String dec_date, String status) {
        this.userId = userId;
        this.uni_name = uni_name;
        this.major = major;
        this.app_date = app_date;
        this.dec_date = dec_date;
        this.status = status;
    }

    public String getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUni_name() {
        return uni_name;
    }

    public void setUni_name(String uni_name) {
        this.uni_name = uni_name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getApp_date() {
        return app_date;
    }

    public void setApp_date(String app_date) {
        this.app_date = app_date;
    }

    public String getDec_date() {
        return dec_date;
    }

    public void setDec_date(String dec_date) {
        this.dec_date = dec_date;
    }

    public String getFund_amount() {
        return fund_amount;
    }

    public void setFund_amount(String fund_amount) {
        this.fund_amount = fund_amount;
    }

    public String getFund_type() {
        return fund_type;
    }

    public void setFund_type(String fund_type) {
        this.fund_type = fund_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
