package com.h3bpm.web.vo;

import java.util.Date;

import com.h3bpm.web.entity.File;

public class WorkCalendarVo {
	private String id = null;	//ObjectID流程ID
	private String title = null;//InstanceName流程名称
	private int status = 0;		//流程状态
	private Date start = null;
	private Date end = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
