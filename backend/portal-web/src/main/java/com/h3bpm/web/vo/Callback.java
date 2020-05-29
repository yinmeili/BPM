package com.h3bpm.web.vo;

import java.util.Map;

import com.h3bpm.web.service.TransportException;

/**
 * A callback to handle asynchronous transport request.
 * 
 * @author tonghao
 */
public abstract class Callback {

	/**
	 * The handler to be called when request success.
	 * 
	 * @param result
	 *            The JSON result (in a map format) of the response.
	 */
	public abstract void onSuccess(Map<String, Object> result);

	/**
	 * The handler to be called when request failure.
	 * 
	 * @param fault
	 *            The exception to be caused.
	 */
	public abstract void onFailure(TransportException fault);
}
