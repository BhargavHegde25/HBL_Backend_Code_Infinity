package com.kony.task.datavalidation;

import org.apache.commons.lang3.StringUtils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.AccountsManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CreateOneTimeTransferServiceValidation implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		// TODO Auto-generated method stub
		AccountsManager accountsManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
		SessionMap accounts = accountsManager
				.getInternalBankAccountsFromSession(HelperMethods.getCustomerIdFromSession(fabricRequestManager));

		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			String featureActionId = HelperMethods.getStringFromJsonObject(requestPayload, MFAConstants.SERVICE_NAME);
			double amount = Double.parseDouble(HelperMethods.getStringFromJsonObject(requestPayload, "amount"));
			double transactionAmount = Double.parseDouble(HelperMethods.getStringFromJsonObject(requestPayload, "transactionAmount") != null
							? HelperMethods.getStringFromJsonObject(requestPayload, "transactionAmount") : "0.0");
			String swiftCode = HelperMethods.getStringFromJsonObject(requestPayload, "swiftCode");
			String clearingCode = HelperMethods.getStringFromJsonObject(requestPayload, "clearingCode");
			String clearingIdentifierCode = HelperMethods.getStringFromJsonObject(requestPayload, "clearingIdentifierCode");
			String townName = HelperMethods.getStringFromJsonObject(requestPayload, "townName");
			String countryName = HelperMethods.getStringFromJsonObject(requestPayload, "countryName");
			String intermediaryBicCode = HelperMethods.getStringFromJsonObject(requestPayload, "intermediaryBicCode");
			String bankName = HelperMethods.getStringFromJsonObject(requestPayload, "beneficiaryBankName");

			if (null == accounts || accounts.isEmpty()) {
				return updateErrorResult(fabricResponseManager);
			}
			String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "fromAccountNumber");
			if (!accounts.hasKey(fromAccountNumber)) {
				alert.prepareError("Invalid FromAccount").log();
				return updateErrorResult(fabricResponseManager);
			}
			if(amount < 0 || transactionAmount<0){
				alert.prepareError("Amount and transactionAmount value cannot be negative").log();
				return updateErrorResult(fabricResponseManager);
			}
			if ((StringUtils.isNotBlank(featureActionId)
					&& TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID.equalsIgnoreCase(featureActionId))) {
				if (StringUtils.isBlank(swiftCode))
					if (StringUtils.isBlank(clearingIdentifierCode) || StringUtils.isBlank(clearingCode)) {
						alert.prepareError("Mandatory fields missing").log();
						return updateErrorResult(fabricResponseManager);
					}
			}
			if ((StringUtils.isNotBlank(featureActionId)
					&& TransferConstants.INTERNATIONAL_TRANSFER_SERVICE_ID.equalsIgnoreCase(featureActionId))) {
				if (StringUtils.isBlank(swiftCode)) {
					if (StringUtils.isBlank(clearingIdentifierCode) || StringUtils.isBlank(clearingCode)) {
						if (StringUtils.isBlank(bankName)
								|| ((StringUtils.isBlank(townName) || StringUtils.isBlank(countryName))
										&& StringUtils.isBlank(intermediaryBicCode))) {
							alert.prepareError("Mandatory fields missing").log();
							return updateErrorResult(fabricResponseManager);
						}
					}
				}
			}
		}
		return true;
	}

	private static boolean updateErrorResult(FabricResponseManager fabricResponseManager) {
		JsonObject resPayload = null;
		if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
			resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		}
		resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
		fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
		return false;
	}
}