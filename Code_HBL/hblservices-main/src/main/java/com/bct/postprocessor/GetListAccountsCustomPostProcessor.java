package com.bct.postprocessor;

import java.net.URLDecoder;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.bct.javaservices.QRGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GetListAccountsCustomPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		alert.prepareError("In GetListAccountsCustomPostProcessor:::").log();
		PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();
		PayloadHandler responsePayloadHandler = fabricResponseManager.getPayloadHandler();
		JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
		alert.prepareError("In GetListAccountsCustomPostProcessor responsePayload:::" + responsePayload).log();
		alert.prepareError("In GetListAccountsCustomPostProcessor requestPayload:::" + requestPayload).log();

		try {
			String channel = "";
			JSONObject reportingParamsJson = getReportingParams(fabricRequestManager);
			if (reportingParamsJson != null) {
				channel = reportingParamsJson.optString("chnl");
			}
			alert.prepareError("GetListAccountsCustomPostProcessor - Detected channel:::" + channel).log();
			if (channel != null && channel.equalsIgnoreCase("mobile")) {
				if (responsePayload.has("Accounts")) {
					JsonArray accounts = responsePayload.getAsJsonArray("Accounts");
					try {
						JsonArray copiedAccounts = accounts.deepCopy().getAsJsonArray();
						for (int i = 0; i < copiedAccounts.size(); i++) {
							JsonObject accCopy = copiedAccounts.get(i).getAsJsonObject();
							alert.prepareError("In GetListAccountsCustomPostProcessor accCopy ::: " + accCopy).log();
							String accountType = accCopy.has("accountType") ? accCopy.get("accountType").getAsString()
									: "";
							alert.prepareError("In GetListAccountsCustomPostProcessor accountType ::: " + accountType)
									.log();
							if ("Checking".equalsIgnoreCase(accountType)) {
								accCopy.addProperty("accountType", HBLConstants.ACCOUNT_TYPE_CURRENT);
							} else if ("Deposit".equalsIgnoreCase(accountType)) {
								accCopy.addProperty("accountType", HBLConstants.ACCOUNT_TYPE_FIXED_DEPOSIT);
							}
							try {
								QRGenerator qr = new QRGenerator();
								String base64QR = qr.generateQRData(accCopy);
								alert.prepareError(
										"In GetListAccountsCustomPostProcessor accCopy base64QR::: " + base64QR).log();
								String encodedBase64QR = Base64.getEncoder().encodeToString(base64QR.getBytes());
								alert.prepareError("In GetListAccountsCustomPostProcessor accCopy encodedBase64QR::: "
										+ encodedBase64QR).log();
								accCopy.addProperty("base64EncodedQR", encodedBase64QR);

							} catch (Exception e) {
								alert.prepareError(
										"QR generation failed for account index " + i + " : " + e.getMessage()).log();
								accCopy.addProperty("base64EncodedQR",
										"ERROR: QR generation failed - " + e.getMessage());
							}
						}
						responsePayload.add("Accounts", copiedAccounts);
						alert.prepareError(
								"In GetListAccountsCustomPostProcessor accCopy responsePayload::: " + responsePayload)
								.log();
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responsePayload);
					} catch (Exception e) {
						alert.prepareError("QR generation failed, keeping original accounts :::" + e.getMessage())
								.log();
					}

					alert.prepareError(
							"Final Accounts with QR:::" + responsePayload.getAsJsonArray("Accounts").toString()).log();
				}
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured in GetListAccountsCustomPostProcessor:::" + e.getMessage(), e);
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
