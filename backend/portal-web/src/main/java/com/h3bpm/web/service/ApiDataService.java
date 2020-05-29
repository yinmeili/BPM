package com.h3bpm.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.utils.DataUtils;
import com.h3bpm.web.utils.TransportUtils;
import com.h3bpm.web.vo.Request;
import com.h3bpm.web.vo.Response;

public abstract class ApiDataService {

	@Value(value = "${application.api.service.url.org}")
	private String apiServiceUrlOrg = null;

	public String getApiServiceUrl() {
		return apiServiceUrlOrg;
	}

//	public Map<String, Object> processSyncAndRetrieveSingleData(String actionUrl, HttpRequestType httpRequestType, Map<String, Object> data) throws IllegalArgumentException, TransportException {
//		Map<String, Object> result = processSync(actionUrl, httpRequestType,data);
//
//		return DataUtils.parseToSingleData(result);
//	}
//
//	public Map<String, Object> processSyncAndRetrieveSingleData(String actionUrl, HttpRequestType httpRequestType) throws IllegalArgumentException, TransportException {
//		return processSyncAndRetrieveSingleData(actionUrl, httpRequestType, null);
//	}
//
//	public Map<String, Object>[] processSyncAndRetrieveArrayData(String actionUrl, HttpRequestType httpRequestType, Map<String, Object> data) throws IllegalArgumentException, TransportException {
//		Map<String, Object> result = processSync(actionUrl, httpRequestType,data);
//
//		return DataUtils.parseToArrayData(result);
//	}
//
//	public Map<String, Object>[] processSyncAndRetrieveArrayData(String actionUrl, HttpRequestType httpRequestType) throws IllegalArgumentException, TransportException {
//		return processSyncAndRetrieveArrayData(actionUrl, httpRequestType, null);
//	}

	public Map<String, Object> processSyncOrg(String actionUrl, HttpRequestType httpRequestType, Map<String, Object> data) throws IllegalArgumentException, TransportException {
		Request request = new Request(apiServiceUrlOrg, actionUrl, httpRequestType.getValue());

		Map<String, Object> param = new HashMap<String, Object>();

		if (data != null) {
			param.putAll(data);
		}

		request.setData(param);

		Response response = TransportUtils.processSync(request);
		Map<String, Object> result = response.getResult();

		return result;
	}

	// ------------------------------------------------------------
	// Below method(s) for the Spring / Hibernate / JSP uses only.
	// Marked as "@Deprecated" to prevent be called externally.
	// ------------------------------------------------------------

	@Deprecated
	public void setApiServiceUrl(String apiServiceUrl) {
		this.apiServiceUrlOrg = apiServiceUrl;
	}
}
