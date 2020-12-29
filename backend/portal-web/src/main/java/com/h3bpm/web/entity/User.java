package com.h3bpm.web.entity;

public class User {
	private String id = null;
	private String name = null;
	private String loginName = null;
	private String mobile = null;

	public User() {

	}

	public User(OThinker.Common.Organization.Models.User appModel) {
		this.id = appModel._ObjectID;
		this.name = appModel._Name;
		this.loginName = appModel.getCode();
		this.mobile = appModel.getMobile();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
}
