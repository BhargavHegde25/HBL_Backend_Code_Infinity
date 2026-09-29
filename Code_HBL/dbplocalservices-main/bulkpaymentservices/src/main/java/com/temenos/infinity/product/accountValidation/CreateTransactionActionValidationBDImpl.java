package com.temenos.infinity.product.accountValidation;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.AccountsManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.scaintegration.helper.Helper;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.dbx.actions.businessdelegate.api.AccountActionBusinessDelegate;
import com.temenos.dbx.constants.ActionConstant;

public class CreateTransactionActionValidationBDImpl implements ObjectProcessorTask {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String FROM_ACCT_NUMBER_FIELD = "fromAccount";

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();

			if (HelperMethods.isMFAVerify(requestPayload) || HelperMethods.isDACSkip(requestPayload)
					|| Helper.isScaVerify(requestPayload)) {
				return true;
			}

			String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, FROM_ACCT_NUMBER_FIELD);

			AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);

			String userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			diagnostic.prepareDebug("from account number {}, service name {}", fromAccountNumber).log();
			SessionMap accounts = accountManager.getInternalBankAccountsFromSession(userId);
			if (!(accounts.hasKey(fromAccountNumber))) {
				updateErrorCode(fabricResponseManager);
				return false;
			}
		}
		return true;
	}

	private void updateErrorCode(FabricResponseManager fabricResponseManager) {
		JsonObject resPayload = null;
		if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
			resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		}
		resPayload = ErrorCodeEnum.ERR_12007.setErrorCode(resPayload);
		fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
	}

}
