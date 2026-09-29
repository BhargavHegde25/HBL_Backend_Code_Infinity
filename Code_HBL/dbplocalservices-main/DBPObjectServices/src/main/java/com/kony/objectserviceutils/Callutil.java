package com.kony.objectserviceutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.postprocessors.UpdateCustomerDetailsObjectServicePostProcessor;
import com.kony.utilities.CallableUtil;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class Callutil implements Callable<CallableUtil> {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	FabricRequestManager fabreq;
	FabricResponseManager fabresp;

	public Callutil(FabricRequestManager fabreq, FabricResponseManager fabresp) {
		this.fabreq = fabreq;
		this.fabresp = fabresp;

	}

	@Override
	public CallableUtil call() throws Exception {
		Map<String, Object> inputParams = new HashMap<>();

		CallableUtil cUtil = new CallableUtil(this.fabresp, this.fabreq);
		JsonObject custparams = new JsonObject();
		inputParams.put("eventCode", "UPDATEPHONE");
		inputParams.put("customParams", custparams);
		try {
			String corecustid = getCoreBackendId(fabreq);
			inputParams.put("coreCustomerId", corecustid);
			ServiceCallHelper.invokePassThroughServiceAndGetResult(fabreq, inputParams, null, "External.pushEvents");
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}

		return cUtil;
	}

	private static String getCoreBackendId(FabricRequestManager fabreq) throws Exception {

		String backendId = null;
		if (fabreq.getServicesManager().getIdentityHandler() != null) {
			Map<String, Object> userAttributesMap = fabreq.getServicesManager().getIdentityHandler()
					.getUserAttributes();
			String backendIdentifier = (String) userAttributesMap.get("backendIdentifiers");
			if (StringUtils.isNotBlank(backendIdentifier)) {
				backendId = getCoreIDFromJson(backendIdentifier);
			}
		}
		return backendId;

	}

	protected static String getCoreIDFromJson(String backendIdentifier) {
		String backendId = null;
		try {
			JsonObject backendIdentifiersJSON = new JsonParser().parse(backendIdentifier).getAsJsonObject();
			Set<java.util.Map.Entry<String, JsonElement>> valueEntry = backendIdentifiersJSON.entrySet();
			if (valueEntry.size() == 1) {
				for (java.util.Map.Entry<String, JsonElement> key : valueEntry) {
					backendId = getBackendIdFromCoreType(backendIdentifiersJSON, key.getKey());

				}
			} else {
				String coreType = ServicesManagerHelper.getServicesManager().getConfigurableParametersHelper()
						.getServerProperty("CAMPAIGN_CORETYPE");
				if (coreType == null)
					coreType = "";
				if (StringUtils.isNotEmpty(coreType) && backendIdentifiersJSON.has(coreType)) {
					backendId = getBackendIdFromCoreType(backendIdentifiersJSON, coreType);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured" + e).log();
		}
		return backendId;
	}

	protected static String getBackendIdFromCoreType(JsonObject backendIdentifiersJSON, String key) {
		JsonArray backendTypeObj = backendIdentifiersJSON.get(key).getAsJsonArray();
		String backendId = null;
		if (backendTypeObj.size() > 0) {
			backendId = backendTypeObj.get(0).getAsJsonObject().get("BackendId").getAsString();
		}
		return backendId;
	}

}