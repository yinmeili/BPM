package com.h3bpm.web.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.h3bpm.web.service.TransportException;

@SuppressWarnings("unchecked")
public class DataUtils {
	public static final String JSON_ACTION_KEY = "action";
	public static final String JSON_DATA_KEY = "data";
	public static final String JSON_TIMESTAMP_KEY = "ts";
	public static final String JSON_ERROR_CODE_KEY = "errorCode";
	public static final String JSON_ERROR_MESSAGE_KEY = "message";
	public static final String JSON_REPLY_TIMESTAMP_KEY = "replyTime";
	public static final String JSON_TASK_UUID = "taskUuid";
	public static final String JSON_CODE = "code";

	public static final int SUCCESS_CODE = 200;
	public static final int WARN_CODE = 1000;

	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CONTENT_TYPE = "application/json";

	public static final String SYS_LOGICAL_VOLUME_DEVICE = "vda";

	public static final int LOGICAL_VOLUME_SYSTEM_LIMIT_MAX_SIZE = 2097152;

	/**
	 * Retrieves the data (single object) from result to an object.
	 * 
	 * @param result
	 *            The result.
	 * 
	 * @return The map, never be null.
	 * 
	 * @throws EquinoxException
	 *             Thrown when the parse failed.
	 */
	public static Map<String, Object> parseToSingleData(Map<String, Object> result) throws TransportException {

		try {
			Object obj = result.get(JSON_DATA_KEY);

			if (obj == null) {
				return new HashMap<String, Object>();
			}

			return (Map<String, Object>) result.get(JSON_DATA_KEY);

		} catch (Exception ex) {
			throw new TransportException(ex.getMessage());
		}
	}

	/**
	 * Retrieves the data (array objects) from result to an object array.
	 * 
	 * @param result
	 *            The result.
	 * 
	 * @return The map array, never be null.
	 * 
	 * @throws EquinoxException
	 *             Thrown when the parse failed.
	 */
	public static Map<String, Object>[] parseToArrayData(Map<String, Object> result) throws TransportException {

		try {
			Object obj = result.get(JSON_DATA_KEY);

			if (obj == null) {
				return new HashMap[0];
			}

			List<Map<String, Object>> ret = (List<Map<String, Object>>) result.get(JSON_DATA_KEY);

			return ret.toArray(new HashMap[ret.size()]);

		} catch (Exception ex) {
			throw new TransportException(ex.getMessage());
		}
	}

	public static boolean isInt(String str) {

		try {
			Integer.parseInt(str);

			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isLong(String str) {

		try {
			Long.parseLong(str);

			return true;

		} catch (Exception ex) {
			return false;
		}
	}
}
