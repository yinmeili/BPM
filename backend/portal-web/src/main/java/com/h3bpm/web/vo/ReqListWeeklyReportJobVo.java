package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import OThinker.Common.DateTimeUtil;

public class ReqListWeeklyReportJobVo {
	private String orgId = null; // 部门ID
	private int jobLevel = 0; // 工作等级

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startTime = null; // 开始时间

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endTime = null; // 结束时间

	@Deprecated
	public ReqListWeeklyReportJobVo() {

	}

	public int getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(int jobLevel) {
		this.jobLevel = jobLevel;
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
		// 页面接收的时间没有时分秒，将时分秒加大到该天的最后时刻
		if (this.endTime != null) {
			Date endTime = DateTimeUtil.addHours(this.endTime, 23);
			endTime = DateTimeUtil.addMinutes(endTime, 59);
			endTime = DateTimeUtil.addSeconds(endTime, 59);
			
			return endTime;
		}

		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
