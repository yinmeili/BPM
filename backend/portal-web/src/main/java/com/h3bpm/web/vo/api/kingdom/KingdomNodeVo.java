package com.h3bpm.web.vo.api.kingdom;

import com.h3bpm.web.enumeration.KingdomNodeStatus;

public class KingdomNodeVo {
	private String name = null;
	private String status = null;
	private String executeResult = null;
	private String statusStr = null;

	public KingdomNodeVo() {
	}

	public KingdomNodeVo(String name, String status, String executeResult) {
		this.name = name;
		this.status = status;
		this.executeResult = executeResult;
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

	// public String getStatusStr() {
	// return KingdomNodeStatus.parse(status).getDisplayName();
	// }

	public String getExecuteResult() {
		return executeResult;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
}
