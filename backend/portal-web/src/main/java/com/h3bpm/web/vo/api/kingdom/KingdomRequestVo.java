package com.h3bpm.web.vo.api.kingdom;

public class KingdomRequestVo {
	private String Value = null;
	private int Type = 4;
	private String Name = null;

	public KingdomRequestVo() {
	}

	public KingdomRequestVo(String Value, String Name) {
		this.Value = Value;
		this.Name = Name;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
