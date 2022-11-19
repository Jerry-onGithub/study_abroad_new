package com.ustc.app.studyabroad.models;

import java.util.HashMap;
import java.util.List;

public class Fields {

    HashMap<String, List<String>> listDataChild;
    List<String> listDataHeader;

    public Fields(List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
        this.listDataChild = listDataChild;
        this.listDataHeader = listDataHeader;
    }

    public HashMap<String, List<String>> getListDataChild() {
        return listDataChild;
    }

    public void setListDataChild(HashMap<String, List<String>> listDataChild) {
        this.listDataChild = listDataChild;
    }

    public List<String> getListDataHeader() {
        return listDataHeader;
    }

    public void setListDataHeader(List<String> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }
}
