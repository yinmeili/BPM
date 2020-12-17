package com.h3bpm.web.entity;

import java.util.Date;

import com.h3bpm.web.vo.ProjectVo;

public class ProjectInfo {
	private String id = null;
	private String name = null;
	private String desc = null;
	private String leaderId = null;
	private String leaderName = null;
	private Date createTime = null;
	private Date startTime = null;
	private Date endTime = null;

	@Deprecated
	public ProjectInfo() {

	}
	
	public ProjectInfo(ProjectVo voBean) {
		this.id = voBean.getId();
		this.name = voBean.getName();
		this.desc = voBean.getDesc();
		this.leaderId = voBean.getLeaderId();
		this.leaderName = voBean.getLeaderName();
		this.startTime = voBean.getStartTime();
		this.endTime = voBean.getEndTime();
		this.createTime = voBean.getCreateTime();
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
