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

public class AccountValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if(!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		String accountNumberInQP = fabricRequestManager.getQueryParamsHandler().getParameter("accountNumber");
		String accountNumber = null;
		String accountID = null;
		String accountId = null;
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			accountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "accountNumber");
			accountID = HelperMethods.getStringFromJsonObject(requestPayload, "accountID");
			accountId = HelperMethods.getStringFromJsonObject(requestPayload, "Account_id");
		}
		if (StringUtils.isNotBlank(accountId) ||
				StringUtils.isNotBlank(accountID) ||
				StringUtils.isNotBlank(accountNumber) ||
				StringUtils.isNotBlank(accountNumberInQP) ) {
			AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
			diagnostic.prepareDebug("validating account accountNumber : {}, accountID : {}, accountId : {}, accountNumberInQP {}",
					accountNumber, accountID, accountId, accountNumberInQP).log();
			if (!accountManager.validateInternalAccount(null, accountNumber)
					&& !accountManager.validateInternalAccount(null, accountID)
					&& !accountManager.validateInternalAccount(null, accountId)
					&& !accountManager.validateInternalAccount(null, accountNumberInQP)) {
				JsonObject resPayload = null;
				if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
					resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
				}
				resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
			}
		}
		
		return true;
	}

}
