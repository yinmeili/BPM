package com.h3bpm.web.vo;

import java.io.Serializable;

public class KnowledgeDescVo implements Serializable{
	private static final long serialVersionUID = -5809782578272555999L;
	
	private String key = null;
	private String desc = null;
	private String detail = null;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
