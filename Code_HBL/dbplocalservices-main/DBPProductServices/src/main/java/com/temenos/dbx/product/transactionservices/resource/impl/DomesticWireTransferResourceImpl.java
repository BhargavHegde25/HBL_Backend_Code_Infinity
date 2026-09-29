package com.temenos.dbx.product.transactionservices.resource.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.DomesticWireTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;
import com.temenos.dbx.product.transactionservices.resource.api.DomesticWireTransferResource;

public class DomesticWireTransferResourceImpl implements DomesticWireTransferResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	DomesticWireTransactionBusinessDelegate wireTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
	DomesticWireTransactionBackendDelegate  wireTransactionBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(DomesticWireTransactionBackendDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		
		WireTransactionDTO wireDTO = null;
		Result result = new Result();
		Double amount = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		String featureActionId = null;
		
		String amountValue = inputParams.get("amount").toString();
		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();
		
		Object payeeId = inputParams.get("payeeId");
		Object payeeAccountNumber = inputParams.get("payeeAccountNumber");
		String baseCurrency  = application.getBaseCurrencyFromCache();
		String transactionCurrency = inputParams.get("transactionCurrency") != null ?inputParams.get("transactionCurrency").toString() : baseCurrency;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
	    
		if(amountValue == null || "".equals(amountValue)) {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}
		
		if((payeeId == null || "".equals(payeeId.toString())) && (payeeAccountNumber == null || "".equals(payeeAccountNumber.toString())))  {
			return ErrorCodeEnum.ERR_12032.setErrorCode(new Result());
		}
		
		featureActionId = FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE;
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		try {
			amount = Double.parseDouble(amountValue);
		}
		catch(NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_10624.setErrorCode(new Result());
		}
		
		fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		inputParams.put("featureActionId", featureActionId);
		inputParams.put("companyId", companyId);
		inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		inputParams.put("createdby", createdby);
		inputParams.put("amount", amount);
		
		try {
			wireDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), WireTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		
		String date =  application.getServerTimeStamp();
								
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String requestid = "";

		if("true".equalsIgnoreCase(validate)) {
			WireTransactionBackendDTO wireBackendDTO = new WireTransactionBackendDTO();
			wireBackendDTO = wireBackendDTO.convert(wireDTO);
			WireTransactionDTO validatewireDTO = wireTransactionBackendDelegate.validateTransaction(wireBackendDTO,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validatewireDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for domestic wire transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		} 
		
		//these things should come from limits Engine after proper integration with validate call
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(fromAccountNumber);
		transactionStatusDTO.setAmount(amount);
		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setTransactionCurrency(transactionCurrency);
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(backendid);
		transactionStatusDTO.setServiceCharge(serviceCharge);

		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();

		wireDTO.setStatus(transactionStatus.getStatus());
		wireDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		wireDTO.setRequestId(transactionStatusDTO.getRequestId());
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		wireDTO.setConfirmationNumber(confirmationNumber);
		try {
			wireDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		wireDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		//wireDTO.setTransactionId(null);
		
		WireTransactionDTO wiredbxDTO = wireTransactionDelegate.createTransactionAtDBX(wireDTO);
		if(wiredbxDTO == null) {
			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if(wiredbxDTO.getDbpErrCode() != null || wiredbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", wiredbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", wiredbxDTO.getDbpErrMsg()));
			return result;
		}
		
		WireTransactionBackendDTO wireBackendDTO = new WireTransactionBackendDTO();
		wireBackendDTO = wireBackendDTO.convert(wiredbxDTO);
		
		try {
			wireBackendDTO.setAmount(Double.parseDouble(wireBackendDTO.getTransactionAmount()));
			wiredbxDTO.setAmount(Double.parseDouble(wireBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		String requestObj = null;
		try {
			requestObj = new JSONObject(wireBackendDTO).toString();
			result = JSONToResult.convert(requestObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for Domestic Wire transfer: ", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
		}
		
		WireTransactionDTO wiretransactionDTO = new WireTransactionDTO();
		String transactionid = wiredbxDTO.getTransactionId();
		if(transactionStatus == TransactionStatusEnum.SENT ) {
			if(StringUtils.isEmpty(backendid)) {
				wireBackendDTO.setTransactionId(null);
				wiretransactionDTO = wireTransactionBackendDelegate.createTransactionWithoutApproval(wireBackendDTO, request);					
				if(wiretransactionDTO == null) {	
					wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12601.setErrorCode(result);
				}
			}
			else {
				String frequency = StringUtils.isEmpty(wireDTO.getFrequencyType()) ? null : wireDTO.getFrequencyType();
				wiretransactionDTO = wireTransactionDelegate.approveTransaction(backendid, request, frequency);
				if(wiretransactionDTO == null) {	
					wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			if(wiretransactionDTO.getDbpErrCode() != null || wiretransactionDTO.getDbpErrMsg() != null) {
				wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, wiretransactionDTO.getDbpErrMsg());
			}	
			if(wiretransactionDTO.getReferenceId() == null || "".equals(wiretransactionDTO.getReferenceId())) {
				wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			
			wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.EXECUTED.getStatus(), wiretransactionDTO.getReferenceId());
			result.addParam(new Param("referenceId", wiretransactionDTO.getReferenceId()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId= null;
			if(StringUtils.isEmpty(backendid))
			{
				WireTransactionDTO wirependingtransactionDTO = wireTransactionDelegate.createPendingTransaction(wiredbxDTO, request);
				if(wirependingtransactionDTO == null)
				{
					wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(wirependingtransactionDTO.getDbpErrCode() != null || wirependingtransactionDTO.getDbpErrMsg() != null) {
					wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, wirependingtransactionDTO.getDbpErrMsg());
				}
				backendid = wirependingtransactionDTO.getReferenceId();
			}
				pendingrefId = backendid;
				wireTransactionDelegate.updateStatusUsingTransactionId(transactionid,transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					wireTransactionBackendDelegate.deleteTransaction(backendid, null, request);
					wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					wireTransactionBackendDelegate.deleteTransaction(backendid, null, request);
					wireTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
					result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
					return result;
				}
				transactionStatus = transactionStatusDTO.getStatus();
				backendid = transactionStatusDTO.getConfirmationNumber();
				
			
			//code snippet being added for alerts on domestic wire transfer
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null,  
						backendid, requestid, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
			}
			
			result.addParam(new Param("requestId", requestid));
			
			
			if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
				result.addParam(new Param("referenceId", backendid));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
				result.addParam(new Param("referenceId", pendingrefId));
				wireTransactionDelegate.updateStatusUsingTransactionId(transactionid,transactionStatus.toString(), pendingrefId);
			}
		}
		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		
		JSONObject bulkWireTransactionJSON = null;
		Gson gsone = new Gson();
		JsonObject requestPayload = gsone.toJsonTree(inputParams).getAsJsonObject();
		if( transactionStatus == TransactionStatusEnum.SENT || transactionStatus == TransactionStatusEnum.PENDING ) {
			bulkWireTransactionJSON = new JSONObject(wiretransactionDTO);
		}
		
		try {
			wireTransactionDelegate.auditloggingforWireTransfers(request, response, result, inputArray, bulkWireTransactionJSON, transactionStatus, amount, requestid, transactionStatusDTO.getConfirmationNumber(), requestPayload, featureActionId);
		} catch(Exception e) {
			alert.prepareError("Error occured while audit logging.",e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		
		return result;
	}
	
	@Override
	public Result updateStatus(String MethodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		
		Result result = new Result();
		WireTransactionDTO wireTransactionDTO = null;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestParams = (HashMap<String, Object>)inputArray[1];
		
		String transactionId = (String)requestParams.get("transactionId");
		String confirmationNumber = (String)requestParams.get("confirmationNumber");
		String status = (String)requestParams.get("status");
		
		if(transactionId == null || transactionId == ""){
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		if(confirmationNumber == null || confirmationNumber == "") {	
			return ErrorCodeEnum.ERR_20698.setErrorCode(result);
		}
		 if(status == null || status == "") {
			return ErrorCodeEnum.ERR_20699.setErrorCode(result);
		}
		
		try {
			// Initialize the required BusinessDelegate class to perform the operation
			DomesticWireTransactionBusinessDelegate wireTransactionDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
			wireTransactionDTO = wireTransactionDelegate.updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
			if(wireTransactionDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			JSONObject wireTransactionJSON = new JSONObject(wireTransactionDTO);
			result = JSONToResult.convert(wireTransactionJSON.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking wiretransaction business delegate",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	}

    public Result processResponseFromLineOfBusiness(Result result, DataControllerRequest request,
            DataControllerResponse response) {
    	/*
    	HashMap<String, Object>inputParams = new HashMap<String, Object>();
    	
    	String transactionId = request.getParameter("dbxtransactionId");
    	Param referenceId = result.getParamByName("referenceId");
    	TransactionStatusEnum status = null;
    	String confirmationNumber = "";
    	
    	DomesticWireTransactionBusinessDelegate wireBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
    	
    	if(referenceId != null) {
    		status = TransactionStatusEnum.EXECUTED;
    		confirmationNumber = referenceId.getValue();
    	}
    	else {
    		status = TransactionStatusEnum.FAILED;
    	}
    	inputParams.put("status", status.getStatus());
    	wireBusinessDelegate.updateStatusUsingTransactionId(transactionId, status.getStatus(), confirmationNumber);
    	
    	result.addParam(new Param("initiationId", request.getParameter("initiationId")));
    	result.addParam(new Param("dbxtransactionId", transactionId));
    	*/
    	return result;
    }
}
