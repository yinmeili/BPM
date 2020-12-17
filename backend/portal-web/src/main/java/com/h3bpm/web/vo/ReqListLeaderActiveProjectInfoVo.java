package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqListLeaderActiveProjectInfoVo extends ReqPageVo {
	private String leaderId = null;// 负责人ID

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime = null;// 发生时间开始

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime = null;// 结束时间开始

	@Deprecated
	public ReqListLeaderActiveProjectInfoVo() {

	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
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
