package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AnnouncementType implements EnumerationInt {

	DEPARTMENT(1, "部门公告"),

	OUTSIDE(2, "外部公告"),

	;

	private final static Logger logger = LoggerFactory.getLogger(AnnouncementType.class);
	private static final Map<Integer, AnnouncementType> DICT = new HashMap<Integer, AnnouncementType>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (AnnouncementType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private AnnouncementType(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static AnnouncementType parse(Integer value) {

		try {
			AnnouncementType found = DICT.get(value);

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
