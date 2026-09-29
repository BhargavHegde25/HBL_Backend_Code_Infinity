package com.temenos.infinity.api.arrangements.task;


import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.memorymgmt.AccountsManager;
import  com.temenos.infinity.api.arrangements.task.AccountValidationTask;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class AccountValidationTask implements ObjectProcessorTask {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		String accountNumberInQP = fabricRequestManager.getQueryParamsHandler().getParameter("accountNumber");
		String accountNumber = null;
		String accountID = null;
		String accountId = null;
		if (!CommonUtils.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			accountNumber = CommonUtils.getStringFromJsonObject(requestPayload, "accountNumber");
			accountID = CommonUtils.getStringFromJsonObject(requestPayload, "accountID");
			accountId = CommonUtils.getStringFromJsonObject(requestPayload, "Account_id");
		}
		if (StringUtils.isNotBlank(accountId) ||
				StringUtils.isNotBlank(accountID) ||
				StringUtils.isNotBlank(accountNumber) ||
				StringUtils.isNotBlank(accountNumberInQP) ) {
			AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
			diagnostic.prepareDebug("validating account accountNumber : {}, accountID : {}, accountId : {}, accountNumberInQP {}",
					accountNumber, accountID, accountId, accountNumberInQP).log();
		/*	if (!accountManager.validateInternalAccount(null, accountNumber)
					&& !accountManager.validateInternalAccount(null, accountID)
					&& !accountManager.validateInternalAccount(null, accountId)
					&& !accountManager.validateInternalAccount(null, accountNumberInQP)) {
				JsonObject resPayload = null;
				if (!CommonUtils.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
					resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
				}
				resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
			}*/
		}
		
		return true;
	}

}
