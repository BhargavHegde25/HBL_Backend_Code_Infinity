package com.bct.postprocessor;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class CheckAppVersionPostprocessor  implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		alert.prepareError("In CheckAppVersionPostprocessor:::").log();
		PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();
		PayloadHandler responsePayloadHandler = fabricResponseManager.getPayloadHandler();
		JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
		alert.prepareError("In CheckAppVersionPostprocessor responsePayload:::" + responsePayload).log();
		alert.prepareError("In CheckAppVersionPostprocessor requestPayload:::" + requestPayload).log();

		try {
			String channel = null;
			String platform = null;
			String clientApVersion = null;
			JSONObject reportingParamsJson = getReportingParams(fabricRequestManager);
			if (reportingParamsJson != null) {
				channel = reportingParamsJson.optString("chnl");
				platform = reportingParamsJson.optString("plat");
				clientApVersion = reportingParamsJson.optString("aver");
			}
			alert.prepareError("CheckAppVersionPostprocessor - Detected channel:::" + channel).log();
			if (channel != null && channel.equalsIgnoreCase("mobile")) {
				alert.prepareError("input parameters are" + platform +", "+ clientApVersion);

				ServicesManager manager = fabricRequestManager.getServicesManager();
				ConfigurableParametersHelper configurableParametersHelper = manager.getConfigurableParametersHelper();
				String storeURL = "";
				String minversion = "";
				String latestversion = "";
				String message = "";
				if (platform != null && platform.equalsIgnoreCase("android")) {
					storeURL = configurableParametersHelper.getServerProperty("ANDROID_STORE_URL");
					minversion = configurableParametersHelper.getServerProperty("ANDROID_MIN");
					latestversion = configurableParametersHelper.getServerProperty("ANDROID_LATEST");

					String upgradeRes = checkUpgrade(platform, clientApVersion, minversion, latestversion).toString();
					
					responsePayload.addProperty("upgrade", upgradeRes);
					responsePayload.addProperty("storeURL", storeURL);
					responsePayload.addProperty("latestVersion", latestversion);
					if (upgradeRes.equalsIgnoreCase("MANDATORY")) {
						message = "Update of the application is available. Please download the new version by clicking the Upgrade button.";
					} else if (upgradeRes.equalsIgnoreCase("OPTIONAL")) {
						message = "Update of the application is available. Please download the new version by clicking the Upgrade button. Do you want to proceed?";
					}
					responsePayload.addProperty("message", message);
				} else {

					storeURL = configurableParametersHelper.getServerProperty("IPHONE_STORE_URL");
					minversion = configurableParametersHelper.getServerProperty("IOS_MIN");
					latestversion = configurableParametersHelper.getServerProperty("IOS_LATEST");

					String upgradeRes = checkUpgrade(platform, clientApVersion, minversion, latestversion).toString();
					responsePayload.addProperty("upgrade", upgradeRes);
					responsePayload.addProperty("storeURL", storeURL);
					responsePayload.addProperty("latestVersion", latestversion);
					if (upgradeRes.equalsIgnoreCase("MANDATORY")) {
						message = "Update of the application is available. Please download the new version by clicking the Upgrade button.";
					} else if (upgradeRes.equalsIgnoreCase("OPTIONAL")) {
						message = "Update of the application is available. Please download the new version by clicking the Upgrade button. Do you want to proceed?";
					}
					responsePayload.addProperty("message", message);
				}
				
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responsePayload);
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured in CheckAppVersionPostprocessor:::" + e.getMessage(), e);
		}
	}

	public JSONObject getReportingParams(FabricRequestManager fabrequestManager) {
		String encodedReportingParams = fabrequestManager.getHeadersHandler().getHeader("X-Kony-ReportingParams");
		JSONObject reportingParamsJson = null;
		if (StringUtils.isNotBlank(encodedReportingParams)) {
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(encodedReportingParams, "utf-8"));
			} catch (Exception e) {
				alert.prepareError("error reading reporting params " + e.getLocalizedMessage(), e).log();
			}
		}
		return reportingParamsJson;
	}
	
	public static UpgradeType checkUpgrade(String platform, String currentVersion, String minRequiredVersion,
			String latestVersion) {

		if (compareVersions(currentVersion, minRequiredVersion) < 0) {
			return UpgradeType.MANDATORY;
		}

		if (compareVersions(currentVersion, latestVersion) < 0) {
			return UpgradeType.OPTIONAL;
		}

		return UpgradeType.NONE;
	}

	// Version comparator: returns -1 if v1 < v2, 0 if equal, 1 if v1 > v2
	public static int compareVersions(String v1, String v2) {
		String[] v1Parts = v1.split("\\.");
		String[] v2Parts = v2.split("\\.");

		int maxLen = Math.max(v1Parts.length, v2Parts.length);

		for (int i = 0; i < maxLen; i++) {
			int v1Segment = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
			int v2Segment = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

			if (v1Segment < v2Segment)
				return -1;
			if (v1Segment > v2Segment)
				return 1;
		}

		return 0;
	}

	public enum UpgradeType {
		NONE, // No upgrade needed
		OPTIONAL, // Upgrade available but not mandatory
		MANDATORY // Must upgrade to continue
	}
	
	public JSONObject getReportingParams(DataControllerRequest request) {
		String encodedReportingParams = request.getHeader("X-Kony-ReportingParams");
		JSONObject reportingParamsJson = null;
		if (StringUtils.isNotBlank(encodedReportingParams)) {
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(encodedReportingParams, "utf-8"));
			} catch (Exception e) {
				alert.prepareError("error reading reporting params " + e.getLocalizedMessage(), e);
			}
		}
		return reportingParamsJson;
	}


}
