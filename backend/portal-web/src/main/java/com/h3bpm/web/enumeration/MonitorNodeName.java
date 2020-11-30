package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MonitorNodeName implements Enumeration {
	MOAN("liquidation_monitor_moan", "夜盘开市"),

	STMARKET("liquidation_monitor_stmarket", "股转做市系统"),

	DEAL("liquidation_monitor_deal", "投资交易系统"),

	;

	private final static Logger logger = LoggerFactory.getLogger(MonitorNodeName.class);
	private static final Map<String, MonitorNodeName> DICT = new HashMap<String, MonitorNodeName>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (MonitorNodeName item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private MonitorNodeName(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static MonitorNodeName parse(String value) {

		try {
			MonitorNodeName found = DICT.get(value);

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
