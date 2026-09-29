package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.math.BigDecimal;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class GetServiceDefinitionMonetaryActionsPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

		if (jsonObject.has("limits") && !jsonObject.get("limits").isJsonNull()) {
			JsonArray limits = jsonObject.get("limits").getAsJsonArray();

			for (JsonElement element : limits) {
				JsonObject Feature = element.getAsJsonObject();

				if (Feature.has("actions") && !Feature.get("actions").isJsonNull()) {
					JsonArray actions = Feature.get("actions").getAsJsonArray();
					for (JsonElement actionElement : actions) {

						JsonObject action = actionElement.getAsJsonObject();

						JsonArray actionLimits = action.get("limits").getAsJsonArray();
						for (JsonElement limitsElement : actionLimits) {

							JsonObject limit = limitsElement.getAsJsonObject();

							String value = limit.has("value")
									&& !limit.get("value").isJsonNull()
											? limit.get("value").getAsString()
											: "0.0";

							limit.addProperty("value", convertFromExponentialToNumber(value));
						}
					}
				}
			}
		}
		responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
	}

	private String convertFromExponentialToNumber(String number) {
		return new BigDecimal(number).toPlainString();
	}

}
