package com.h3bpm.web.vo;

import java.util.List;

public class ResponseWorkCalendarVo {
	private int finishTotal = 0;
	private int unfinishTotal = 0;
	private int exceedTimeLimitTotal = 0;
	private int unReadTotal = 0;

	private List<WorkCalendarVo> workItemList = null;

	@Deprecated
	public ResponseWorkCalendarVo() {

	}

	public ResponseWorkCalendarVo(List<WorkCalendarVo> workItemList) {
		this.workItemList = workItemList;
	}

	public int getFinishTotal() {
		return finishTotal;
	}

	public void setFinishTotal(int finishTotal) {
		this.finishTotal = finishTotal;
	}

	public int getUnfinishTotal() {
		return unfinishTotal;
	}

	public void setUnfinishTotal(int unfinishTotal) {
		this.unfinishTotal = unfinishTotal;
	}

	public int getExceedTimeLimitTotal() {
		return exceedTimeLimitTotal;
	}

	public void setExceedTimeLimitTotal(int exceedTimeLimitTotal) {
		this.exceedTimeLimitTotal = exceedTimeLimitTotal;
	}

	public Object getWorkItemList() {
		return workItemList;
	}

	public void setWorkItemList(List<WorkCalendarVo> workItemList) {
		this.workItemList = workItemList;
	}

	public int getUnReadTotal() {
		return unReadTotal;
	}

	public void setUnReadTotal(int unReadTotal) {
		this.unReadTotal = unReadTotal;
	}
}
