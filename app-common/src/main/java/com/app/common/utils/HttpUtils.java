package com.app.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.exception.HttpException;

/**
 * HTTP方法
 * 
 *
 */
public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static String httpGet(String url) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			HttpGet httpget = new HttpGet(url);

			response = httpClient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity, "UTF-8");
				httpget.releaseConnection();
				return content;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new HttpException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new HttpException();
		}
		return null;
	}

	public static String httpPost(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : param.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			/*
			 * JSONObject postData = new JSONObject(); for (Map.Entry<String, String> entry
			 * : param.entrySet()) { //postData.add(new BasicNameValuePair(entry.getKey(),
			 * entry.getValue())); postData.put(entry.getKey(), entry.getValue()); }
			 */
			httpClient = HttpClientBuilder.create().build();
			HttpPost httppost = new HttpPost(url);
			// StringEntity postEntity = new StringEntity(postData.toString(),
			// Consts.UTF_8);
			httppost.setEntity(formentity);
			// logger.info("http post
			// data:"+IOUtils.toString(formentity.getContent(),"UTF-8"));
			response = httpClient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity, "UTF-8");
				httppost.releaseConnection();
				// logger.info("http post result:"+content);
				return content;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new HttpException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new HttpException();
		}
		return null;
	}

	public static String httpPost(String url, String str) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		httpClient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		StringEntity entity = new StringEntity(str, "utf-8");
		entity.setContentEncoding("UTF-8");
		httppost.setEntity(entity);
		try {
			response = httpClient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity ret_entity = response.getEntity();
				String content = EntityUtils.toString(ret_entity, "UTF-8");
				httppost.releaseConnection();
				// logger.info("http post result:"+content);
				return content;
			}
		} catch (ClientProtocolException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}
		return null;
	}

	/**
	 * get
	 * 
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String host, String path, String method, Map<String, String> headers,
			Map<String, String> querys) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		httpClient = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}
		response = httpClient.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			request.releaseConnection();
			// logger.info("http doGet result:"+content);
			return content;
		}
		return null;
	}

	private static String buildUrl(String host, String path, Map<String, String> querys)
			throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}
}
