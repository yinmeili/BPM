package com.h3bpm.web.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum DeletedFileType implements  EnumerationInt {

	DELETED(1, "已删除"),

	UNDELETED(0, "未删除"),

	COMPLETE_DELETED(2,"彻底删除"),

	;

	private final static Logger logger = LoggerFactory.getLogger(DeletedFileType.class);
	private static final Map<Integer, DeletedFileType> DICT = new HashMap<Integer, DeletedFileType>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (DeletedFileType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private DeletedFileType(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static DeletedFileType parse(Integer value) {

		try {
			DeletedFileType found = DICT.get(value);

			return found;

		} catch (NullPointerException ex) {
			logger.debug("Failed to parse the value: " + value, ex);

			return null;
		}
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
}
