package csv2excel;

import java.util.List;

import com.google.common.collect.Lists;
/**
 *  
 * @author doujinlong
 *
 */
public class WikiResult {

	private String url;
	private String method;
	private String result;
	private String postJson;
	private Integer status;
	private List<String> params =Lists.newArrayList();
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "WikiResult [url=" + url + ", method=" + method + ", result=" + result + ", params=" + params + "]";
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPostJson() {
		return postJson;
	}
	public void setPostJson(String postJson) {
		this.postJson = postJson;
	}
	
}
