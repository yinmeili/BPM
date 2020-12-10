package com.h3bpm.web.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum WorkflowExecuteType implements  EnumerationInt {
	
	UNFINISH(0, "未执行"),
	
	SUCCESS(1, "成功"),
	
	FAIL(2,"执行失败"),

	;

	private final static Logger logger = LoggerFactory.getLogger(WorkflowExecuteType.class);
	private static final Map<Integer, WorkflowExecuteType> DICT = new HashMap<Integer, WorkflowExecuteType>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (WorkflowExecuteType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private WorkflowExecuteType(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static WorkflowExecuteType parse(Integer value) {

		try {
			WorkflowExecuteType found = DICT.get(value);

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
