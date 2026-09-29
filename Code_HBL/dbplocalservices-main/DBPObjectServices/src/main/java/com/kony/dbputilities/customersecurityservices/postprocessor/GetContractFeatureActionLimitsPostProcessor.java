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
import com.temenos.dbx.product.utils.InfinityConstants;

public class GetContractFeatureActionLimitsPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

		if (jsonObject.has(InfinityConstants.limits) && !jsonObject.get(InfinityConstants.limits).isJsonNull()) {
			JsonArray limits = jsonObject.get(InfinityConstants.limits).getAsJsonArray();

			for (JsonElement element : limits) {
				JsonObject company = element.getAsJsonObject();

				if (company.has(InfinityConstants.contractCustomerLimits)
						&& !company.get(InfinityConstants.contractCustomerLimits).isJsonNull()) {
					JsonArray contractCustomerLimits = company.get(InfinityConstants.contractCustomerLimits)
							.getAsJsonArray();
					for (JsonElement contractCustomerLimitsElement : contractCustomerLimits) {

						JsonObject contractCustomerLimit = contractCustomerLimitsElement.getAsJsonObject();

						if (contractCustomerLimit.has(InfinityConstants.actions)
								&& !contractCustomerLimit.get(InfinityConstants.actions).isJsonNull()) {
							JsonArray actions = contractCustomerLimit.get(InfinityConstants.actions).getAsJsonArray();
							for (JsonElement actionElement : actions) {

								JsonObject action = actionElement.getAsJsonObject();

								JsonArray actionLimits = action.get(InfinityConstants.limits).getAsJsonArray();
								for (JsonElement limitsElement : actionLimits) {

									JsonObject limit = limitsElement.getAsJsonObject();

									String value = limit.has(InfinityConstants.value)
											&& !limit.get(InfinityConstants.value).isJsonNull()
													? limit.get(InfinityConstants.value).getAsString()
													: "0.0";

									limit.addProperty(InfinityConstants.value, convertFromExponentialToNumber(value));
								}
							}
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
