package com.h3bpm.web.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class WeeklyReportData implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Excel(name = "周报人员", orderNum = "0")
    private String userDisplayName;

    public WeeklyReportData() {
    }

    public WeeklyReportData(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    @Override
    public String toString() {
        return "WeeklyReportData{" +
                "userDisplayName='" + userDisplayName + '\'' +
                '}';
    }
}
