package com.kony.task.datavalidation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.AccountsManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class BulkTransferValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if (!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}

		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			if (HelperMethods.isMFAVerify(requestPayload)) {
				diagnostic.prepareDebug("This is MFA verification call").log();
				return true;
			}
			AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);

			String bulkTransferString = requestPayload.get("bulkTransferString").toString();
			JsonPrimitive convertedObject = new Gson().fromJson(bulkTransferString,JsonPrimitive.class);
			JsonArray resultarray = new Gson().fromJson(convertedObject.getAsString(),JsonArray.class);
			
			diagnostic.prepareDebug("bulkTransferString {}", resultarray).log();

			List<Map<String, Object>> inputs = null;
			if (StringUtils.isNotBlank(requestPayload.get("bulkTransferString").toString())) {
				inputs = getFormattedInput(resultarray);			
			}
			if (inputs != null) {
				for (Map<String, Object> input : inputs) {
					diagnostic.prepareDebug("validating fromAccountNumber {}", input.get("fromAccountNumber")).log();
					if (accountManager.validateInternalAccount(null, input.get("fromAccountNumber").toString())) {
						JsonObject resPayload = null;
						if (!HelperMethods
								.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
							resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
						}
						resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
						return false;
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> getFormattedInput(JsonArray jArray) {
		List<Map<String, Object>> inputs = new ArrayList<>();
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		if (jArray.isJsonArray()) {
			for (int i = 0; i < jArray.size(); i++) {
				Map<String, Object> temp = (Map<String, Object>) gson.fromJson(jArray.get(i), type);
				inputs.add(temp);
			}
		}
		return inputs;
	}

}
