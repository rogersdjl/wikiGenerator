package com.rrc.finance;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.ImmutableMap;
/**
 * 简单的http请求，带着cookie一起访问，get/post/put
 * @author doujinlong
 *
 */
public class HttpUtil {


	public static HttpResult getResponse(String url, String cookie) {
		HttpResult result = new HttpResult();
		HttpRequest request = null;
		try {
			request = HttpRequest.get(url).accept("application/json").headers(ImmutableMap.of("Cookie", cookie));
			result.setStatusCode(request.code());
			result.setResult(request.body());
		} catch (Exception e) {
			result.setResult("接口超时，无法访问，请检查网络"+e.getMessage());
		}
		return result;
	}

	/**
	 * 发送post请求
	 *
	 * @param url
	 * @param data
	 */
	public static HttpResult post(String url, String data, String cookie) {
		HttpResult result = new HttpResult();
		try {
			HttpRequest response = HttpRequest.post(url).contentType("application/json")
					.headers(ImmutableMap.of("Cookie", cookie)).send(data);
			int status = response.code();
			String body = response.body();
			result.setResult(body);
			result.setStatusCode(status);
		} catch (Exception e) {
			result.setResult("接口超时，无法访问，请检查网络");
		}
		return result;
	}

	public static HttpResult put(String url, String data, String cookie) {
		HttpResult result = new HttpResult();
		try {
			HttpRequest response = HttpRequest.put(url).contentType("application/json")
					.headers(ImmutableMap.of("Cookie", cookie)).send(data);
			int status = response.code();
			String body = response.body();
			result.setResult(body);
			result.setStatusCode(status);
		} catch (Exception e) {
			result.setResult("接口超时，无法访问，请检查网络");
		}
		return result;
	}
}
