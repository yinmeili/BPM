package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqCreateProjectInfo {
	private String name;
	private String desc;
	private String leaderId;
	private String city;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
