package com.h3bpm.web.vo;

import java.util.Map;

/**
 * This class represents a HTTP response.
 * 
 * @author tonghao
 */
public class Response {
	private Map<String, Object> result = null;

	public Response(Map<String, Object> result) {
		this.result = result;
	}

	/**
	 * Returns the result of a success request.
	 * 
	 * @return A JSON data (in a map format), never be null.
	 */
	public Map<String, Object> getResult() {
		return result;
	}
}
