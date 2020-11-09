package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public enum KingdomNodeStatus implements  Enumeration {

	UNDO("0", "未执行"),

	DOING("1", "正在执行"),
	
	FINISH("2", "执行完成")

	;

	private final static Logger logger = LoggerFactory.getLogger(KingdomNodeStatus.class);
	private static final Map<String, KingdomNodeStatus> DICT = new HashMap<String, KingdomNodeStatus>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (KingdomNodeStatus item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private KingdomNodeStatus(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static KingdomNodeStatus parse(String value) {

		try {
			KingdomNodeStatus found = DICT.get(value);

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
