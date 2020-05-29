package com.h3bpm.web.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ApiActionUrl {

	/**
	 * 判断一个组织是否另一个组织的父级 /orgs/{childId}/parents/{ancestorId}
	 */
	GET_ORG_IS_PARENT("/orgs/%s/parents/%s", HttpRequestType.GET),
	
	/**
	 * 获取上级OU /orgs/{unitId}/parents
	 */
	GET_PARENT_ORG("/orgs/%s/parents", HttpRequestType.GET),

	;

	private final static Logger logger = LoggerFactory.getLogger(ApiActionUrl.class);
	private static final Map<String, ApiActionUrl> DICT = new HashMap<String, ApiActionUrl>();
	private static final Map<String, Object> VALUES_MAP = new HashMap<String, Object>();

	private String url = null;
	private HttpRequestType httpRequestType = null;

	// static {
	//
	// for (ApiActionUrl item : values()) {
	// DICT.put(item.url, item);
	// VALUES_MAP.put(item.url, item.httpRequestType);
	// }
	// }

	private ApiActionUrl(String url, HttpRequestType httpRequestType) {
		this.url = url;
		this.httpRequestType = httpRequestType;
	}

	public String getUrl() {
		return url;
	}

	public HttpRequestType getHttpRequestType() {
		return httpRequestType;
	}
}
