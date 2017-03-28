package com.haojiankang.framework.commons.utils.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.haojiankang.framework.commons.utils.Sharp;

public class MessageUtil {

	private static final String host = "http://sms.market.alicloudapi.com";
	private static final String path = "/singleSendSms";
	private static final String method = "GET";

	/**
	 * map:对象 ParamString：消息模板中的动态参数的值json格式，消息总长度不大于70字数.列如：{\"key\":\"value\"}
	 * RecNum：接受消息人号码(可多个) 列如：18911090932,18911090932 TemplateCode 消息模板代码
	 * APPCODE：系统认证编码 选传参数 SignName：签名值 选传参数。默认是:"远程诊疗协同平台"
	 * 
	 * @param map
	 * 
	 *            <pre>
	 *            public static void main(String[] args) {
	 *            	Map map = new HashMap<>();
	 *            	map.put("ParamString", "{\"patient\":\"项羽\",\"requstType\":\"心电\"}");
	 *            	map.put("RecNum", "17701338992");
	 *            	map.put("TemplateCode", "SMS_37045062");
	 *            	sendMessage(map);
	 *            }
	 *            </pre>
	 */
	public static void sendMessage(Map<String, String> map) throws Exception {

		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization","APPCODE " + (map.get("APPCODE") == null ? "3150ed3dd3214d88a840164d104e8278" : map.get("APPCODE")));
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("ParamString", map.get("ParamString"));
		querys.put("RecNum", map.get("RecNum"));
		querys.put("SignName", map.get("SignName") == null ? "远程诊疗协同平台" : map.get("SignName"));
		querys.put("TemplateCode", map.get("TemplateCode"));

		JsonNode result = null;
		/**
		 * 发送http请求并接受结果
		 */
		HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
		// System.out.println(response.toString());
		// 获取response的body
		String resultStr = EntityUtils.toString(response.getEntity());
		result = (JsonNode) (Sharp.strToJsonObj(resultStr));
		boolean isTrue = result.findValue("success").asBoolean();
		map.put("success", String.valueOf(isTrue));
		if (!isTrue) {
			map.put("message", result.findValue("message").toString());
		}
	}

}
