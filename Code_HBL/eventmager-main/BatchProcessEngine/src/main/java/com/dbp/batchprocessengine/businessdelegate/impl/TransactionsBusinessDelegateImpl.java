package com.dbp.batchprocessengine.businessdelegate.impl;

import java.time.LocalDateTime;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.batchprocessengine.businessdelegate.api.TransactionsBusinessDelegate;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.controller.DataControllerRequest;

public class TransactionsBusinessDelegateImpl implements TransactionsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public JsonObject getAllTransactions(String customerId, String accountId, LocalDateTime lastsynctime,
			LocalDateTime curtime,String companyLegalUnit) {
		JsonObject transactions = new JsonObject();
		JsonArray transactionsArray = new JsonArray();
		HashMap<String, Object> requestParameters = new HashMap<>();
		DataControllerRequest dc = null;
		try {
			String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON("BatchTransactionsRecords",
					null, "transactions", requestParameters, null, dc);
			JsonParser parser = new JsonParser();
			transactions = parser.parse(responseString).getAsJsonObject();
			transactionsArray = transactions.getAsJsonArray("transactions");
			JsonObject additionalParams = new JsonObject();
			additionalParams.addProperty("PayeeNickName", "Payee1");
			additionalParams.addProperty("ServerDate", "2020-03-01");
			additionalParams.addProperty("CreditorName", "Creditor1");
			additionalParams.addProperty("MaskedToAccount", "****8231");
			additionalParams.addProperty("AMOUNT", "2000");
			for (JsonElement transaction : transactionsArray) {
				transaction.getAsJsonObject().addProperty("customerId", customerId);
				transaction.getAsJsonObject().addProperty("companyLegalUnit", companyLegalUnit);
				transaction.getAsJsonObject().addProperty("accountId", accountId);
				transaction.getAsJsonObject().addProperty("additionalParams", additionalParams.toString());
			}
		} catch (Exception e) {
			alert.prepareError("exception occurred in business layer ", e).log();
		}
		return transactions;
	}

	@Override
	public JsonObject getAllTransactions(String customerId, LocalDateTime lastsynctime, LocalDateTime curtime,String companyLegalUnit){

		JsonObject transactions = new JsonObject();
		JsonArray transactionsArray = new JsonArray();
		HashMap<String, Object> requestParameters = new HashMap<>();
		DataControllerRequest dc = null;
		try {
			String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON("BatchTransactionsRecords",
					null, "transactions", requestParameters, null, dc);
			JsonParser parser = new JsonParser();
			transactions = parser.parse(responseString).getAsJsonObject();
			transactionsArray = transactions.getAsJsonArray("transactions");
			JsonObject additionalParams = new JsonObject();
			additionalParams.addProperty("PayeeNickName", "Payee1");
			additionalParams.addProperty("ServerDate", "2020-03-01");
			additionalParams.addProperty("CreditorName", "Creditor1");
			additionalParams.addProperty("MaskedToAccount", "****8231");
			additionalParams.addProperty("AMOUNT", "2000");

			for (JsonElement transaction : transactionsArray) {
				transaction.getAsJsonObject().addProperty("customerId", customerId);
				transaction.getAsJsonObject().addProperty("companyLegalUnit", companyLegalUnit);
				transaction.getAsJsonObject().remove("accountId");
				transaction.getAsJsonObject().addProperty("additionalParams", additionalParams.toString());
			}
		} catch (Exception e) {
			alert.prepareError("exception occurred in business layer ", e).log();
		}
		return transactions;
	}

}
