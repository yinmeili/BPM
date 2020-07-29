package com.h3bpm.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReqListKnowledgePageVo extends ReqPageVo {
	private String flowCodes = null;// 流程Codes，多个以逗号分隔 如：“system_test,system_upgrade,test_online”
	private String name = null;// 知识名称
	private String tagName = null;// 标签

	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date startTimeStart = null;// 发生时间开始

	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date startTimeEnd = null;// 发生时间结束

	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date endTimeStart = null;// 结束时间开始

	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date endTimeEnd = null;// 结束时间结束

	@Deprecated
	public ReqListKnowledgePageVo() {

	}

	public String getFlowCodes() {
		return flowCodes;
	}

	public void setFlowCodes(String flowCodes) {
		this.flowCodes = flowCodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
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
