package com.kony.eventdispatcher.operations;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

import eu.bitwalker.useragentutils.UserAgent;

public class ReportingParamsOperations {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static JsonObject getReportingParams(DataControllerRequest request, String reportingParamsString) {
		if (StringUtils.isNotBlank(reportingParamsString)) {
			try {
				String decodedString = java.net.URLDecoder.decode(reportingParamsString, StandardCharsets.UTF_8.name());
				JsonObject reportingParamsObject = new Gson().fromJson(decodedString, JsonObject.class);
				UserAgent parsedUserAgentString = null;
				try {
					String userAgent = reportingParamsObject.get("ua").toString();
					userAgent = getUserAgentStr(userAgent);
					parsedUserAgentString = UserAgent.parseUserAgentString(userAgent);
					if (parsedUserAgentString != null)
						reportingParamsObject.addProperty("ua", parsedUserAgentString.toString());
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
				try {
					JsonElement channelId = reportingParamsObject.get("chnl");
					String devicename = "";
					String os = "";
					if (channelId != null && (channelId.toString().equalsIgnoreCase("desktop")
							|| channelId.toString().equalsIgnoreCase("\"desktop\""))) {
						UserAgent userAgent = parsedUserAgentString;
						if (userAgent != null) {
							StringBuilder sb = new StringBuilder();
							sb.append(userAgent.getBrowser().getName()).append(" ")
									.append(userAgent.getBrowserVersion().getVersion());
							devicename = sb.toString();
							os = userAgent.getOperatingSystem().getName();
							reportingParamsObject.addProperty("os", os);
							reportingParamsObject.addProperty("dm", devicename);
						}
					} else {
						if (reportingParamsObject.get("") != null)
							reportingParamsObject.addProperty("os",
									reportingParamsObject.get("plat") + " " + reportingParamsObject.get("os"));
					}
				} catch (Exception e) {
					alert.prepareError("Error occured in fetching OS", e).log();
				}
				try {
					String ip = request.getHeaderMap().get(URLConstants.CLIENT_IP).toString();
					ip = StringUtils.isBlank(ip) ? HelperMethods.getDecyptedClientIpAddressFromCache(request)
							: HelperMethods.decryptClientIp(ip);
					reportingParamsObject.addProperty(URLConstants.IPADDRESS, ip);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
			} catch (Exception e) {
				alert.prepareError("Reporting Params Exception ", e).log();
			}

		}
		return new JsonObject();
	}

	private static String getUserAgentStr(String userAgentstr) {
		if (StringUtils.isBlank(userAgentstr))
			return userAgentstr;
		Pattern pattern = Pattern.compile(URLConstants.PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgentstr);
		if (matcher.find()) {
			userAgentstr = userAgentstr.replace(matcher.group(),
					matcher.group().replace(URLConstants.REPLACE, URLConstants.REPLACE_WITH));
		}
		return userAgentstr;
	}

	public static JsonObject getReportingParams(FabricRequestManager fabricRequestManager) {
		try {
			String reportingParamsString = fabricRequestManager.getServicesManager().getDeviceRequestData()
					.getReportingParams();
			if (StringUtils.isNotEmpty(reportingParamsString)) {
				String decodedString = java.net.URLDecoder.decode(reportingParamsString, StandardCharsets.UTF_8.name());
				JsonObject reportingParams = new Gson().fromJson(decodedString, JsonObject.class);
				UserAgent userAgent = null;
				try {
					String userAgentstr = reportingParams.get("ua").toString();
					userAgentstr = getUserAgentStr(userAgentstr);
					userAgent = UserAgent.parseUserAgentString(userAgentstr);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
				try {
					JsonElement channelId = reportingParams.get("chnl");
					String devicename = "";
					String os = "";
					if (channelId != null && (channelId.toString().equalsIgnoreCase("desktop")
							|| channelId.toString().equalsIgnoreCase("\"desktop\""))) {

						if (fabricRequestManager.getHeadersHandler().getHeader("User-Agent") != null
								&& userAgent == null) {
							String userAgentStr = getUserAgentStr(
									fabricRequestManager.getHeadersHandler().getHeader("User-Agent"));
							userAgent = UserAgent.parseUserAgentString(userAgentStr);
						}
						if (userAgent != null) {
							StringBuilder sb = new StringBuilder();
							sb.append(userAgent.getBrowser().getName()).append(" ")
									.append(userAgent.getBrowserVersion().getVersion());
							devicename = sb.toString();
							os = userAgent.getOperatingSystem().getName();
							reportingParams.addProperty("os", os);
							reportingParams.addProperty("dm", devicename);
						}
					} else {
						if (reportingParams.get("") != null)
							reportingParams.addProperty("os",
									reportingParams.get("plat") + " " + reportingParams.get("os"));
					}
					if (userAgent != null)
						reportingParams.addProperty("ua", userAgent.toString());
				} catch (Exception e) {
					alert.prepareError("Error occured in fetching OS", e).log();
				}
				
				String ip = fabricRequestManager.getHeadersHandler().getHeader(URLConstants.CLIENT_IP);
				ip = StringUtils.isBlank(ip) 
						? HelperMethods.getDecyptedClientIpAddressFromCache(fabricRequestManager)
						: HelperMethods.decryptClientIp(ip);
				reportingParams.addProperty(URLConstants.IPADDRESS, ip);
			
				return reportingParams;
			}
		} catch (Exception e) {
			alert.prepareError("Reporting Params Exception ", e).log();
		}

		return new JsonObject();
	}

}
