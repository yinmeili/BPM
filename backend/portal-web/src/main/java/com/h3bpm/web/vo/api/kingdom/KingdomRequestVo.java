package com.h3bpm.web.vo.api.kingdom;

import com.alibaba.fastjson.annotation.JSONField;

public class KingdomRequestVo {

	@JSONField(name="Value")
	private String value = null;
	
	@JSONField(name="Type")
	private int type = 4;
	
	@JSONField(name="Name")
	private String name = null;

	public KingdomRequestVo() {
	}

	public KingdomRequestVo(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
