package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqListWorkflowTaskPageVo extends ReqPageVo {
	private String flowCode = null;// 流程Code
	private String userDisplayName = null;// 流程处理人姓名

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date startTimeStart = null;// 发生时间开始

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date startTimeEnd = null;// 发生时间结束

	@Deprecated
	public ReqListWorkflowTaskPageVo() {

	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public Date getStartTimeStart() {
		return startTimeStart;
	}

	public void setStartTimeStart(Date startTimeStart) {
		this.startTimeStart = startTimeStart;
	}

	public Date getStartTimeEnd() {
		return startTimeEnd;
	}

	public void setStartTimeEnd(Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}

}
