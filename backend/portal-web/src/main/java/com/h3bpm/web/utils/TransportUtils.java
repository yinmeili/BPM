package com.h3bpm.web.utils;

import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.service.TransportException;
import com.h3bpm.web.vo.Callback;
import com.h3bpm.web.vo.Request;
import com.h3bpm.web.vo.Response;
import com.h3bpm.web.vo.ResponseList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class is designed to handle sending HTTP request and receiving its response.
 *
 * @author tonghao
 */
@SuppressWarnings("unchecked")
public class TransportUtils {
    private static final Logger LOGGER = Logger.getLogger(TransportUtils.class);

    /**
     * 发送同步请求
     *
     * @param request The request, cannot be null.
     * @return The response.
     * @throws IllegalArgumentException Thrown when the "request" is null.
     * @throws TransportException       Thrown when any error occurs.
     */
    public static Response processSync(final Request request) throws IllegalArgumentException, TransportException {
        Validate.isTrue(request != null, "The 'request' is null");

        HttpResponse httpResponse = null;
        String uuid = UUID.randomUUID().toString();

        try {

            if (request.getMethod().equals(HttpRequestType.GET.getValue())) {
                httpResponse = doGet(request, uuid);

            } else if (request.getMethod().equals(HttpRequestType.POST.getValue())) {
                httpResponse = doPost(request, uuid);
            }

            // Parses the response data.
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), DataUtils.CHARSET_UTF8));

                    // List<String> lines = IOUtils.readLines(reader);
                    Map<String, Object> result = new ObjectMapper().readValue(reader, Map.class);
                    LOGGER.info("[" + uuid + "] Responsed result: " + JSONObject.toJSONString(result));

                    // int errorCode = (Integer) result.get(DataUtils.JSON_ERROR_CODE_KEY);
                    //
                    // if (DataUtils.SUCCESS_CODE != errorCode) {
                    // String message = (String) result.get(DataUtils.JSON_ERROR_MESSAGE_KEY);
                    // if (errorCode >= DataUtils.WARN_CODE) {
                    // result.put(DataUtils.JSON_ERROR_MESSAGE_KEY, message);
                    //
                    // } else {
                    // throw new TransportException(errorCode, message);
                    // }
                    // }

                    return new Response(result);

                } finally {
                    IOUtils.closeQuietly(reader);
                }

            } else {
                LOGGER.info("[" + uuid + "] Responsed empty result");

                return new Response(new HashMap<String, Object>());
            }

        } catch (TransportException ex) {
            LOGGER.info("[" + uuid + "] The request is success, but responsed data represents error result: " + request, ex);

            throw ex;

        } catch (HttpHostConnectException ex) {
            LOGGER.info("[" + uuid + "] Failed to process the request: " + request, ex);

            throw new TransportException("访问 API 服务失败！<br/><br/>【地址】<br/>" + request.getUrl() + "<br/><br/>请检查该地址是否正确或者该服务是否已经启动！");

        } catch (Exception ex) {

            if (ex instanceof JsonParseException || ex instanceof JsonMappingException) {
                throw new TransportException("返回数据格式转换时发生错误！");
            }

            throw new TransportException(ex.getMessage());

        }
    }

    /**
     * 发送同步请求
     *
     * @param request The request, cannot be null.
     * @return The responseList.
     * @throws IllegalArgumentException Thrown when the "request" is null.
     * @throws TransportException       Thrown when any error occurs.
     */
    public static ResponseList processSyncResponseList(final Request request) throws IllegalArgumentException, TransportException {
        Validate.isTrue(request != null, "The 'request' is null");

        HttpResponse httpResponse = null;
        String uuid = UUID.randomUUID().toString();

        try {

            if (request.getMethod().equals(HttpRequestType.GET.getValue())) {
                httpResponse = doGet(request, uuid);

            } else if (request.getMethod().equals(HttpRequestType.POST.getValue())) {
                httpResponse = doPost(request, uuid);
            }

            // Parses the response data.
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), DataUtils.CHARSET_UTF8));

                    // List<String> lines = IOUtils.readLines(reader);
                    List<Map<String, Object>> result = new ObjectMapper().readValue(reader, ArrayList.class);
                    LOGGER.info("[" + uuid + "] Responsed result: " + com.alibaba.fastjson.JSONObject.toJSONString(result));

                    // int errorCode = (Integer) result.get(DataUtils.JSON_ERROR_CODE_KEY);
                    //
                    // if (DataUtils.SUCCESS_CODE != errorCode) {
                    // String message = (String) result.get(DataUtils.JSON_ERROR_MESSAGE_KEY);
                    // if (errorCode >= DataUtils.WARN_CODE) {
                    // result.put(DataUtils.JSON_ERROR_MESSAGE_KEY, message);
                    //
                    // } else {
                    // throw new TransportException(errorCode, message);
                    // }
                    // }

                    return new ResponseList(result);

                } finally {
                    IOUtils.closeQuietly(reader);
                }

            } else {
                LOGGER.info("[" + uuid + "] Responsed empty result");

                return new ResponseList(new ArrayList<Map<String, Object>>());
            }

        } catch (TransportException ex) {
            LOGGER.info("[" + uuid + "] The request is success, but responsed data represents error result: " + request, ex);

            throw ex;

        } catch (HttpHostConnectException ex) {
            LOGGER.info("[" + uuid + "] Failed to process the request: " + request, ex);

            throw new TransportException("访问 API 服务失败！<br/><br/>【地址】<br/>" + request.getUrl() + "<br/><br/>请检查该地址是否正确或者该服务是否已经启动！");

        } catch (Exception ex) {

            if (ex instanceof JsonParseException || ex instanceof JsonMappingException) {
                throw new TransportException("返回数据格式转换时发生错误！");
            }

            throw new TransportException(ex.getMessage());

        }
    }

    /**
     * 发送异步请求
     *
     * @param request  The request, cannot be null.
     * @param callback The callback, cannot be null.
     * @throws IllegalArgumentException Thrown when the "request" or the "callback" is null.
     */
    public static void processAsync(final Request request, final Callback callback) throws IllegalArgumentException {
        Validate.isTrue(request != null, "The 'request' is null");
        Validate.isTrue(callback != null, "The 'callback' is null");

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Response response = processSync(request);

                    callback.onSuccess(response.getResult());

                } catch (TransportException ex) {
                    callback.onFailure(ex);
                }
            }

        }).start();
    }

    private static HttpResponse doGet(final Request request, String uuid) throws Exception {
        HttpClient httpClient = null;
        HttpGet httpRequest = null;

        try {
            JSONObject json = new JSONObject();
            json.put(DataUtils.JSON_DATA_KEY, request.getData());
            json.put(DataUtils.JSON_TIMESTAMP_KEY, new Date().getTime());
            LOGGER.info("[" + uuid + "] Prepared data to be sent: " + json.toJSONString());

            StringEntity entity = new StringEntity(json.toJSONString(), DataUtils.CHARSET_UTF8);
            // entity.setContentEncoding(DataUtils.CHARSET_UTF8);
            // entity.setContentType(DataUtils.CONTENT_TYPE);

            httpRequest = new HttpGet(request.getUrl());

            httpRequest.addHeader("content-type", DataUtils.CONTENT_TYPE);

            httpClient = new DefaultHttpClient();

            if (HttpsUtils.isHttpsScheme(request.getUrl())) {
                HttpsUtils.enableSsl(httpClient);
            }

            return httpClient.execute(httpRequest);

        } finally {

            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

    }

    private static HttpResponse doPost(Request request, String uuid) throws Exception {
        HttpClient httpClient = null;
        HttpPost httpRequest = null;

        try {
            //JSONObject json = new JSONObject((Map<String, Object>) request.getData());
            //StringEntity entity = new StringEntity(json.toJSONString(), DataUtils.CHARSET_UTF8);

            StringEntity entity = new StringEntity(com.alibaba.fastjson.JSONObject.toJSONString(request.getData()), DataUtils.CHARSET_UTF8);

            httpRequest = new HttpPost(request.getUrl());

            httpRequest.addHeader("content-type", DataUtils.CONTENT_TYPE);
            httpRequest.setEntity(entity);

            httpClient = new DefaultHttpClient();

            if (HttpsUtils.isHttpsScheme(request.getUrl())) {
                HttpsUtils.enableSsl(httpClient);
            }

            return httpClient.execute(httpRequest);

        } finally {

            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

    }
}
