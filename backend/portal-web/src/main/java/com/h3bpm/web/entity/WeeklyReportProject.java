package com.h3bpm.web.entity;

public class WeeklyReportProject {
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

	public WeeklyReportProject() {

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

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
