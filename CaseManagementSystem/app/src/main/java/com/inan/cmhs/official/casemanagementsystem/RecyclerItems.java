package com.inan.cmhs.official.casemanagementsystem;

public class RecyclerItems {
    String caseNo,name;
    long serialNo;

    public RecyclerItems(String caseNo, String name, long serialNo) {
        this.caseNo = caseNo;
        this.name = name;
        this.serialNo = serialNo;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }
}
