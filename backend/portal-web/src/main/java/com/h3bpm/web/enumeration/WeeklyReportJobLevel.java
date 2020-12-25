package com.h3bpm.web.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum WeeklyReportJobLevel implements EnumerationInt {

	LEVEL_1(1, "一级"),

	LEVEL_2(2, "二级"),

	LEVEL_3(3, "三级"),

	LEVEL_4(4, "四级"),

	;

	private final static Logger logger = LoggerFactory.getLogger(WeeklyReportJobLevel.class);
	private static final Map<Integer, WeeklyReportJobLevel> DICT = new HashMap<Integer, WeeklyReportJobLevel>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (WeeklyReportJobLevel item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private WeeklyReportJobLevel(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static WeeklyReportJobLevel parse(Integer value) {

		try {
			WeeklyReportJobLevel found = DICT.get(value);

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
