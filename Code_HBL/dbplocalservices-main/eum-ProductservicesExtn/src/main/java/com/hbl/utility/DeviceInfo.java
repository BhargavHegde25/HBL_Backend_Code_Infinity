package com.hbl.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;

import eu.bitwalker.useragentutils.UserAgent;

public class DeviceInfo {
	public static JSONObject GetUserDeviceInfo(DataControllerRequest requestInstance, Map<String, Object> inputMap){
		String reportingParams= "";
		String userAgentstr = "";
		if(requestInstance!=null) {
		reportingParams=requestInstance.getHeader("X-Kony-ReportingParams");
		userAgentstr=getUserAgentStr(requestInstance.getHeader("User-Agent"));
		}else if(inputMap!=null) {
		reportingParams=inputMap.get("X-Kony-ReportingParams")!=null?inputMap.get("X-Kony-ReportingParams").toString():"";
		userAgentstr=getUserAgentStr(inputMap.get("User-Agent").toString());
		}
		
		JSONObject reportingParamsJson;
		JSONObject deviceInfo = new JSONObject();
		try {
			reportingParamsJson = new JSONObject(
			        URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
		String deviceId = reportingParamsJson.optString("did");
        String channel_id = reportingParamsJson.optString("chnl");
        String appId = reportingParamsJson.optString("aid");
        String deviceName = new String();
        String operatingSystem = new String();
        if (channel_id.equalsIgnoreCase("desktop")) {
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentstr);
            StringBuilder sb = new StringBuilder();
            sb.append(userAgent.getBrowser().getName()).append(" ")
                    .append(userAgent.getBrowserVersion().getVersion());
            deviceName = sb.toString();
            operatingSystem = userAgent.getOperatingSystem().getName();
        } else {
            deviceName = reportingParamsJson.optString("dm");
            operatingSystem = reportingParamsJson.optString("plat") + " " + reportingParamsJson.optString("os");
        }
        deviceInfo.put("deviceName", deviceName);
        deviceInfo.put("operatingSystem", operatingSystem);
        deviceInfo.put("channel_id", channel_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deviceInfo;
}
private static String getUserAgentStr(String userAgentstr) {
	if (StringUtils.isBlank(userAgentstr))
		return userAgentstr;
	Pattern pattern = Pattern.compile("(?:Edg/(([0-9]+).([0-9]*)))", Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(userAgentstr);
	if (matcher.find()) {
		userAgentstr = userAgentstr.replace(matcher.group(), matcher.group().replace("Edg", "Edge"));
	}
	return userAgentstr;
}

}
