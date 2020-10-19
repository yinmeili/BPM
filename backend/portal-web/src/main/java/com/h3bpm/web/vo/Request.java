package com.h3bpm.web.vo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;

/**
 * This class represents a HTTP request.
 *
 * @author tonghao
 */
public class Request {
    private String url = null;
    private String actionUrl = null;
    private Object data = null;
    private String method = null;

    public Request(String url, String actionUrl, String method) throws IllegalArgumentException {
        Validate.isTrue(StringUtils.isNotBlank(url), "The 'url' is blank");
        Validate.isTrue(StringUtils.isNotBlank(actionUrl), "The 'actionUrl' is blank");
        Validate.isTrue(StringUtils.isNotBlank(method), "The 'method' is blank");

        this.url = url;
        this.actionUrl = actionUrl;
        this.method = method;
    }

    public Request(String url, String method) throws IllegalArgumentException {
        Validate.isTrue(StringUtils.isNotBlank(url), "The 'url' is blank");
        Validate.isTrue(StringUtils.isNotBlank(method), "The 'method' is blank");

        this.url = url;
        this.actionUrl = "";
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public Object getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setDataMapList(List<Map<String, Object>> data) {
        this.data = data;
    }

    public void setDataObject(Object data) {
        this.data = data;
    }

    public String getUrl() {
        return url + actionUrl;
    }
}
