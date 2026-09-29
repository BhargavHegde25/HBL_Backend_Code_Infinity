package com.kony.task.datavalidation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.GeneralTransactionDTO;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.TransactionManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class UpdateTransactionValidationTask implements ObjectProcessorTask {
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
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			if(HelperMethods.isMFAVerify(requestPayload)
					|| HelperMethods.isDACSkip(requestPayload)) {
				diagnostic.prepareDebug("This is MFA verification call").log();
				return true;
			}
			TransactionManager transactionManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
			String transactionId = HelperMethods.getStringFromJsonObject(requestPayload, "transactionId");
			String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "fromAccountNumber");
			String toAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "toAccountNumber");
			String extAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "ExternalAccountNumber");
			String payeeId = HelperMethods.getStringFromJsonObject(requestPayload, "payeeId");
			String payPersonId = HelperMethods.getStringFromJsonObject(requestPayload, "payPersonId");
			
			String featureActionId = HelperMethods.getStringFromJsonObject(requestPayload, MFAConstants.SERVICE_NAME);
					
			String customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			String confirmationNumber = HelperMethods.getStringFromJsonObject(requestPayload, "confirmationNumber");
			
			diagnostic.prepareDebug("validating fromAccountNumber {},toAccountNumber {}, extAccountNumber {},"
					+ " payeeId {},payPersonId {}, featureActionId {}",
					fromAccountNumber,toAccountNumber,extAccountNumber,payeeId,payPersonId,featureActionId).log();
			
			boolean isDataValid = false;
			
			if (StringUtils.isNotBlank(transactionId)) {
				isDataValid = (transactionManager.validateTransactionId(null, transactionId)
						&& transactionManager.validateTransaction(null, featureActionId, fromAccountNumber, toAccountNumber,
								extAccountNumber, payeeId, payPersonId));
			} else {
				isDataValid = validateConfirmationNumber(confirmationNumber, customerId, featureActionId);
			}
			
			if (!isDataValid) {
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
	
	public boolean validateConfirmationNumber(String confirmationNumber, String customerId, String featureActionId) {
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_featureactionlist(featureActionId);
		GeneralTransactionDTO transaction = generalTransactionBusinessDelegate.fetchExecutedTranscationEntry(confirmationNumber, featureActionId);
		if(transaction != null 
				&& confirmationNumber.equals(transaction.getConfirmationNumber())) {
					return true;
		}
		return false;
	}

}
