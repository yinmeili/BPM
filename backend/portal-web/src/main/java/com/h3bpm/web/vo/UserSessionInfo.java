package com.h3bpm.web.vo;

import java.io.Serializable;
import java.util.List;

import OThinker.Common.Organization.Models.User;

public class UserSessionInfo implements Serializable {
	private static final long serialVersionUID = -5192782585361267362L;

	/**
	 * 登录用户基本信息
	 */
	private User user = null;

	/**
	 * 用户所有父部门的id集合
	 */
	private List<String> parentIds = null;

	public List<String> getParentIds() {
		return parentIds;
	}

	public void setParentIds(List<String> parentIds) {
		this.parentIds = parentIds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
