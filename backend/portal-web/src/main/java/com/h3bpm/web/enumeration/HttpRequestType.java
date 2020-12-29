package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HttpRequestType implements Enumeration {
	GET("get", "get"),

	POST("post", "post"),
	
	PUT("put", "put"),

	;

	private final static Logger logger = LoggerFactory.getLogger(HttpRequestType.class);
	private static final Map<String, HttpRequestType> DICT = new HashMap<String, HttpRequestType>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String value = null;
	private String displayName = null;

	static {

		for (HttpRequestType item : values()) {
			DICT.put(item.value, item);
			VALUES_MAP.put(item.value, item.displayName);
		}
	}

	private HttpRequestType(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static HttpRequestType parse(String value) {

		try {
			HttpRequestType found = DICT.get(value);

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
