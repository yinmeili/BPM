package com.h3bpm.web.vo;

import java.io.Serializable;

public class UserInfoVo implements Serializable {
	private static final long serialVersionUID = -5192782585361267361L;
	
	private String id = null;
	private String name = null;

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
