package com.h3bpm.web.vo;

public class RespListKnowledgePageVo extends ReqPageVo {
	private String id = null;// 知识id
	private String flowId = null;// 流程id
	private String flowCodeStr = null;// 流程Code，中文描述
	private String name = null;// 知识名称
	private String tagName = null;// 标签
	private String startTime = null;// 发生时间
	private String endTime = null;// 结束时间
	private String createUserName = null;// 上传人

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowCodeStr() {
		return flowCodeStr;
	}

	public void setFlowCodeStr(String flowCodeStr) {
		this.flowCodeStr = flowCodeStr;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
