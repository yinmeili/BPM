package com.h3bpm.web.vo;

import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.WorkflowCode;

/**
 * Created by tonghao on 2020/10/13.
 */
public class WorkFlowTaskVo {

	private String id = null;
	private String instanceId = null;
	private String workflowCode = null;
	private String userLoginName = null;
	private String userDisplayName = null;
	private Long createTime = null;
	private Long startTime = null;
	private Long endTime = null;
	private int executeType = 0;

	public WorkFlowTaskVo() {
	}

	public WorkFlowTaskVo(WorkFlowTask model) {
		this.id = model.getId();
		this.instanceId = model.getInstanceId();
		this.workflowCode = model.getWorkFlowCode();
		this.userLoginName = model.getUserLoginName();
		this.userDisplayName = model.getUserDisplayName();
		this.executeType = model.getExecuteType();

		if (model.getCreateTime() != null) {
			this.createTime = model.getCreateTime().getTime();
		}

		if (model.getStartTime() != null) {
			this.startTime = model.getStartTime().getTime();
		}

		if (model.getEndTime() != null) {
			this.endTime = model.getEndTime().getTime();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public int getExecuteType() {
		return executeType;
	}

	public void setExecuteType(int executeType) {
		this.executeType = executeType;
	}

	public String getWorkflowCodeStr() {
		if (WorkflowCode.parse(this.workflowCode) != null) {
			return WorkflowCode.parse(this.workflowCode).getDisplayName();
		}
		return this.workflowCode;
	}

}
