package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public enum TagType implements  Enumeration {

	KNOWLEDGE("KNOWLEDGE", "知识库")

	;

	private final static Logger logger = LoggerFactory.getLogger(TagType.class);
	private static final Map<String, TagType> DICT = new HashMap<String, TagType>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (TagType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private TagType(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static TagType parse(Integer value) {

		try {
			TagType found = DICT.get(value);

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
