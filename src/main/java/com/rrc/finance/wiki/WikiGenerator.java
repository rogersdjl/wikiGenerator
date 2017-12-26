package com.rrc.finance.wiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.rrc.finance.http.HttpMethod;
import com.rrc.finance.http.HttpResult;
import com.rrc.finance.utils.FileCopyUtils;
import com.rrc.finance.utils.HttpUtils;
import com.rrc.finance.utils.JsonUtils;
/**
 * wiki生成工具
 * @author doujinlong
 *
 */
public abstract class WikiGenerator {
	
	private final static String []CSS_FILES = new String[]{"/batch.css","/batch(1).css","/batch(2).css","/batch(3).css",
			"/colors.css","/default-theme.css","/macro"};
	
	private final static String WIKI_PATTERN="<!DOCTYPE html>"+
				"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
				"        <title>View Source</title>"+
				"        <link type=\"text/css\" rel=\"stylesheet\" href=\"./wikis/batch.css\" media=\"all\">"+
				"<link type=\"text/css\" rel=\"stylesheet\" href=\"./wikis/batch(1).css\" media=\"print\">"+
				"<link type=\"text/css\" rel=\"stylesheet\" href=\"./wikis/batch(2).css\" media=\"all\">"+
				"<link type=\"text/css\" rel=\"stylesheet\" href=\"./wikis/batch(3).css\" media=\"all\">"+
				"<link type=\"text/css\" rel=\"stylesheet\" href=\"./wikis/colors.css\" media=\"all\">"+
				"<link type=\"text/css\" rel=\"stylesheet\" href=\"./wikis/default-theme.css\" media=\"all\">"+
				"    </head>"+
				"    <body class=\"mceContentBody aui-theme-default wiki-content fullsize\">"+
				"%s</body></html>";
	
	private final static String WIKI_FILE_PATH = "/com/rrc/finance/resources";
	
	public static WikiParam httpRequest(HttpMethod method, String url, String cookie, String json) throws JSONException, Exception {
		if (method == null || StringUtils.isBlank(url)) {
			WikiParam wikiResult = new WikiParam();
			wikiResult.setResult("url或请求类型参数为空，不能进行请求");
			return null;
		}
		WikiParam result = new WikiParam();
		result.setMethod(method.toString());
		result.setPostJson(JsonUtils.formatJson(json));
		final int index = url.indexOf('?');
		result.setUrl(index>0?url.substring(0,index):url);
		if (method.equals(HttpMethod.GET)) {
			if (url.lastIndexOf('?') >= 0) {
				String params = url.substring(url.lastIndexOf('?') + 1, url.length());
				String[] param = params.split("&");
				for (String string : param) {
					String each = string.split("=")[0];
					result.getParams().add(each);
				}
			}
			HttpResult ret = HttpUtils.getResponse(url, cookie);
			result.setResult(JsonUtils.formatJson(ret.getResult()));
			result.setStatus(ret.getStatusCode());
			return result;
		}else if (method.equals(HttpMethod.POST)||method.equals(HttpMethod.PUT)) {
			if (url.lastIndexOf('?') >= 0) {
				String params = url.substring(url.lastIndexOf('?') + 1, url.length());
				String[] param = params.split("&");
				for (String string : param) {
					String each = string.split("=")[0];
					result.getParams().add(each);
				}
			}
			result.getParams().addAll(JsonUtils.getKeys(new JSONObject(json)));
			HttpResult ret = HttpUtils.post(url, json, cookie);
			result.setResult(JsonUtils.formatJson(ret.getResult()));
			result.setStatus(ret.getStatusCode());
			return result;
		}
		return null;
	}
	/**
	 * 用wiki内容进行拼装
	 * @param wiki
	 * @return
	 */
	public static String generateWiki(List<WikiParam> param){
		String wiki = "";
		//每一个请求都增加进去
		for (WikiParam wikiParam : param) {
			wiki+=assembleWiki(wikiParam);
		}
		return String.format(WIKI_PATTERN, wiki);
	}
	/**
	 * 最重要的拼装wiki
	 * @param result
	 * @return
	 */
	private static String assembleWiki(WikiParam result){
		String method = result.getMethod();
		String url = result.getUrl();
		String ret = result.getResult();
		String postJson = result.getPostJson();
		String param = "";
		List<String> params = result.getParams();
		for (String wikiHttpParam : params) {
			param+="<tr><td class=\"confluenceTd\">"+wikiHttpParam+"</td><td class=\"confluenceTd\">字段名称</td><td class=\"confluenceTd\">Y/N</td><td class=\"confluenceTd\">备注</td></tr>";
		}
		return "<p>&nbsp;</p><p><span style=\"font-size: 24.0px;\">"
				+ "1，接口名称2</span></p><p><strong>"
				+ "请求类型："+ method+"&nbsp; &nbsp; &nbsp;"
				+ "接口地址："+url+"</strong></p><p"
				+ ">请求参数："
				+ "</p><table class=\"confluenceTable\"><tbody>"
				+ "<tr><th class=\"confluenceTh\">字段</th><th class=\"confluenceTh\">名称</th><th class=\"confluenceTh\">是否必填</th><th class=\"confluenceTh\">备注</th></tr>"
				+ param+ "<tr><td colspan=\"1\" class=\"confluenceTd\">&nbsp;</td><td colspan=\"1\" class=\"confluenceTd\">&nbsp;</td><td colspan=\"1\" class=\"confluenceTd\">&nbsp;</td><td colspan=\"1\" class=\"confluenceTd\">&nbsp;</td></tr></tbody></table>"
				+(method.equals("GET")?"":"<p>post示例请求：</p><table class=\"wysiwyg-macro\" data-macro-name=\"code\" data-macro-parameters=\"language=java|title=示例返回\" style=\"background-image: url(/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGU6bGFuZ3VhZ2U9amF2YXx0aXRsZT3npLrkvovov5Tlm559&amp;locale=zh_CN&amp;version=2); background-repeat: no-repeat;\" data-macro-body-type=\"PLAIN_TEXT\"><tbody><tr><td class=\"wysiwyg-macro-body\"><pre>"+postJson+"&nbsp;</pre></td></tr></tbody></table><p>&nbsp;</p>")
				+ "<p>标准示例返回：</p><table class=\"wysiwyg-macro\" data-macro-name=\"code\" data-macro-parameters=\"language=java|title=示例返回\" style=\"background-image: url(/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGU6bGFuZ3VhZ2U9amF2YXx0aXRsZT3npLrkvovov5Tlm559&amp;locale=zh_CN&amp;version=2); background-repeat: no-repeat;\" data-macro-body-type=\"PLAIN_TEXT\"><tbody><tr><td class=\"wysiwyg-macro-body\"><pre>"+ret+"&nbsp;</pre></td></tr></tbody></table><p>&nbsp;</p>";
		
	}
	
	public static void createWikiFile(String html,String filePath) throws URISyntaxException, IOException {
		InputStream[] is = new InputStream[7];
		for (int i=0;i<7;i++) {
			is[i] = getInputStreamByPath(CSS_FILES[i]);
		}
		File out = new File(filePath+"/wiki/");
		out.mkdirs();
		for (int i=0;i<7;i++) {
			FileCopyUtils.copy(is[i], new FileOutputStream(new File(out,CSS_FILES[i])));
		}
		FileOutputStream fos = new FileOutputStream(filePath+"/wiki.html");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		osw.write(html);
		osw.flush();
		osw.close();
		fos.flush();
		fos.close();
	}
	private static InputStream getInputStreamByPath(String name) {
		String path = WIKI_FILE_PATH+name;
		return WikiGenerator.class.getResourceAsStream(path);
	}
}