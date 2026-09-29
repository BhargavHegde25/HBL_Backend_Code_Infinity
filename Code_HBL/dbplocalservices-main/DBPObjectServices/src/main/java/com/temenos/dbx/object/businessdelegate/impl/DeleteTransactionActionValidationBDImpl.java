package com.temenos.dbx.object.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.dbx.actions.businessdelegate.api.AccountActionBusinessDelegate;
import com.temenos.dbx.constants.ActionConstant;
import com.temenos.dbx.transaction.businessdelegate.api.TransactionReportBusinessDelegate;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public class DeleteTransactionActionValidationBDImpl implements ObjectProcessorTask {
	private final LoggerUtil logger = new LoggerUtil(DeleteTransactionActionValidationBDImpl.class);
	private static final String TRANSACTION_ID_FIELD = "transactionId";
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			
			AccountActionBusinessDelegate accountActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountActionBusinessDelegate.class);
			String userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			String transactionId = HelperMethods.getStringFromJsonObject(requestPayload, TRANSACTION_ID_FIELD);
			
			TransactionReportBusinessDelegate transReportBusinessdelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(TransactionReportBusinessDelegate.class);
			TransactionDTO transaction = transReportBusinessdelegate.getTransactionById(transactionId, fabricRequestManager
					.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
			diagnostic.prepareDebug("from account number "+transaction.getFromAccountNumber()+", service name "+transaction.getServiceName()).log();
			if (!accountActionBusinessDelegate.hasUserAccountFeatureAction(userId, transaction.getFromAccountNumber(),
					ActionConstant.valueOf(transaction.getServiceName()))) {
				JsonObject resPayload = null;
				if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
					resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
				}
				resPayload = ErrorCodeEnum.ERR_12007.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
			}
		}
		return true;
	}

}
