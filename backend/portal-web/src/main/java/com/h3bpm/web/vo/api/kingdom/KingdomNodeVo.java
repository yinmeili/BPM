package com.h3bpm.web.vo.api.kingdom;

import com.h3bpm.web.entity.MonitorNode;
import com.h3bpm.web.entity.MonitorNodeHistory;
import com.h3bpm.web.enumeration.KingdomNodeStatus;

public class KingdomNodeVo implements Comparable<KingdomNodeVo> {
	private String name = null;
	private String status = null;
	private String executeResult = null;

	@Override // 升序
	public int compareTo(KingdomNodeVo o) {
		int thisIndex = 0;
		int index = 0;

		String[] thisNames = name.split("-");
		if (thisNames.length == 2) {
			thisIndex = Integer.parseInt(thisNames[0]);
		}

		String name = o.getName();
		String[] names = name.split("-");

		if (names.length == 2) {
			index = Integer.parseInt(names[0]);
		}

		return thisIndex - index;
	}

	public KingdomNodeVo() {
	}

	public KingdomNodeVo(String name, String status, String executeResult) {
		this.name = name;
		this.status = status;
		this.executeResult = executeResult;
	}

	public KingdomNodeVo(MonitorNode model) {
		this.name = model.getName();
		this.status = model.getStatus();
		this.executeResult = model.getExecuteResult();
	}
	
	public KingdomNodeVo(MonitorNodeHistory model) {
		this.name = model.getName();
		this.status = model.getStatus();
		this.executeResult = model.getExecuteResult();
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

	// public String getStatusStr() {
	// return statusStr;
	// }

	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
}
