package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public enum ErrorCode implements  EnumerationInt {

	SUCCESS(200, "成功"), 

	AUTHORITY_ERROR(30001, "权限校验错误"),

	;

	private final static Logger logger = LoggerFactory.getLogger(ErrorCode.class);
	private static final Map<Integer, ErrorCode> DICT = new HashMap<Integer, ErrorCode>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (ErrorCode item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private ErrorCode(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static ErrorCode parse(Integer value) {

		try {
			ErrorCode found = DICT.get(value);

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
