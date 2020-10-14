package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public enum WorkflowCode implements  Enumeration {

	LIQUIDATION("liquidation", "清算")

	;

	private final static Logger logger = LoggerFactory.getLogger(WorkflowCode.class);
	private static final Map<String, WorkflowCode> DICT = new HashMap<String, WorkflowCode>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (WorkflowCode item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private WorkflowCode(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static WorkflowCode parse(String value) {

		try {
			WorkflowCode found = DICT.get(value);

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
