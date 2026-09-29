package com.hbl.productservicesExtn.utills;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;

import eu.bitwalker.useragentutils.UserAgent;

public class HBLUtility {

	public static JSONObject getDeviceInfo(DataControllerRequest requestInstance){
		String reportingParams= requestInstance.getHeader("X-Kony-ReportingParams");
		
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
			String userAgentstr = getUserAgentStr(requestInstance.getHeader("User-Agent"));
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
		

      /*  
		String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
        String deviceID = requestInstance.getParameter("Device_id");
        String customerID = requestInstance.getParameter("Customer_id");
        String statusID = requestInstance.getParameter("Status_id");
        String deviceName = requestInstance.getParameter("DeviceName");
        String lastUsedIp = requestInstance.getParameter("LastUsedIp");
        String lastLoginTime = requestInstance.getParameter("LastLoginTime");
        String operatingSystem = requestInstance.getParameter("OperatingSystem");
        String channel_id = requestInstance.getParameter("channel_id");
        String isTracking = requestInstance.getParameter("isTracking");
        String appid = requestInstance.getParameter("appid");
        String username = null;

        if ((customerID == null || StringUtils.isBlank(customerID))
                && requestInstance.getParameter("username") != null) {
            requestInstance.setAttribute("isServiceBeingAccessedByOLB", true);
            username = requestInstance.getParameter("username");
            if (StringUtils.isBlank(username)) {
                ErrorCodeEnum.ERR_20612.setErrorCode(processedResult);
                Param statusParam = new Param("Status", "Failure", FabricConstants.STRING);
                processedResult.addParam(statusParam);
                return processedResult;
            }
           
        }

        if (StringUtils.isNotBlank(channel_id)) {
            if (channel_id.equalsIgnoreCase("Mobile"))
                channel_id = "CH_ID_MOB";
            else if (channel_id.equalsIgnoreCase("Desktop")) {
                channel_id = "CH_ID_INT";
                if (operatingSystem.contains("Android") || operatingSystem.contains("iPhone")
                        || operatingSystem.contains("iPad")) {
                    channel_id = "CH_ID_MOB_INT";
                }
            } else if (channel_id.equalsIgnoreCase("Tablet"))
                channel_id = "CH_ID_TABLET";
            else if (channel_id.equalsIgnoreCase("MobileWeb"))
                channel_id = "CH_ID_MOB_INT";
            else
                channel_id = "CH_ID_MOB";
        } else {
            channel_id = "CH_ID_MOB";
        }

        Map<String, String> postParametersMap = new HashMap<String, String>();
		
	}
	*/
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
