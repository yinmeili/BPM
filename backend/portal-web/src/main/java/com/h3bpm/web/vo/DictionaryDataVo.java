package com.h3bpm.web.vo;

import OThinker.H3.Entity.Data.Metadata.EnumerableMetadata;

public class DictionaryDataVo {
	private String code = null;
	private String displayName = null;

	public DictionaryDataVo() {

	}

	public DictionaryDataVo(EnumerableMetadata model) {
		this.code = model.getCode();
		this.displayName = model.getEnumValue();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
