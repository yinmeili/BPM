package com.h3bpm.web.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum LiquidationStatus implements EnumerationInt {

	INACTIVE(0, "禁用"),

	ACTIVE(1, "启用"),

	;

	private final static Logger logger = LoggerFactory.getLogger(LiquidationStatus.class);
	private static final Map<Integer, LiquidationStatus> DICT = new HashMap<Integer, LiquidationStatus>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (LiquidationStatus item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private LiquidationStatus(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static LiquidationStatus parse(Integer value) {

		try {
			LiquidationStatus found = DICT.get(value);

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
