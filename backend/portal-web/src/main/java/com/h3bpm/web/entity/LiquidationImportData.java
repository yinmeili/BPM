package com.h3bpm.web.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class LiquidationImportData implements java.io.Serializable{

    @Excel(name = "清算日期", orderNum = "0")
    private Date startTime;

    @Excel(name = "值班人员", orderNum = "1")
    private String userDisplayName;

    public LiquidationImportData() {
    }

    public LiquidationImportData(Date startTime, String userDisplayName) {
        this.startTime = startTime;
        this.userDisplayName = userDisplayName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "startTime='" + startTime + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                '}';
    }
}
