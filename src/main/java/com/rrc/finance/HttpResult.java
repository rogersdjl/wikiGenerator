package com.rrc.finance;
/**
 * http返回类，
 *  result=接口返回 statusCode=状态码
 * @author doujinlong
 *
 */
public class HttpResult {

	private String result;
	
	private Integer statusCode;

	public String getResult() {
		return result;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
}
