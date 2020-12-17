package com.h3bpm.web.entity;

public class BizObjectInfo {
	private String id = null;
	private String workflowCode = null;
	private String title = null;
	private String businessSys = null;

	public String getBusinessSys() {
		return businessSys;
	}

	public void setBusinessSys(String businessSys) {
		this.businessSys = businessSys;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}
}
