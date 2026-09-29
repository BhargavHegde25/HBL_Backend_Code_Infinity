package com.dbp.reminderengine.businessdelegate.impl;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.reminderengine.businessdelegate.api.ProcessAlertsBusinessDelegate;
import com.dbp.reminderengine.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProcessAlertsBusinessDelegateImpl implements ProcessAlertsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public JsonArray getCustomerDetails(String customerDetails) {
		JsonArray resArray = new JsonArray();
		try {
			JsonObject customParams = getCustomParams();
			JsonArray customerDetailsArr = new JsonParser().parse(customerDetails).getAsJsonArray();
			for (JsonElement customerElement : customerDetailsArr) {
				if (customerElement.isJsonObject()) {
					JsonObject customer = customerElement.getAsJsonObject();
					customer.add("customparams", customParams);
					resArray.add(customer);
				}
			}
			return resArray;
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return new JsonArray();
	}

	private static JsonObject getCustomParams() {
		try {
			String responseString = DBPServiceExecutorBuilder.builder().withOperationId(Constants.STUB_OPERATION)
					.withRequestParameters(new HashMap<>()).withServiceId(Constants.REMINDERENGINESTUBSERVICE).build()
					.getResponse();
			return new JsonParser().parse(responseString).getAsJsonObject().get("customparams").getAsJsonObject();
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return new JsonObject();

	}

}
