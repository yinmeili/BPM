package com.h3bpm.web.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.service.TransportException;
import com.h3bpm.web.vo.Callback;
import com.h3bpm.web.vo.Request;
import com.h3bpm.web.vo.Response;
import com.h3bpm.web.vo.ResponseList;

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
	 * @param request
	 *            The request, cannot be null.
	 * @return The response.
	 * @throws IllegalArgumentException
	 *             Thrown when the "request" is null.
	 * @throws TransportException
	 *             Thrown when any error occurs.
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
				
			} else if (request.getMethod().equals(HttpRequestType.PUT.getValue())) {
				httpResponse = doPut(request, uuid);
			}

			// Parses the response data.
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {
				BufferedReader reader = null;

				try {

					/**
					 * 过大的响应体，需要通过解压gzip完成
					 */
					org.apache.http.Header ceheader = httpEntity.getContentEncoding();
					if (ceheader != null) {
						for (org.apache.http.HeaderElement element : ceheader.getElements()) {
							if ("gzip".equalsIgnoreCase(element.getName())) {
								httpEntity = new GzipDecompressingEntity(httpEntity);
								break;
							}
						}
					}

					reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), DataUtils.CHARSET_UTF8));
					ObjectMapper objectMapper = new ObjectMapper();
					// 忽略转义字符
					objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

					Map<String, Object> result = objectMapper.readValue(reader, Map.class);
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
	 * @param request
	 *            The request, cannot be null.
	 * @return The responseList.
	 * @throws IllegalArgumentException
	 *             Thrown when the "request" is null.
	 * @throws TransportException
	 *             Thrown when any error occurs.
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
				
			} else if (request.getMethod().equals(HttpRequestType.PUT.getValue())) {
				httpResponse = doPut(request, uuid);
			}

			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {
				BufferedReader reader = null;
				try {

					/**
					 * 过大的响应体，需要通过解压gzip完成
					 */
					org.apache.http.Header ceheader = httpEntity.getContentEncoding();
					if (ceheader != null) {
						for (org.apache.http.HeaderElement element : ceheader.getElements()) {
							if ("gzip".equalsIgnoreCase(element.getName())) {
								httpEntity = new GzipDecompressingEntity(httpEntity);
								break;
							}
						}
					}

					InputStreamReader ins = new InputStreamReader(httpEntity.getContent(), DataUtils.CHARSET_UTF8);

					reader = new BufferedReader(ins);

					ObjectMapper objectMapper = new ObjectMapper();

					// 忽略转义字符
					objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
					objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
					objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
					objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
					objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
					objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
					objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
					objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

					List<Map<String, Object>> result = null;

					try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
						result = objectMapper.readValue(reader, ArrayList.class);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// JSONArray jsonArray = com.alibaba.fastjson.JSONObject.parseArray(testStr);

					// List<Map<String, Object>> result2 = com.alibaba.fastjson.JSONObject.parseObject(testStr, new TypeReference<List<Map<String, Object>>>() {
					// });

					// LOGGER.info("[" + uuid + "] Responsed result: " + com.alibaba.fastjson.JSONObject.toJSONString(result));

					// return new ResponseList(result2);

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
	 * @param request
	 *            The request, cannot be null.
	 * @param callback
	 *            The callback, cannot be null.
	 * @throws IllegalArgumentException
	 *             Thrown when the "request" or the "callback" is null.
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
			httpRequest.addHeader("Connection", "close");

			httpClient = new DefaultHttpClient();

			if (HttpsUtils.isHttpsScheme(request.getUrl())) {
				HttpsUtils.enableSsl(httpClient);
			}

			return httpClient.execute(httpRequest);

		} finally {

			// if (httpClient != null && httpClient.getConnectionManager() != null) {
			// httpClient.getConnectionManager().shutdown();
			// }
		}

	}

	@SuppressWarnings("deprecation")
	private static HttpResponse doPost(Request request, String uuid) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpRequest = null;

		try {
			// JSONObject json = new JSONObject((Map<String, Object>) request.getData());
			// StringEntity entity = new StringEntity(json.toJSONString(), DataUtils.CHARSET_UTF8);

			StringEntity entity = new StringEntity(com.alibaba.fastjson.JSONObject.toJSONString(request.getData()), DataUtils.CHARSET_UTF8);

			httpRequest = new HttpPost(request.getUrl());
			// httpRequest.setProtocolVersion(HttpVersion.HTTP_1_0);

			httpRequest.addHeader("content-type", DataUtils.CONTENT_TYPE);

			// 对于过大的响应体，需要以gzip格式来保存响应
			httpRequest.addHeader("Accept-Encoding", "gzip, deflate, br");
			httpRequest.addHeader("Connection", "close");

			httpRequest.setEntity(entity);

			// RequestConfig config = RequestConfig.custom().setConnectTimeout(1000000) // 连接超时时间
			// .setConnectionRequestTimeout(100000) // 从连接池中取的连接的最长时间
			// .setSocketTimeout(10 * 100000) // 数据传输的超时时间
			// .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
			// .build();
			//
			// httpRequest.setConfig(config);

			httpClient = new DefaultHttpClient();

			if (HttpsUtils.isHttpsScheme(request.getUrl())) {
				HttpsUtils.enableSsl(httpClient);
			}

			return httpClient.execute(httpRequest);

		} finally {

			// if (httpClient != null && httpClient.getConnectionManager() != null) {
			// httpClient.getConnectionManager().shutdown();
			// }
		}

	}
	
	@SuppressWarnings("deprecation")
	private static HttpResponse doPut(Request request, String uuid) throws Exception {
		HttpClient httpClient = null;
		HttpPut httpRequest = null;

		try {
			// JSONObject json = new JSONObject((Map<String, Object>) request.getData());
			// StringEntity entity = new StringEntity(json.toJSONString(), DataUtils.CHARSET_UTF8);

			StringEntity entity = new StringEntity(com.alibaba.fastjson.JSONObject.toJSONString(request.getData()), DataUtils.CHARSET_UTF8);

			httpRequest = new HttpPut(request.getUrl());
			// httpRequest.setProtocolVersion(HttpVersion.HTTP_1_0);

			httpRequest.addHeader("content-type", DataUtils.CONTENT_TYPE);

			// 对于过大的响应体，需要以gzip格式来保存响应
			httpRequest.addHeader("Accept-Encoding", "gzip, deflate, br");
			httpRequest.addHeader("Connection", "close");

			httpRequest.setEntity(entity);

			// RequestConfig config = RequestConfig.custom().setConnectTimeout(1000000) // 连接超时时间
			// .setConnectionRequestTimeout(100000) // 从连接池中取的连接的最长时间
			// .setSocketTimeout(10 * 100000) // 数据传输的超时时间
			// .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
			// .build();
			//
			// httpRequest.setConfig(config);

			httpClient = new DefaultHttpClient();

			if (HttpsUtils.isHttpsScheme(request.getUrl())) {
				HttpsUtils.enableSsl(httpClient);
			}

			return httpClient.execute(httpRequest);

		} finally {

			// if (httpClient != null && httpClient.getConnectionManager() != null) {
			// httpClient.getConnectionManager().shutdown();
			// }
		}

	}
}
