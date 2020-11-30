package com.h3bpm.web.entity;

import com.h3bpm.web.enumeration.KingdomNodeStatus;
import com.h3bpm.web.vo.api.kingdom.KingdomNodeVo;

public class MonitorNode extends ChildrenTable {

	private String name = null;
	private String status = null;
	private String executeResult = null;
	private String desc = null;

	public MonitorNode() {

	}

	public MonitorNode(KingdomNodeVo voBean, String flowId, int index, String monitorNodeName) {
		this.name = voBean.getName();
		this.status = voBean.getStatus();
		this.executeResult = voBean.getExecuteResult();
		this.setParentIndex(index);
		this.setParentPropertyName(monitorNodeName);
		this.setParentObjectID(flowId);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExecuteResult() {
		return executeResult;
	}

	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
}
