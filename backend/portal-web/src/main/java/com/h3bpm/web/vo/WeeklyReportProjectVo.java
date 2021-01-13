package com.h3bpm.web.vo;

import com.h3bpm.web.entity.WeeklyReportProject;

public class WeeklyReportProjectVo {
	private String id = null;
	private String parentId = null;
	private String userId = null;
	private String userName = null;
	private String name = null;
	private String info = null;
	private String evolve = null;
	private String ratio = null;
	private String org = null;
	private String remark = null;

	public WeeklyReportProjectVo() {

	}

	public WeeklyReportProjectVo(WeeklyReportProject model) {
		this.id = model.getId();
		this.parentId = model.getParentId();
		this.userId = model.getUserId();
		this.userName = model.getUserName();
		this.name = model.getName();
		this.info = model.getInfo();
		this.evolve = model.getEvolve();
		this.ratio = model.getRatio();
		this.org = model.getOrg();
		this.remark = model.getRemark();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getEvolve() {
		return evolve;
	}

	public void setEvolve(String evolve) {
		this.evolve = evolve;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
