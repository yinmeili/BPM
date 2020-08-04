package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public enum KnowledgeStatus implements  EnumerationInt {

	CHECKING(0, "审核中"), 

	SHARE_FINISH(1, "已共享"),
	;

	private final static Logger logger = LoggerFactory.getLogger(KnowledgeStatus.class);
	private static final Map<Integer, KnowledgeStatus> DICT = new HashMap<Integer, KnowledgeStatus>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (KnowledgeStatus item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private KnowledgeStatus(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static KnowledgeStatus parse(Integer value) {

		try {
			KnowledgeStatus found = DICT.get(value);

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
