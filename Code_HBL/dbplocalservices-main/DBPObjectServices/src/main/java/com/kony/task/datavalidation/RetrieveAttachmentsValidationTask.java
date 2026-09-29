package com.kony.task.datavalidation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.TransactionManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class RetrieveAttachmentsValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if (!isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		diagnostic.prepareDebug("data access control is enabled").log();
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			String transactionId = HelperMethods.getStringFromJsonObject(requestPayload, "transactionId");
			diagnostic.prepareDebug("validating transactionId {}",transactionId).log();
			if(StringUtils.isNotBlank(transactionId)) {
				TransactionManager transactionManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
				if (!transactionManager.validateTransactionId(null, transactionId)) {
					JsonObject resPayload = null;
					if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
						resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
					}
					resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
					fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
					return false;
				}
		    }
	    }
		return true;
	}

	public boolean isDACEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDacEnabled = configurableParametersHelper.getServerProperty("DAC_ENABLED");
			return StringUtils.isBlank(isDacEnabled)
					|| BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DAC_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}
}
