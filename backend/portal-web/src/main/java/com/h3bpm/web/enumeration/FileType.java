package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FileType implements Enumeration {
	FILE("file", "文件"),

	DIR("dir", "文件夹"),

	;

	private final static Logger logger = LoggerFactory.getLogger(FileType.class);
	private static final Map<String, FileType> DICT = new HashMap<String, FileType>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (FileType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private FileType(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static FileType parse(String value) {

		try {
			FileType found = DICT.get(value);

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
