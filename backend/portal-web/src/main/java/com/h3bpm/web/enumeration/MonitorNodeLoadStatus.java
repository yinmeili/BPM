package com.h3bpm.web.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum MonitorNodeLoadStatus implements EnumerationInt {

	UNDO(0, "未开始"),

	LIQUIDATION_NIGHT_FINISH(1, "夜间清算及开市完成"),
	
	CHECK_FINISH(2, "日间核对完成"),
	;

	private final static Logger logger = LoggerFactory.getLogger(MonitorNodeLoadStatus.class);
	private static final Map<Integer, MonitorNodeLoadStatus> DICT = new HashMap<Integer, MonitorNodeLoadStatus>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (MonitorNodeLoadStatus item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private MonitorNodeLoadStatus(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static MonitorNodeLoadStatus parse(Integer value) {

		try {
			MonitorNodeLoadStatus found = DICT.get(value);

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
