package com.dbp.batchprocessengine.businessdelegate.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.batchprocessengine.businessdelegate.api.AccountsBusinessDelegate;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.controller.DataControllerRequest;

public class AccountsBusinessDelegateImpl implements AccountsBusinessDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public JsonObject getAccountDetails(String customerId, LocalDateTime lastsynctime, LocalDateTime curtime,String companyLegalUnit) {
		JsonArray accountsArray = new JsonArray();
		JsonObject accounts = new JsonObject();
		HashMap<String, Object> requestParameters = new HashMap<>();
		DataControllerRequest dc = null;
		try {
			String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON("BatchAccountsRecords", null,
					"accounts", requestParameters, null, dc);

			JsonParser parser = new JsonParser();
			accounts = parser.parse(responseString).getAsJsonObject();
			accountsArray = accounts.getAsJsonArray("accounts");
			JsonObject additionalParams = new JsonObject();
			additionalParams.addProperty("PayeeNickName", "Payee1");
			additionalParams.addProperty("ServerDate", "2020-03-01");
			additionalParams.addProperty("CreditorName", "Creditor1");
			additionalParams.addProperty("MaskedToAccount", "****8231");
			additionalParams.addProperty("MaskedFromAccount", "****9876");
			additionalParams.addProperty("AMOUNT", "2000");
			for (JsonElement account : accountsArray) {
				account.getAsJsonObject().addProperty("customerId", customerId);
				account.getAsJsonObject().addProperty("companyLegalUnit", companyLegalUnit);
				account.getAsJsonObject().addProperty("additionalParams", additionalParams.toString());
				account.getAsJsonObject().remove("accountId");
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception Occured:", e).log();
		}

		return accounts;
	}

	@Override
	public JsonObject getAccountDetails(String customerId, String accountId, LocalDateTime lastsynctime,
			LocalDateTime curtime, String companyLegalUnit) {

		JsonObject accounts = new JsonObject();
		HashMap<String, Object> requestParameters = new HashMap<>();
		DataControllerRequest dc = null;
		try {
			String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON("BatchAccountDetails", null,
					"account", requestParameters, null, dc);

			JsonParser parser = new JsonParser();
			accounts = parser.parse(responseString).getAsJsonObject();
			accounts.getAsJsonArray("accounts").get(0).getAsJsonObject().addProperty("customerId", customerId);
			accounts.getAsJsonArray("accounts").get(0).getAsJsonObject().addProperty("accountId", accountId);
			accounts.getAsJsonArray("accounts").get(0).getAsJsonObject().addProperty("companyLegalUnit", companyLegalUnit);
			JsonObject additionalParams = new JsonObject();
			additionalParams.addProperty("PayeeNickName", "Payee1");
			additionalParams.addProperty("ServerDate", "2020-03-01");
			additionalParams.addProperty("CreditorName", "Creditor1");
			additionalParams.addProperty("MaskedToAccount", "****8231");
			additionalParams.addProperty("MaskedFromAccount", "****9876");
			additionalParams.addProperty("AMOUNT", "2000");
			accounts.getAsJsonArray("accounts").get(0).getAsJsonObject().addProperty("additionalParams",
					additionalParams.toString());
		} catch (Exception e) {
			diagnostic.prepareDebug("Execption Occured:", e).log();
		}
		return accounts;

	}

}
