package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum KnowledgeType implements Enumeration {

	SHARE_FILE("share_file", "共享文件"),

	MY_FILE("my_file", "我的文件"),

	SHARE_KNOWLEDGE("share_knowledge", "共享知识"),

	MY_KNOWLEDGE("share_knowledge", "我的知识"),

	;

	private final static Logger logger = LoggerFactory.getLogger(KnowledgeType.class);
	private static final Map<String, KnowledgeType> DICT = new HashMap<String, KnowledgeType>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (KnowledgeType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private KnowledgeType(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static KnowledgeType parse(String value) {

		try {
			KnowledgeType found = DICT.get(value);

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
