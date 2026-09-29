package com.temenos.infinity.api.holdings.datavalidation;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.AccountsManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class DisputeFromAccountValidation implements ObjectProcessorTask{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if(!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		String accountNumber = null;
		String transactionType = null;
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			accountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "fromAccountNumber");
			transactionType = HelperMethods.getStringFromJsonObject(requestPayload, "transactionType");
			diagnostic.prepareDebug("HBL## transactionType:"+ transactionType).log();
		}
	if (!"Cards".equals(transactionType)){
		diagnostic.prepareDebug("HBL## transactionType inside if:").log();
		if(StringUtils.isNotBlank(accountNumber)) {
			if(accountNumber.contains("-"))
				accountNumber= accountNumber.split("-")[1];
			AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
			diagnostic.prepareDebug("validating account accountNumber : {}",accountNumber).log();
			if (!accountManager.validateInternalAccount(null, accountNumber)) {
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
}
