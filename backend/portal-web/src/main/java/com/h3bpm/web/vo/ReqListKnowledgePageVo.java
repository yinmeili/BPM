package com.h3bpm.web.vo;

public class ReqListKnowledgePageVo extends ReqPageVo {
	private String flowCodes = null;// 流程Codes，多个以逗号分隔 如：“system_test,system_upgrade,test_online”
	private String name = null;// 知识名称
	private String tagName = null;// 标签
	private String startTimeStart = null;// 发生时间开始
	private String startTimeEnd = null;// 发生时间结束
	private String endTimeStart = null;// 结束时间开始
	private String endTimeEnd = null;// 结束时间结束

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

	public String getStartTimeStart() {
		return startTimeStart;
	}

	public void setStartTimeStart(String startTimeStart) {
		this.startTimeStart = startTimeStart;
	}

	public String getStartTimeEnd() {
		return startTimeEnd;
	}

	public void setStartTimeEnd(String startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}

	public String getEndTimeStart() {
		return endTimeStart;
	}

	public void setEndTimeStart(String endTimeStart) {
		this.endTimeStart = endTimeStart;
	}

	public String getEndTimeEnd() {
		return endTimeEnd;
	}

	public void setEndTimeEnd(String endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}

}
