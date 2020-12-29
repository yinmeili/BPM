package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqListOrgWeeklyReportPageVo extends ReqPageVo {
	private String userId = null;// 查看用户ID
	private String orgId = null;// 部门ID

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date startTime = null;// 开始时间

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date endTime = null;// 截止时间

	@Deprecated
	public ReqListOrgWeeklyReportPageVo() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
