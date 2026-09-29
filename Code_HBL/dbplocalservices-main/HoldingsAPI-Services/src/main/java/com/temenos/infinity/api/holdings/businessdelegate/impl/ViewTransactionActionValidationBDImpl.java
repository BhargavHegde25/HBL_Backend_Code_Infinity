package com.temenos.infinity.api.holdings.businessdelegate.impl;

import java.util.Iterator;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.api.holdings.businessdelegate.api.AccountActionBusinessDelegate;
import com.temenos.infinity.api.holdings.constants.ActionConstant;
import com.temenos.infinity.api.holdings.constants.ServiceNameConstant;

public class ViewTransactionActionValidationBDImpl implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	AccountActionBusinessDelegate accountActionBusinessDelegate = null;
	private static final String FROM_ACCOUNT_NUM = "fromAccountNumber";
	private static final String SERVICE_NAME = "serviceName";
	private static final String IS_DISPUTED = "isDisputed";
	private static final String DS_NAME = "records";
	private static final String CV_DS_NAME = "Transactions";
	private static final String ONE_TIME_TRANSFER = "OneTimeTransfer";
	private static final String RECURRING_TRANSFER = "StandingInstruction";
	private String userId = null;

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
		String dsName = "";
		if(JSONUtil.hasKey(response, ONE_TIME_TRANSFER))
		    dsName = ONE_TIME_TRANSFER;
		else if(JSONUtil.hasKey(response, RECURRING_TRANSFER))
            dsName = RECURRING_TRANSFER;
		else
		    dsName = JSONUtil.hasKey(response, CV_DS_NAME) ? CV_DS_NAME : DS_NAME;
		JsonArray transactions = JSONUtil.getJsonArrary(response, dsName);
		if (JSONUtil.isJsonNotNull(transactions) && transactions.size() > 0) {
			userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			accountActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountActionBusinessDelegate.class);
			applyPermission(transactions);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(response);
		}
		return true;
	}

	private void applyPermission(JsonArray transactions) {
		Iterator<JsonElement> itr = transactions.iterator();
		while (itr.hasNext()) {
			JsonObject transaction = itr.next().getAsJsonObject();
			String fromAccountNumber = JSONUtil.hasKey(transaction, FROM_ACCOUNT_NUM) ? 
					transaction.get(FROM_ACCOUNT_NUM).getAsString() : "";
			String serviceName = JSONUtil.hasKey(transaction, SERVICE_NAME) ? 
							transaction.get(SERVICE_NAME).getAsString() : "";
			String isDisputed = JSONUtil.hasKey(transaction, IS_DISPUTED) ? 
									transaction.get(IS_DISPUTED).getAsString() : "";
			if(StringUtils.isNotBlank(serviceName) &&
					!validateTransactionAction(userId, serviceName, fromAccountNumber, BooleanUtils.toBoolean(isDisputed))) {
				itr.remove();
			}
		}
	}
	
	public boolean validateTransactionAction(String userId, String serviceName, String accountId,
			boolean isDisputed) {
		
		diagnostic.prepareDebug("Permission Check for accountId "+accountId+",and serviceName :"+serviceName).log();
		
		boolean disputeViewStatus = !isDisputed || accountActionBusinessDelegate.hasUserAccountFeatureAction(
				userId, accountId, ActionConstant.DISPUTE_TRANSACTIONS_VIEW);
		
		diagnostic.prepareDebug("Dispute transaction view Permission Check for accountId "+accountId+"is :"+disputeViewStatus).log();
		
		ServiceNameConstant name ;
		
		try {
			  name = ServiceNameConstant.valueOf(serviceName);
		} catch (Exception e) {
			name = ServiceNameConstant.UNKNOWN;
			diagnostic.prepareDebug("validateTransactionAction: Caught exception for : "+serviceName).log();
		}
		
		switch(name) {
		
		case INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW);
		case INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW);
		case INTRA_BANK_FUND_TRANSFER_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.INTRA_BANK_FUND_TRANSFER_VIEW);
		case TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW);
		case BILL_PAY_BULK:
		case BILL_PAY_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.BILL_PAY_VIEW_PAYMENTS);
		case DOMESTIC_WIRE_TRANSFER_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.DOMESTIC_WIRE_TRANSFER_VIEW);
		case INTERNATIONAL_WIRE_TRANSFER_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.INTERNATIONAL_WIRE_TRANSFER_VIEW);
		case P2P_CREATE:
			return disputeViewStatus && accountActionBusinessDelegate.hasUserAccountFeatureAction(
					userId, accountId, ActionConstant.P2P_VIEW);
		default:
			return false;
		}
	}
	
}
