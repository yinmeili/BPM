package com.h3bpm.web.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class WeeklyReportSendData implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Excel(name = "姓名", orderNum = "0")
	private String userDisplayName;

	@Excel(name = "登录名", orderNum = "1")
	private String loginName;

	@Excel(name = "部门", orderNum = "2")
	private String orgName;

	@Excel(name = "工作级别", orderNum = "3")
	private int jobLevel;

	public WeeklyReportSendData() {
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(int jobLevel) {
		this.jobLevel = jobLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
