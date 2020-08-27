package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum WorkCalendarStatus implements EnumerationInt {

	UNFINISH(0, "待办"),

	FINISH(1, "已办"),

	EXCEED_TIME_LIMIT(-1, "逾期"),

	;

	private final static Logger logger = LoggerFactory.getLogger(WorkCalendarStatus.class);
	private static final Map<Integer, WorkCalendarStatus> DICT = new HashMap<Integer, WorkCalendarStatus>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (WorkCalendarStatus item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private WorkCalendarStatus(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static WorkCalendarStatus parse(Integer value) {

		try {
			WorkCalendarStatus found = DICT.get(value);

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
