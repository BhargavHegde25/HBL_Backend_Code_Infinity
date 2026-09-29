package com.bct.postprocessor;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GetDetailsAccountsCustomPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();
		PayloadHandler responsePayloadHandler = fabricResponseManager.getPayloadHandler();
		JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
		alert.prepareError("In GetDetailsAccountsCustomPostProcessor responsePayload:::" + responsePayload).log();
		alert.prepareError("In GetDetailsAccountsCustomPostProcessor requestPayload:::" + requestPayload).log();

		try {
			String channel = "";
			JSONObject reportingParamsJson = getReportingParams(fabricRequestManager);
			if (reportingParamsJson != null) {
				channel = reportingParamsJson.optString("chnl");
			}
			alert.prepareError("GetDetailsAccountsCustomPostProcessor - Detected channel:::" + channel).log();
			if (channel != null && channel.equalsIgnoreCase("mobile")) {
				if (responsePayload.has("Accounts")) {
					JsonArray accounts = responsePayload.getAsJsonArray("Accounts");
					try {
						JsonArray copiedAccounts = accounts.deepCopy().getAsJsonArray();
						for (int i = 0; i < copiedAccounts.size(); i++) {
							JsonObject accCopy = copiedAccounts.get(i).getAsJsonObject();
							alert.prepareError("In GetDetailsAccountsCustomPostProcessor accCopy ::: " + accCopy).log();
							String accountType = accCopy.has("accountType") ? accCopy.get("accountType").getAsString()
									: "";
							alert.prepareError(
									"In GetDetailsAccountsCustomPostProcessor accountType ::: " + accountType).log();
							if ("Checking".equalsIgnoreCase(accountType)) {
								accCopy.addProperty("accountType", HBLConstants.ACCOUNT_TYPE_CURRENT);
							} else if ("Deposit".equalsIgnoreCase(accountType)) {
								accCopy.addProperty("accountType", HBLConstants.ACCOUNT_TYPE_FIXED_DEPOSIT);
							}
						}
						responsePayload.add("Accounts", copiedAccounts);
						alert.prepareError("In GetDetailsAccountsCustomPostProcessor accCopy responsePayload::: "
								+ responsePayload).log();
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responsePayload);
					} catch (Exception e) {
						alert.prepareError(
								"Exception occurred in GetDetailsAccountsCustomPostProcessor:::" + e.getMessage())
								.log();
					}

					alert.prepareError(
							"Final Accounts with QR:::" + responsePayload.getAsJsonArray("Accounts").toString()).log();
				}
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured in GetDetailsAccountsCustomPostProcessor:::" + e.getMessage(), e);
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

}
