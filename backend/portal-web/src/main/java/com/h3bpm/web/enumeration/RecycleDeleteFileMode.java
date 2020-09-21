package com.h3bpm.web.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum RecycleDeleteFileMode implements EnumerationInt {

	/*
	 * 只将共享文件中的is_delete变成2
	 */
	FILE_DELETE(0, "共享文件记录删除"),

	/*
	将共享文件中的is_delete变成2同时删除sftp上的文件
	 */
	FILE_COMPLETE_DELET(1, "共享文件记录和文件删除"),

	/*
	只将我的文件中的is_delete变成2
	 */
	MY_FILE_DELETE(2, "我的文件记录删除"),

	/*
	将我的文件中的is_delete变成2同时删除sftp上的文件
	 */
	MY_FILE_COMPLETE_DELET(3, "我的文件记录和文件删除"),

	;

	private final static Logger logger = LoggerFactory.getLogger(RecycleDeleteFileMode.class);
	private static final Map<Integer, RecycleDeleteFileMode> DICT = new HashMap<Integer, RecycleDeleteFileMode>();
	private static final Map<Integer, Object> VALUES_MAP = new HashMap<Integer, Object>();

	private Integer value = null;
	private String displayName = null;

	static {

		for (RecycleDeleteFileMode item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private RecycleDeleteFileMode(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static RecycleDeleteFileMode parse(Integer value) {

		try {
			RecycleDeleteFileMode found = DICT.get(value);

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
