package com.h3bpm.web.service;

import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.utils.TransportUtils;
import com.h3bpm.web.vo.Request;
import com.h3bpm.web.vo.Response;
import com.h3bpm.web.vo.ResponseList;
import com.h3bpm.web.vo.api.kingdom.KingdomRequestVo;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ApiDataService {

    @Value(value = "${application.api.service.url.org}")
    private String apiServiceUrlOrg = null;

    @Value(value = "${application.api.service.url.bpm}")
    private String apiServiceUrlBpm = null;

    @Value(value = "${application.api.kingdom.service.url}")
    private String apiKingdomServiceUrl = null;

    @Value(value = "${application.api.systemCode}")
    private String systemCode = null;

    @Value(value = "${application.api.secret}")
    private String secret = null;

    public String getApiServiceUrl() {
        return apiServiceUrlOrg;
    }

    // public Map<String, Object> processSyncAndRetrieveSingleData(String actionUrl, HttpRequestType httpRequestType, Map<String, Object> data) throws IllegalArgumentException, TransportException {
    // Map<String, Object> result = processSync(actionUrl, httpRequestType,data);
    //
    // return DataUtils.parseToSingleData(result);
    // }
    //
    // public Map<String, Object> processSyncAndRetrieveSingleData(String actionUrl, HttpRequestType httpRequestType) throws IllegalArgumentException, TransportException {
    // return processSyncAndRetrieveSingleData(actionUrl, httpRequestType, null);
    // }
    //
    // public Map<String, Object>[] processSyncAndRetrieveArrayData(String actionUrl, HttpRequestType httpRequestType, Map<String, Object> data) throws IllegalArgumentException, TransportException {
    // Map<String, Object> result = processSync(actionUrl, httpRequestType,data);
    //
    // return DataUtils.parseToArrayData(result);
    // }
    //
    // public Map<String, Object>[] processSyncAndRetrieveArrayData(String actionUrl, HttpRequestType httpRequestType) throws IllegalArgumentException, TransportException {
    // return processSyncAndRetrieveArrayData(actionUrl, httpRequestType, null);
    // }

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

    public Map<String, Object> processSyncBpm(String actionUrl, HttpRequestType httpRequestType, Map<String, Object> data) throws IllegalArgumentException, TransportException {
        Request request = new Request(apiServiceUrlBpm, actionUrl, httpRequestType.getValue());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("systemCode", systemCode);
        param.put("secret", secret);

        if (data != null) {
            param.putAll(data);
        }

        request.setData(param);

        Response response = TransportUtils.processSync(request);
        Map<String, Object> result = response.getResult();

        return result;
    }

    public List<Map<String, Object>> processSyncKingdom(HttpRequestType httpRequestType, List<KingdomRequestVo> kingdomRequestVoList) throws IllegalArgumentException, TransportException {
        Request request = new Request(apiKingdomServiceUrl, httpRequestType.getValue());

        request.setDataObject(kingdomRequestVoList);

        ResponseList responseList = TransportUtils.processSyncResponseList(request);
        List<Map<String, Object>> result = responseList.getResult();

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
