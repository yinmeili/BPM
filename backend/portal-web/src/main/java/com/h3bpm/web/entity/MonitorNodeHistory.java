package com.h3bpm.web.entity;

import java.util.Date;

import com.h3bpm.web.vo.api.kingdom.KingdomNodeVo;

import OThinker.Common.DateTimeUtil;

public class MonitorNodeHistory {

	private String id = null;
	private String name = null;
	private String status = null;
	private String executeResult = null;
	private String monitorType = null;
	private Date monitorDate = null;

	public MonitorNodeHistory(KingdomNodeVo voBean, String moniterName) {
		this.name = voBean.getName();
		this.status = voBean.getStatus();
		this.executeResult = voBean.getExecuteResult();
		this.monitorType = moniterName;

		// 只取当前时间的 年月日
		this.monitorDate = DateTimeUtil.parse(DateTimeUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
	}

	public MonitorNodeHistory() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public Date getMonitorDate() {
		return monitorDate;
	}

	public void setMonitorDate(Date monitorDate) {
		this.monitorDate = monitorDate;
	}
}
