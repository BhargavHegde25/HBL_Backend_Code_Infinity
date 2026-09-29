package com.kony.dbputilities.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;

import eu.bitwalker.useragentutils.UserAgent;

public class UserAgentUtil {

    JSONObject reportingParams;
    String deviceName;
    String os;
    String channel;
    String platform;
    String browser;
    String deviceId;
    String appId;

    public UserAgentUtil(DataControllerRequest request) throws JSONException, UnsupportedEncodingException {
        this.reportingParams = new JSONObject(URLDecoder.decode(request.getHeader("X-Kony-ReportingParams"), "UTF-8"));
        channel = reportingParams.optString("chnl");
        platform = reportingParams.optString("plat");
        deviceId = reportingParams.optString("did");
        appId = reportingParams.optString("aid");

        if ("desktop".equalsIgnoreCase(channel)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            deviceName = userAgent.getOperatingSystem().getDeviceType().getName();
            os = userAgent.getOperatingSystem().getName();
            browser = userAgent.getBrowser().getName();
        } else {
            deviceName = reportingParams.optString("dm");
            os = platform + " " + reportingParams.optString("os");
            browser = reportingParams.optString("dm");
        }

    }

    public String getClientIp(DataControllerRequest request) {
        if (null != request.getHeader("X-Forwarded-For")) {
            return request.getHeader("X-Forwarded-For");
        }
        return "0.0.0.0";
    }

    public String getClientDevice() {
        return deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getClientOs() {
        return os;
    }

    public String getChannel() {
        return channel;
    }

    public String getPlatform() {
        return platform;
    }

    public String getBrowser() {
        return browser;
    }

    public String getAppID() {
        return appId;
    }

}
