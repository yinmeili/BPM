package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqListBusinessExceptionPageVo extends ReqPageVo {
	private String businessSystem = null;// 交易系统，多个以逗号分隔 如：“system_test,system_upgrade,test_online”
	private String keyword = null;// 关键字
	private String userId = null;// 创建者用户ID

	@JsonFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date startTimeStart = null;// 发生时间开始

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date startTimeEnd = null;// 发生时间结束

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date endTimeStart = null;// 结束时间开始

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date endTimeEnd = null;// 结束时间结束

	@Deprecated
	public ReqListBusinessExceptionPageVo() {

	}

	public String getBusinessSystem() {
		return businessSystem;
	}

	public void setBusinessSystem(String businessSystem) {
		this.businessSystem = businessSystem;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Date getEndTimeStart() {
		return endTimeStart;
	}

	public void setEndTimeStart(Date endTimeStart) {
		this.endTimeStart = endTimeStart;
	}

	public Date getEndTimeEnd() {
		return endTimeEnd;
	}

	public void setEndTimeEnd(Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
}
