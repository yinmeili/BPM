package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqListProjectInfoPageVo extends ReqPageVo {
	private String keyword = null;// 关键字
	private String leaderId = null;// 负责人ID
	private String city = null;// 城市

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTimeStart = null;// 发生时间开始

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTimeEnd = null;// 发生时间结束

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTimeStart = null;// 结束时间开始

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTimeEnd = null;// 结束时间结束

	@Deprecated
	public ReqListProjectInfoPageVo() {

	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
