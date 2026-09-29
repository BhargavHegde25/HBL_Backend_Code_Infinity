package com.temenos.dbx.object.businessdelegate.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbl.productservicesExtn.exception.CustomException;
import com.hbl.productservicesExtn.javaservice.GetPendingScheduledTrasactions;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.dbx.actions.businessdelegate.api.AccountActionBusinessDelegate;
import com.temenos.dbx.constants.ActionConstant;
import com.temenos.dbx.constants.ServiceNameConstant;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;

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
		if (JSONUtil.isJsonNotNull(transactions) /*&& transactions.size() > 0 */) {
			userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			accountActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountActionBusinessDelegate.class);
			String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
			if(PAYMENT_BACKEND.equalsIgnoreCase("STUB") && (dsName.equalsIgnoreCase("OneTimeTransfer")|| dsName.equalsIgnoreCase("StandingInstruction"))) {
				return true;
			}
			applyPermission(transactions);
			
			/* adding dbx domestic scheduled transactions 
			 * 
			 * */
			if(dsName.equalsIgnoreCase(ONE_TIME_TRANSFER) || dsName.equalsIgnoreCase(CV_DS_NAME))
			addDBXDomestcPendingScheduledTrasactions(transactions,userId, dsName);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(response);
		}
		return true;
	}

	private void applyPermission(JsonArray transactions) {
		Iterator<JsonElement> itr = transactions.iterator();
		int i=0;
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
			if(serviceName.equalsIgnoreCase("BILL_PAY_CREATE")) {
				transactions.get(i).getAsJsonObject().add("transactionComments", transaction.get("toAccountName"));
			}
			
			i++;
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
	public void addDBXDomestcPendingScheduledTrasactions(JsonArray transactions, String userId, String dsName) {
	JSONArray dbxScheduledTrans = new JSONArray();
	try {
	 dbxScheduledTrans = GetDBXDomestcPendingScheduledTrasactions(userId, dsName);
	 diagnostic.prepareDebug("GetDBXDomestcPendingScheduledTrasactions: dbxScheduledTrans"+dbxScheduledTrans).log();
	 if(dbxScheduledTrans!=null && dbxScheduledTrans.length()>0) {
	 dbxScheduledTrans=processScheduledResponse(dbxScheduledTrans);
	 }
	}catch (CustomException e) {
		diagnostic.prepareDebug("CustomException occured in GetDBXDomestcPendingScheduledTrasactions",e).log();
	}
	catch (Exception e) {
		diagnostic.prepareDebug("Exception occured in GetDBXDomestcPendingScheduledTrasactions",e).log();
	}
	JsonArray dbxScheduledTransResp = JsonParser.parseString(dbxScheduledTrans.toString()).getAsJsonArray();
	transactions.addAll(dbxScheduledTransResp);
	}
	public JSONArray GetDBXDomestcPendingScheduledTrasactions(String userId, String dsName) throws CustomException {
		List<InterBankFundTransferDTO> interbankfundtransferdto = null;
		String serviceName = "HBLOtherbankTrnsferJavaService";
		String operationName = "GetPendingScheduledTrasactions";
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("userId", userId);
		requestParams.put("isScheduled", "1");
		if(StringUtils.isNotBlank(dsName) && dsName.equalsIgnoreCase(ONE_TIME_TRANSFER)) {
		requestParams.put("filter", "ALL");
		}
		JSONArray interbankJsonArray = null;
		alert.prepareError("inputmap:" + requestParams).log();
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);

			alert.prepareError("Scheduled Interbank Json Response:" + jsonRsponse).log();
			String dbpErrCode = jsonRsponse.has("dbpErrCode") ? jsonRsponse.getString("dbpErrCode") : "";
			String dbpErrMsg = jsonRsponse.has("dbpErrMsg") ? jsonRsponse.getString("dbpErrMsg") : "";
			alert.prepareError("Scheduled Interbank Json Response has dbpErrCode:" + dbpErrCode).log();
			if (StringUtils.isNotBlank(dbpErrCode) || StringUtils.isNotBlank(dbpErrMsg)) {
				throw new CustomException(dbpErrCode, dbpErrMsg);
			} else if (jsonRsponse.has("success") && jsonRsponse.get("success").toString().equalsIgnoreCase("true")
					&& jsonRsponse.has("scheduledTransactions")) {
				interbankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			}

		} catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while GetPendingScheduledTrasactions:"+ jsonExp).log();
			throw new CustomException("20001",jsonExp.getMessage());
		} catch (CustomException exp) {
			alert.prepareError("CustomException occured while GetPendingScheduledTrasactions:"+ exp).log();
			throw new CustomException(exp.getErrorCode(),exp.getErrorMessage());
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured while GetPendingScheduledTrasactions:"+ exp).log();
			throw new CustomException("20001",exp.getMessage());
		}
		return interbankJsonArray;
	}
	public JSONArray processScheduledResponse(JSONArray dbxScheduledTrans ){
		JSONArray arrayResp= new JSONArray();
		if(dbxScheduledTrans!=null && dbxScheduledTrans.length()>0) {
		for(int i=0;i<dbxScheduledTrans.length();i++) {
			JSONObject record = dbxScheduledTrans.getJSONObject(i);
			JSONObject obj= new JSONObject();
			String status=record.optString("status");
			BigDecimal amountDecimal= new BigDecimal(record.optString("amount")).setScale(2, RoundingMode.UNNECESSARY);
			String amount= amountDecimal.toPlainString();
			obj.put("amount", amount);
			obj.put("fromAccountNumber", record.optString("fromAccountNumber"));
			obj.put("transactionCurrency",record.optString("transactionCurrency"));
			obj.put("fromAccountName", record.optString("fromAccountName"));
			obj.put("currentStatus",status);
			obj.put("description", record.optString("notes"));
			obj.put("scheduledDate",record.optString("scheduledDate"));
			obj.put("fromAccountIBAN", "");
			obj.put("transactionId",record.optString("transactionId"));
			obj.put("referenceId",record.optString("requestId"));
			obj.put("transactionType", record.optString("transactionType"));
			obj.put("paymentCurrencyId", record.optString("transactionCurrency"));
			obj.put("toAccountName", record.optString("beneficiaryName"));
			obj.put("fromAccountCurrency", record.optString("fromAccountCurrency"));
			obj.put("orderingCustomerId", record.optString("companyId").split("_").length>0?record.optString("companyId").split("_")[1]:record.optString("companyId"));
			obj.put("toAccountNumber", record.optString("toAccountNumber"));
			obj.put("frequencyType", record.optString("frequencyTypeId"));
			obj.put("isScheduled", record.optString("isScheduled")!=null&& record.optString("isScheduled").equals("1")?"true":"false");
			obj.put("paymentType", record.optString("paymentType"));
			obj.put("paidby", record.optString("paidBy"));
			
			String toAccountName = record.optString("beneficiaryName");
        	String toAccountNumber = record.optString("toAccountNumber");
        	if(StringUtils.isNotBlank(toAccountName) && StringUtils.isNotBlank(toAccountNumber)) {
        		if(toAccountNumber.length() > 5) {
        		obj.put("transactionComments", toAccountName +"..."+toAccountNumber.substring(toAccountNumber.length() - 4));
        		}
        	}
        	else {
        		obj.put("transactionComments", "...");
        	}
			String isScheduled=record.optString("isScheduled");
			String statusDescription=status;
			if(StringUtils.isNotBlank(record.optString("isScheduled")) && isScheduled.equals("1")) {
					if(status.equalsIgnoreCase("Executed") ) {
						statusDescription="Completed";
					}
			}
			obj.put("statusDescription",statusDescription);
			obj.put("serviceName",record.optString("featureActionId"));
			obj.put("structuredIssuer", record.optString("featureActionId"));
			obj.put("transactionAmount", record.optString("transactionAmount"));
			if(record.optString("serviceCharge")!=null) {
			JSONArray charges = new JSONArray();
			JSONObject chargeObj = new JSONObject();
			chargeObj.put("chargeCurrency", record.optString("feeCurrency"));
			chargeObj.put("chargeAmount", record.optString("serviceCharge"));
			charges.put(chargeObj);
			obj.put("charges",charges);
			}
			arrayResp.put(obj);
		}
	}
		diagnostic.prepareDebug("returning processScheduledResponse:"+arrayResp).log();
		return arrayResp;
	}
	
}
