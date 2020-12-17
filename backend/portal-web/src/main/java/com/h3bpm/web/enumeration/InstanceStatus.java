package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum InstanceStatus implements Enumeration {

	UNFINISH("2", "待办"),

	FINISH("4", "结束"),

	CANCEL("5", "取消"),

	;

	private final static Logger logger = LoggerFactory.getLogger(InstanceStatus.class);
	private static final Map<String, InstanceStatus> DICT = new HashMap<String, InstanceStatus>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (InstanceStatus item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private InstanceStatus(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static InstanceStatus parse(String value) {

		try {
			InstanceStatus found = DICT.get(value);

			return found;

		} catch (NullPointerException ex) {
			logger.debug("Failed to parse the value: " + value, ex);

			return null;
		}
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

}
