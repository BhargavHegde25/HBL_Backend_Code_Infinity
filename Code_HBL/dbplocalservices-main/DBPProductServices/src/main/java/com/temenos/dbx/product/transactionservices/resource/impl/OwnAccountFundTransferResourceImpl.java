package com.temenos.dbx.product.transactionservices.resource.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.dbx.product.commons.backenddelegate.api.TransactionLimitsBackendDelegate;
import com.temenos.dbx.product.commons.backenddelegate.impl.TransactionLimitsBackendDelegateImpl;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.OwnAccountFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.OwnAccountFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferDTO;
import com.temenos.dbx.product.transactionservices.resource.api.OwnAccountFundTransferResource;

public class OwnAccountFundTransferResourceImpl implements OwnAccountFundTransferResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	OwnAccountFundTransferBackendDelegate ownAccFundTransferBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(OwnAccountFundTransferBackendDelegate.class);

    
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		OwnAccountFundTransferDTO ownaccountDTO = null;
		Result result = new Result();
		Double amount = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);

		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();


		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		if(StringUtils.isEmpty(legalEntityId))
			legalEntityId=(String) customer.get("legalEntityId");

		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if(PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
			if("true".equalsIgnoreCase(validate)) {
				return stubValidateResponse(inputParams);
			}
			else {
				return stubCommitResponse(inputParams);
			}
		}
		
		if(CustomerSession.IsAPIUser(customer)) {
			createdby = inputParams.get("customerId") == null ? null : inputParams.get("customerId").toString();
			if(createdby == null || createdby.isEmpty()) {
				return ErrorCodeEnum.ERR_12052.setErrorCode(new Result());
			}
		}
		
		String featureActionId = null;
		String amountValue = inputParams.get("amount").toString();
		String toAccountNumber = inputParams.get("toAccountNumber").toString();
		String frequencyType = inputParams.get("frequencyType") == null ? null : inputParams.get("frequencyType").toString();


		String baseCurrency;
		try {
			baseCurrency= LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
			if(StringUtils.isEmpty(baseCurrency)){
				baseCurrency  = application.getBaseCurrencyFromCache();
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching base currency from Legal Entity"+e).log();
			return  ErrorCodeEnum.ERR_27001.setErrorCode(new Result());
		}

		String transactionCurrency = inputParams.get("transactionCurrency") == null ? baseCurrency : inputParams.get("transactionCurrency").toString();
		String serviceCharge = inputParams.get("serviceCharge") == null ? "0.0" : inputParams.get("serviceCharge").toString();


		if(amountValue == null || amountValue == "") {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}
		
		if( (fromAccountNumber != null && fromAccountNumber != "") &&
				(toAccountNumber != null && toAccountNumber != "") &&
				fromAccountNumber.equals(toAccountNumber) )
		{
			return ErrorCodeEnum.ERR_12307.setErrorCode(new Result());
		}
		
		featureActionId = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE;
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		if(!CustomerSession.IsAPIUser(customer) && ! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
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
		
		try {
			ownaccountDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), OwnAccountFundTransferDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());			
		}
		
		String date = ownaccountDTO.getScheduledDate() == null ? 
				(ownaccountDTO.getProcessingDate() == null ? 
						(ownaccountDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: ownaccountDTO.getFrequencyStartDate())
						: ownaccountDTO.getProcessingDate()) 
				: ownaccountDTO.getScheduledDate();
								
		String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String beneficiaryId = inputParams.get("beneficiaryId") == null ? null : inputParams.get("beneficiaryId").toString();
		String requestid = "";
	
		if("true".equalsIgnoreCase(validate)) {
			OwnAccountFundTransferBackendDTO backendObj = new OwnAccountFundTransferBackendDTO();
			backendObj =  backendObj.convert(ownaccountDTO);
			OwnAccountFundTransferDTO validateownaccountDTO = ownAccFundTransferBackendDelegate.validateTransaction(backendObj,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validateownaccountDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		}
		
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(fromAccountNumber);
		transactionStatusDTO.setAmount(amount);
		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setTransactionCurrency(transactionCurrency);
		transactionStatusDTO.setServiceCharge(serviceCharge);
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(backendid);
		
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		
		ownaccountDTO.setStatus(transactionStatus.getStatus());

		try {
			if(transactionCurrency!=baseCurrency)
				ownaccountDTO.setAmount(_getConvertedAmount(Double.parseDouble(amountValue),transactionCurrency,baseCurrency,request));
			else
				ownaccountDTO.setAmount(amount);
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		ownaccountDTO.setRequestId(transactionStatusDTO.getRequestId());
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		ownaccountDTO.setConfirmationNumber(confirmationNumber);
		ownaccountDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		ownaccountDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		ownaccountDTO.setLegalEntityId(legalEntityId);
		//ownaccountDTO.setTransactionId(null);
		
		OwnAccountFundTransferDTO ownaccountdbxDTO = ownaccountTransactionDelegate.createTransactionAtDBX(ownaccountDTO);				
		if(ownaccountdbxDTO == null) {
			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if(ownaccountdbxDTO.getDbpErrCode() != null || ownaccountdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", ownaccountdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", ownaccountdbxDTO.getDbpErrMsg()));
			return result;
		}
		ownaccountdbxDTO.setValidate(validate);
		
		OwnAccountFundTransferBackendDTO ownaccountBackendDTO = new OwnAccountFundTransferBackendDTO();
		ownaccountBackendDTO = ownaccountBackendDTO.convert(ownaccountdbxDTO);
					
		String creditValueDate = inputParams.get("creditValueDate") == null ? "" : inputParams.get("creditValueDate").toString();
        String totalAmount = inputParams.get("totalAmount") == null ? "" : inputParams.get("totalAmount").toString(); 
        String exchangeRate = inputParams.get("exchangeRate") == null ? "" : inputParams.get("exchangeRate").toString();
        String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
        ownaccountBackendDTO.setCreditValueDate(creditValueDate);
        ownaccountBackendDTO.setTotalAmount(totalAmount);
        ownaccountBackendDTO.setExchangeRate(exchangeRate);
        ownaccountBackendDTO.setOverrides(overrides);
        
		ownaccountBackendDTO.setBeneficiaryId(beneficiaryId);
		
		try {
			String responseObj = new JSONObject(ownaccountBackendDTO).toString();
			result = JSONToResult.convert(responseObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
		}
		OwnAccountFundTransferDTO ownaccounttransactionDTO = new OwnAccountFundTransferDTO();
		String transactionid = ownaccountdbxDTO.getTransactionId();
        String createWithPaymentId = inputParams.get("createWithPaymentId") == null ? ""
                : inputParams.get("createWithPaymentId").toString();

        try {
        	ownaccountBackendDTO.setAmount(Double.parseDouble(ownaccountBackendDTO.getTransactionAmount()));
        	ownaccountdbxDTO.setAmount(Double.parseDouble(ownaccountBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
        
        if (transactionStatus == TransactionStatusEnum.SENT) {
            if (StringUtils.isEmpty(backendid)
                    || (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true"))) {
                if(StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true")){
                    ownaccountBackendDTO.setTransactionId(backendid);
                    String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
                    ownaccountBackendDTO.setCharges(charges);
                } else {
                    ownaccountBackendDTO.setTransactionId(null);
                }
				ownaccounttransactionDTO = ownAccFundTransferBackendDelegate.createTransactionWithoutApproval(ownaccountBackendDTO, request);					
				if(ownaccounttransactionDTO == null) {	
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12600.setErrorCode(result);
				}
			}
			else {
				String frequency = StringUtils.isEmpty(ownaccountDTO.getFrequencyTypeId()) ? null : ownaccountDTO.getFrequencyTypeId();
				ownaccounttransactionDTO  = ownaccountTransactionDelegate.approveTransaction(backendid, request, frequency);
				if(ownaccounttransactionDTO == null) {	
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			if(ownaccounttransactionDTO.getDbpErrCode() != null || ownaccounttransactionDTO.getDbpErrMsg() != null) {
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				result.addParam(new Param("errorDetails", ownaccounttransactionDTO.getErrorDetails()));
                return ErrorCodeEnum.ERR_00000.setErrorCode(result, ownaccounttransactionDTO.getDbpErrMsg());
			}
			if(ownaccounttransactionDTO.getReferenceId() == null || "".equals(ownaccounttransactionDTO.getReferenceId())) {
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			ownaccountDTO=ownaccounttransactionDTO;
			ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.EXECUTED.getStatus(), ownaccounttransactionDTO.getReferenceId());
			result.addParam(new Param("referenceId", ownaccounttransactionDTO.getReferenceId()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			ownaccountdbxDTO.setCreditValueDate(creditValueDate);
			ownaccountdbxDTO.setTotalAmount(totalAmount);
			ownaccountdbxDTO.setExchangeRate(exchangeRate);
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId= null;
			if (StringUtils.isEmpty(backendid)
                    || (StringUtils.isNotBlank(backendid)  && createWithPaymentId.equalsIgnoreCase("true"))) {
			    if(StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true") ){
			        ownaccountdbxDTO.setTransactionId(backendid);
			        String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
			        ownaccountdbxDTO.setCharges(charges);
                }
				OwnAccountFundTransferDTO ownaccountpendingtransactionDTO = ownaccountTransactionDelegate.createPendingTransaction(ownaccountdbxDTO, request);
				if(ownaccountpendingtransactionDTO == null)
				{
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(ownaccountpendingtransactionDTO.getDbpErrCode() != null || ownaccountpendingtransactionDTO.getDbpErrMsg() != null) {
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					result.addParam(new Param("errorDetails", ownaccountpendingtransactionDTO.getErrorDetails()));
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, ownaccountpendingtransactionDTO.getDbpErrMsg());
				}
				backendid = ownaccountpendingtransactionDTO.getReferenceId();
				ownaccountDTO=ownaccountpendingtransactionDTO;
			}
				pendingrefId = backendid;
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid,transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					ownAccFundTransferBackendDelegate.deleteTransactionWithoutApproval(backendid, null, frequencyType, request);
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					ownAccFundTransferBackendDelegate.deleteTransactionWithoutApproval(backendid, null, frequencyType, request);
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
					result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
					return result;
				}
				
				transactionStatus = transactionStatusDTO.getStatus();
				backendid = transactionStatusDTO.getConfirmationNumber();
				
			
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
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionid,transactionStatus.toString(), pendingrefId);
			}
			
			//code snippet being added for alerts on Own account  transfer
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null,  
						backendid, requestid, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
			}

		}
		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		
		if(ownaccountDTO.getOverrides() != null) {
			result.addParam(new Param("overrides",ownaccountDTO.getOverrides()));
		}
		if(ownaccountDTO.getOverrideList() != null) {
			result.addParam(new Param("overrideList",ownaccountDTO.getOverrideList()));
		}
		if(ownaccountDTO.getCharges() != null) {
			result.addParam(new Param("charges",ownaccountDTO.getCharges()));
		}
		if(ownaccountDTO.getExchangeRate() != null) {
			result.addParam(new Param("exchangeRate",ownaccountDTO.getExchangeRate()));
		}
		if(ownaccountDTO.getTotalAmount() != null) {
			result.addParam(new Param("totalAmount",ownaccountDTO.getTotalAmount()));
		}
		
		if(ownaccountDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", ownaccountDTO.getMessageDetails()));
        }
		
		if(ownaccountDTO.getQuoteCurrency() != null) {
            result.addParam(new Param("quoteCurrency", ownaccountDTO.getQuoteCurrency()));
        }
		
		try {
			_logTransaction(request,response,inputArray,result,transactionStatus,transactionStatusDTO.getConfirmationNumber(),ownaccountdbxDTO,requestid);
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
	private double _getConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request) {
		TransactionLimitsBackendDelegate transactionLimitsBackendDelegate =  DBPAPIAbstractFactoryImpl.getBackendDelegate(TransactionLimitsBackendDelegate.class);
		return transactionLimitsBackendDelegate.fetchNewConvertedAmount(amount,transactionCurrency,baseCurrency,request);
	}
	
	@Override
	public Result updateStatus(String MethodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		
		Result result = new Result();
		OwnAccountFundTransferDTO ownaccountTransactionDTO = null;
		
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
			OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
			ownaccountTransactionDTO = ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
			if(ownaccountTransactionDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			JSONObject ownaccountTransactionJSON = new JSONObject(ownaccountTransactionDTO);
			result = JSONToResult.convert(ownaccountTransactionJSON.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking ownaccounttransaction business delegate",exp).log();
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
    	
    	OwnAccountFundTransferBusinessDelegate ownaccountBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
    	
    	if(referenceId != null) {
    		status = TransactionStatusEnum.EXECUTED;
    		confirmationNumber = referenceId.getValue();
    	}
    	else {
    		status = TransactionStatusEnum.FAILED;
    	}
    	inputParams.put("status", status.getStatus());
    	ownaccountBusinessDelegate.updateStatusUsingTransactionId(transactionId, status.getStatus(), confirmationNumber);
    	
    	result.addParam(new Param("dbxtransactionId", transactionId));
    	*/
    	return result;
    }
    
    @Override
	public Result editTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
    	
		OwnAccountFundTransferBackendDelegate ownAccFundTransferBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(OwnAccountFundTransferBackendDelegate.class);
		OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		
		String featureActionId = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE;
		OwnAccountFundTransferDTO ownaccountDTO = null;
		Result result = new Result();
		String confirmationNumber = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isBusinessUser = CustomerSession.IsBusinessUser(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		Object transactionIdParam = inputParams.get("confirmationNumber");
		Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");
		
		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();
		
		if(confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
			confirmationNumber = confirmationNumberParam.toString();
			ownaccountDTO = ownaccountTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", Arrays.asList(companyId), createdby , legalEntityId);
			if(ownaccountDTO == null) {
					ownaccountDTO = ownAccFundTransferBackendDelegate.fetchTransactionById(confirmationNumber, request);
                if (ownaccountDTO == null) {
                    alert.prepareError("Record doesn't exist").log();
                    return ErrorCodeEnum.ERR_12003.setErrorCode(result);
                } else {
                    ownaccountDTO.setCompanyId(companyId);
                    ownaccountDTO.setCreatedby(createdby);
                    ownaccountDTO.setFeatureActionId(featureActionId);
                    if (StringUtils.isNotBlank(inputParams.get("amount").toString())) {
                        ownaccountDTO.setAmount(Double.parseDouble(inputParams.get("amount").toString()));
                        ownaccountDTO.setTransactionAmount(inputParams.get("amount").toString());
                    }
                    ownaccountDTO = ownaccountTransactionDelegate.createTransactionAtDBX(ownaccountDTO);
                }
			}
		}
		else {
			alert.prepareError("confirmationNumber is missing in the payload which is mandatory for edit").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
		}
		
		String transactionId = ownaccountDTO.getTransactionId();
		String oldRequestId = ownaccountDTO.getRequestId();
		
		//Authorization checks on transcationId
		/*
		if(ownaccountDTO.getCompanyId() != null && ! ownaccountDTO.getCompanyId().equals(companyId)) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isBusinessUser && ! CustomerSession.IsCombinedUser(customer) && ! ownaccountDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		*/
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = ownaccountDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		OwnAccountFundTransferDTO requestDTO = null;
		inputParams.put("createdby", createdby);
		inputParams.put("companyId", companyId);
		inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		
		try {
			requestDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), OwnAccountFundTransferDTO.class);
			requestDTO.setConfirmationNumber(null);
			requestDTO.setTransactionId(null);
			ownaccountDTO = ownaccountDTO.updateValues(requestDTO);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(ownaccountDTO == null) {
			alert.prepareError("Error occured while fetching the input params: " ).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		//Authorization checks on newfromAccountNumber and featureactionId
		String newfromAccountNumber = ownaccountDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, newfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		String date = ownaccountDTO.getScheduledDate() == null ? 
				(ownaccountDTO.getProcessingDate() == null ? 
						(ownaccountDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: ownaccountDTO.getFrequencyStartDate())
						: ownaccountDTO.getProcessingDate()) 
				: ownaccountDTO.getScheduledDate();
		
		//Transaction limit checks on amount and featureactionId
		Double amount = ownaccountDTO.getAmount();
		String transactionAmount = inputParams.get("transactionAmount") != null ? inputParams.get("transactionAmount").toString() : null;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
	    
	    if("0".equals(transactionAmount) || "0.0".equals(transactionAmount) || "0.00".equals(transactionAmount)) {
			transactionAmount = amount + "";
		}
	    
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();

		if("true".equalsIgnoreCase(validate)) {
			OwnAccountFundTransferBackendDTO backendObj = new OwnAccountFundTransferBackendDTO();
			backendObj =  backendObj.convert(ownaccountDTO);
			OwnAccountFundTransferDTO validateownaccountDTO = ownAccFundTransferBackendDelegate.validateTransaction(backendObj,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validateownaccountDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		}
		
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(fromAccountNumber);
		transactionStatusDTO.setAmount(amount);
		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setTransactionCurrency(ownaccountDTO.getTransactionCurrency());
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setRequestId(oldRequestId);
		transactionStatusDTO.setServiceCharge(serviceCharge);
		
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);
		
		if(transactionStatusDTO == null)
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		
		TransactionStatusEnum transactionStatus =transactionStatusDTO.getStatus();
		ownaccountDTO.setStatus(transactionStatus.getStatus());
		//ownaccountDTO.setRequestId(null);
		ownaccountDTO.setConfirmationNumber(confirmationNumber);
		ownaccountDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		try {
			ownaccountDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		ownaccountDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());

		OwnAccountFundTransferBackendDTO ownaccountBackendDTO = new OwnAccountFundTransferBackendDTO();
		ownaccountBackendDTO = ownaccountBackendDTO.convert(ownaccountDTO);
		
		String requestObj = null;
		try {
			requestObj = new JSONObject(ownaccountBackendDTO).toString();
			result = JSONToResult.convert(requestObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		String referenceId = null;
		OwnAccountFundTransferDTO resDTO = null;
		
		try {
			ownaccountBackendDTO.setAmount(Double.parseDouble(ownaccountBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
        ownaccountBackendDTO.setOverrides(overrides);
		
		if (transactionStatus == TransactionStatusEnum.SENT) {

				if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
					ownaccountBackendDTO.setTransactionId(confirmationNumber);
					resDTO = ownAccFundTransferBackendDelegate.editTransactionWithoutApproval(ownaccountBackendDTO, request);
				}
				
				if(resDTO == null) {
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12600.setErrorCode(result);
				}
				
				if(resDTO.getDbpErrMsg() != null && !resDTO.getDbpErrMsg().isEmpty()) {
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					result.addParam(new Param("errorDetails", resDTO.getErrorDetails()));
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, resDTO.getDbpErrMsg());
				}
				
				referenceId = resDTO.getReferenceId();

				if(StringUtils.isEmpty(referenceId)) {
					ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Failed to edit the transaction at backend").log();
					return ErrorCodeEnum.ERR_12602.setErrorCode(new Result());
				}
				
				ownaccountDTO = resDTO;
				ownaccountDTO.setConfirmationNumber(referenceId);
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), ownaccountDTO.getReferenceId());
				
				result.addParam(new Param("referenceId", resDTO.getReferenceId()));
				result.addParam(new Param("status", transactionStatus.getStatus()));
		        result.addParam(new Param("message", transactionStatus.getMessage()));
				
		}else if(transactionStatus == TransactionStatusEnum.PENDING){
				
			String requestId = transactionStatusDTO.getRequestId();
    		referenceId = confirmationNumber;
    		ownaccountBackendDTO.setTransactionId(confirmationNumber);
    		OwnAccountFundTransferDTO pendingtransactionDTO = ownAccFundTransferBackendDelegate.editTransactionWithApproval(ownaccountBackendDTO, request);
    		
    		if(pendingtransactionDTO == null)
    		{
    			ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			alert.prepareError("Error occured while creating entry into the backend table: ").log();
    			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
    		}
    		
    		if(pendingtransactionDTO.getDbpErrCode() != null || pendingtransactionDTO.getDbpErrMsg() != null) {
    			ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			result.addParam(new Param("errorDetails", pendingtransactionDTO.getErrorDetails()));
    			return ErrorCodeEnum.ERR_00000.setErrorCode(result, pendingtransactionDTO.getDbpErrMsg());
    		}
    		
    		String backendid = pendingtransactionDTO.getReferenceId();
    		ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
            transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, backendid, transactionStatusDTO.isSelfApproved(), featureActionId, request);
            
            if(transactionStatusDTO == null) 
			{							
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
				return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
			}	
			if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
            transactionStatus=transactionStatusDTO.getStatus();
            ownaccountTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
            ownaccountDTO.setConfirmationNumber(backendid);
            ownaccountDTO.setStatus(transactionStatus.toString());
            ownaccountDTO.setRequestId(requestId);
    		result.addParam(new Param("requestId", requestId));
    		result.addParam(new Param("referenceId", backendid));

    		if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
			}
    		
		}else if(transactionStatus == TransactionStatusEnum.APPROVED){
    		result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
    		result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
    		result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
    	}
		
		if(ownaccountDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", ownaccountDTO.getMessageDetails()));
        }
		
		if(ownaccountDTO.getQuoteCurrency() != null) {
            result.addParam(new Param("quoteCurrency", ownaccountDTO.getQuoteCurrency()));
        }
		
		//delete the old transaction entry from DBX
        if(StringUtils.isNotEmpty(referenceId)) {
        	ownaccountDTO.setTransactionId(transactionId);
        	ownaccountTransactionDelegate.updateTransactionAtDBX(ownaccountDTO);
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
	public Result cancelScheduledTransactionOccurrence(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		String featureActionId = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL;
		String requestId = "";
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		OwnAccountFundTransferDTO ownaccountDTO = null;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Object transactionIdParam = inputParams.get("confirmationNumber");
		Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");
		String confirmationNumber = null;
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(createdby);
		contracts.add(CustomerSession.getCompanyId(customer));
		
		if(confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
			confirmationNumber = confirmationNumberParam.toString();
			ownaccountDTO = ownaccountTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber, contracts, createdby , null);
			if(ownaccountDTO == null) {
				ownaccountDTO = ownAccFundTransferBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(ownaccountDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
				ownaccountDTO.setFeatureActionId(FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE);
				ownaccountDTO.setCreatedby(createdby);
				CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, ownaccountDTO.getFromAccountNumber());
				String companyId = account.getOrganizationId();
				ownaccountDTO.setCompanyId(companyId);
				String transactionId = ownaccountDTO.getTransactionId();
				ownaccountDTO.setConfirmationNumber(transactionId);
				ownaccountDTO.setTransactionId(null);
				OwnAccountFundTransferDTO dbxDTO = ownaccountTransactionDelegate.createTransactionAtDBX(ownaccountDTO);
				if(dbxDTO != null) {
					ownaccountDTO.setTransactionId(dbxDTO.getTransactionId());
					ownaccountDTO.setRequestId(dbxDTO.getRequestId());
				}
			}
		}
		else {
			alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for cancel occurrence").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		
		String oldrequestId = ownaccountDTO.getRequestId();
		String transactionId = ownaccountDTO.getTransactionId();
		
		//Authorization checks on transcationId
		/*
		if(ownaccountDTO.getCompanyId() != null && ! contracts.contains(ownaccountDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! ownaccountDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = ownaccountDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(ownaccountDTO.getCompanyId());
		transactionStatusDTO.setAccountId(ownaccountDTO.getFromAccountNumber());
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setRequestId(oldrequestId);
		transactionStatusDTO.setAmount(ownaccountDTO.getAmount());
		transactionStatusDTO.setServiceCharge(ownaccountDTO.getServiceCharge());
		transactionStatusDTO.setTransactionCurrency(ownaccountDTO.getTransactionCurrency());
		
		
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		
		if (transactionStatus == TransactionStatusEnum.SENT) {
			OwnAccountFundTransferDTO ownaccounttransactionDTO = new OwnAccountFundTransferDTO();
			ownaccounttransactionDTO = ownAccFundTransferBackendDelegate.cancelTransactionWithoutApproval(confirmationNumber, request);					
			if(ownaccounttransactionDTO == null) {	
				return ErrorCodeEnum.ERR_12600.setErrorCode(result);
			}
			if(ownaccounttransactionDTO.getDbpErrCode() != null || ownaccounttransactionDTO.getDbpErrMsg() != null) {
                return ErrorCodeEnum.ERR_00000.setErrorCode(result, ownaccounttransactionDTO.getDbpErrMsg());
			}
			String referenceId = ownaccounttransactionDTO.getReferenceId();
			if(referenceId == null || "".equals(referenceId)) {
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			result.addParam(new Param("referenceId", referenceId));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestId = transactionStatusDTO.getRequestId();
			OwnAccountFundTransferDTO ownaccountpendingtransactionDTO = new OwnAccountFundTransferDTO();
			ownaccountpendingtransactionDTO = ownaccountTransactionDelegate.cancelTransactionWithApproval(confirmationNumber, transactionId, request);					
			if(ownaccountpendingtransactionDTO == null)
			{
				alert.prepareError("Error occured while creating entry into the backend table: ").log();
				return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
			}
			if(ownaccountpendingtransactionDTO.getDbpErrCode() != null || ownaccountpendingtransactionDTO.getDbpErrMsg() != null) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, ownaccountpendingtransactionDTO.getDbpErrMsg());
			}
			confirmationNumber = ownaccountpendingtransactionDTO.getReferenceId();
			transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, confirmationNumber, isSelfApproved, featureActionId, request);
			if(transactionStatusDTO == null) 
			{							
				return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
			}	
			if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
			transactionStatus = transactionStatusDTO.getStatus();
			confirmationNumber = transactionStatusDTO.getConfirmationNumber();
			ownaccountTransactionDelegate.updateRequestId(transactionId, requestId);
			result.addParam(new Param("requestId", requestId));
			result.addParam(new Param("referenceId", confirmationNumber));
	        if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
			}
		}
		
		result.addParam(new Param("transactionId", confirmationNumber+""));
		try {
			LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null,confirmationNumber, requestId, CustomerSession.getCustomerName(customer),null);
		} catch (Exception e) {
			alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
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
	public Result deleteTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		String featureActionId = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String requestId = "";
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		OwnAccountFundTransferDTO ownaccountDTO = null;
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if(PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
				return stubDeleteTransactionResponse();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
        String legalEntityId = (String) customer.get("legalEntityId");
		Object transactionIdParam = inputParams.get("confirmationNumber");
		Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");
		String frequencyType = inputParams.get("frequencyType") == null ? null : inputParams.get("frequencyType").toString();
		String transactionId = null;
		String confirmationNumber = null;
		String transactionType = null;
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(createdby);
		contracts.add(CustomerSession.getCompanyId(customer));
		
		if(confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
			confirmationNumber = confirmationNumberParam.toString();
			ownaccountDTO = ownaccountTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber, contracts, createdby , legalEntityId);
			if(ownaccountDTO == null) {
				ownaccountDTO = ownAccFundTransferBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(ownaccountDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
				ownaccountDTO.setFeatureActionId(FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE);
				ownaccountDTO.setCreatedby(createdby);
				CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, ownaccountDTO.getFromAccountNumber());
				String companyId = account.getOrganizationId();
				ownaccountDTO.setCompanyId(companyId);
				transactionId = ownaccountDTO.getTransactionId();
				ownaccountDTO.setConfirmationNumber(transactionId);
				ownaccountDTO.setTransactionId(null);
				OwnAccountFundTransferDTO dbxDTO = ownaccountTransactionDelegate.createTransactionAtDBX(ownaccountDTO);
				if(dbxDTO != null) {
					ownaccountDTO.setTransactionId(dbxDTO.getTransactionId());
					ownaccountDTO.setRequestId(dbxDTO.getRequestId());
				}
			}
		}
		else {
			alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for delete").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		
		transactionId = ownaccountDTO.getTransactionId();
		transactionType = ownaccountDTO.getTransactionType();
		String oldrequestId = ownaccountDTO.getRequestId();
		
		//Authorization checks on transcationId
		/*
		if(ownaccountDTO.getCompanyId() != null && ! contracts.contains(ownaccountDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! ownaccountDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = ownaccountDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(ownaccountDTO.getCompanyId());
		transactionStatusDTO.setAccountId(ownaccountDTO.getFromAccountNumber());
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setRequestId(oldrequestId);
		transactionStatusDTO.setAmount(ownaccountDTO.getAmount());
		transactionStatusDTO.setServiceCharge(ownaccountDTO.getServiceCharge());
		transactionStatusDTO.setTransactionCurrency(ownaccountDTO.getTransactionCurrency());
		
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		
		if (transactionStatus == TransactionStatusEnum.SENT) {
			OwnAccountFundTransferDTO ownaccounttransactionDTO = new OwnAccountFundTransferDTO();
			ownaccounttransactionDTO = ownAccFundTransferBackendDelegate.deleteTransactionWithoutApproval(confirmationNumber, transactionType, frequencyType, request);					
			if(ownaccounttransactionDTO == null) {	
				return ErrorCodeEnum.ERR_12600.setErrorCode(result);
			}
			if(ownaccounttransactionDTO.getDbpErrCode() != null || ownaccounttransactionDTO.getDbpErrMsg() != null) {
                return ErrorCodeEnum.ERR_00000.setErrorCode(result, ownaccounttransactionDTO.getDbpErrMsg());
			}
			String referenceId = ownaccounttransactionDTO.getReferenceId();
			if(referenceId == null || "".equals(referenceId)) {
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			ownaccountTransactionDelegate.deleteTransactionAtDBX(transactionId);
			result.addParam(new Param("referenceId", referenceId));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestId = transactionStatusDTO.getRequestId();
			OwnAccountFundTransferDTO ownaccountpendingtransactionDTO = new OwnAccountFundTransferDTO();
			ownaccountpendingtransactionDTO = ownaccountTransactionDelegate.deleteTransactionWithApproval(confirmationNumber, transactionType, frequencyType, transactionId, request);					
			if(ownaccountpendingtransactionDTO == null)
			{
				alert.prepareError("Error occured while creating entry into the backend table: ").log();
				return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
			}
			if(ownaccountpendingtransactionDTO.getDbpErrCode() != null || ownaccountpendingtransactionDTO.getDbpErrMsg() != null) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, ownaccountpendingtransactionDTO.getDbpErrMsg());
			}
			confirmationNumber = ownaccountpendingtransactionDTO.getReferenceId();
			transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, confirmationNumber, isSelfApproved, featureActionId, request);
			if(transactionStatusDTO == null) 
			{							
				return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
			}	
			if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
			transactionStatus = transactionStatusDTO.getStatus();
			confirmationNumber = transactionStatusDTO.getConfirmationNumber();
			ownaccountTransactionDelegate.updateRequestId(transactionId, requestId);
			result.addParam(new Param("requestId", requestId));
			result.addParam(new Param("referenceId", confirmationNumber));
	        if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
			}
		}
		 
		result.addParam(new Param("transactionId", confirmationNumber+""));
		inputParams.put("amount", ownaccountDTO.getAmount());
		inputParams.put("scheduledDate", ownaccountDTO.getScheduledDate());
		inputParams.put("fromAccountNumber", ownaccountDTO.getFromAccountNumber());
		inputParams.put("toAccountNumber", ownaccountDTO.getToAccountNumber());
		inputParams.put("isScheduled", ownaccountDTO.getIsScheduled());
		inputParams.put("Frequency", ownaccountDTO.getFrequencyTypeId());
		inputParams.put("NoofRecurrences", ownaccountDTO.getNumberOfRecurrences());
		inputParams.put("TransferDate", ownaccountDTO.getScheduledDate());
		inputParams.put("EndBy", ownaccountDTO.getFrequencyEndDate());
		
		try {
			LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null, confirmationNumber, requestId, CustomerSession.getCustomerName(customer),null);
		} catch (Exception e) {
			alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		return result;
	}
	
	/**
	 * Logs Own Account Transfer status in auditactivity
	 * @param request
	 * @param response
	 * @param result
	 * @param transactionStatus
	 * @param ownaccountDTO
	 * @param referenceId
	 * @param requestId
	 */
	private void _logTransaction(DataControllerRequest request,DataControllerResponse response,Object[] inputArray,Result result, TransactionStatusEnum transactionStatus, String referenceId,OwnAccountFundTransferDTO ownaccountDTO,String requestId) {
		
		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE)) return;
		try {
			ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApproversBusinessDelegate.class);
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);

			AuditLog auditLog = new AuditLog();

			String eventType = Constants.MAKE_TRANSFER;
			String eventSubType = "";
			String producer = "Transactions/POST(createTransfer)";
			String statusID = "";
			String frequencyType = result.getParamValueByName(Constants.FREQUENCYTYPE);
			String isScheduled = result.getParamValueByName(Constants.ISSCHEDULED);
			boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
			String fromAccountNumber = "";
			String toAccountNumber = "";
			
			if (request.containsKeyInRequest("validate")) {
                String validate = request.getParameter("validate");
                if(StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase("true"))
                    return;
            }

			if (request.containsKeyInRequest("fromAccountNumber")) {
				fromAccountNumber = request.getParameter("fromAccountNumber");
			}
			if (request.containsKeyInRequest("toAccountNumber")) {
				toAccountNumber = request.getParameter("toAccountNumber");
			}
			;

			JsonObject customParams = new JsonObject();
			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);

			eventSubType = auditLog.deriveSubTypeForInternalTransfer(isScheduled, frequencyType);
			List<Param> params = result.getAllParams();
			for (Param param : params) {
				if (request.containsKeyInRequest(param.getName())) {
					continue;
				} else {
					customParams.addProperty(param.getName(), param.getValue());
				}
			}
			if (transactionStatus.toString().contains("DENIED")) {
				statusID = Constants.SID_EVENT_FAILURE;
				customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
			} else {
				switch (transactionStatus) {
				case SENT:
					referenceId = customParams.get("referenceId").toString();
					if (ownaccountDTO.getDbpErrMsg() != null && !ownaccountDTO.getDbpErrMsg().isEmpty()) {
						statusID = Constants.SID_EVENT_FAILURE;
					}
					if (referenceId == null || "".equals(referenceId)) {
						statusID = Constants.SID_EVENT_FAILURE;
					} else {
						statusID = Constants.SID_EVENT_SUCCESS;
						customParams.addProperty(Constants.REFERENCEID, referenceId);
						if (isSMEUser) {
							customParams.addProperty(Constants.APPROVERS, "Pre-Approved");
							customParams.addProperty("approvedBy", "Pre-Approved");
						}
					}
					break;
				case PENDING:
					statusID = Constants.SID_EVENT_SUCCESS;
					customParams.addProperty(Constants.REFERENCEID, referenceId);
					eventSubType = Constants.PENDING_APPROVAL_ + eventSubType;
					List<String> approvers = approversBusinessDelegate.getRequestApproversList(requestId);
					if (approvers == null) {
						customParams.addProperty(Constants.APPROVERS, "");
					} else {
						customParams.addProperty(Constants.APPROVERS, approvers.toString());
					}
					break;
				default:
					break;
				}
			}
			if (isSMEUser) {
				customParams.addProperty("approvedBy", "N/A");
				customParams.addProperty("rejectedBy", "N/A");
			}

			AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);
			
			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null,
					CustomerSession.getCustomerId(customer), null,  customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine.").log();
		}
	}
	
	private Result stubCommitResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", String.format("%06d", new java.util.Date().getTime()%1000000));
        result.addStringParam("status", "Sent");
		result.addStringParam("message", "Success! Your transaction has been completed");
        result.addStringParam("totalAmount",inputParams.containsKey("totalAmount") && inputParams.get("totalAmount")!=null?inputParams.get("totalAmount").toString():"");
        result.addStringParam("charges", "");
        result.addStringParam("fromAccountNumber", inputParams.containsKey("fromAccountNumber") && inputParams.get("fromAccountNumber")!=null?inputParams.get("fromAccountNumber").toString():"");
		result.addStringParam("scheduledDate", inputParams.containsKey("scheduledDate") && inputParams.get("scheduledDate")!=null?inputParams.get("scheduledDate").toString():"");
        result.addStringParam("isScheduled", inputParams.containsKey("isScheduled") && inputParams.get("isScheduled")!=null?inputParams.get("isScheduled").toString():"");
        result.addStringParam("fromAccountCurrency",inputParams.containsKey("fromAccountCurrency") && inputParams.get("fromAccountCurrency")!=null? inputParams.get("fromAccountCurrency").toString():"");
        result.addStringParam("frequencyType", inputParams.containsKey("frequencyType") && inputParams.get("frequencyType")!=null?inputParams.get("frequencyType").toString():"");
        result.addStringParam("toAccountNumber", inputParams.containsKey("toAccountNumber") && inputParams.get("toAccountNumber")!=null?inputParams.get("toAccountNumber").toString():"");
        result.addStringParam("transactionCurrency", inputParams.containsKey("transactionCurrency") && inputParams.get("transactionCurrency")!=null?inputParams.get("transactionCurrency").toString():"");
        result.addStringParam("transactionType",inputParams.containsKey("transactionType") && inputParams.get("transactionType")!=null?inputParams.get("transactionType").toString() :"");
        result.addStringParam("paidBy", inputParams.containsKey("paidBy") && inputParams.get("paidBy")!=null?inputParams.get("paidBy").toString():"");
        result.addStringParam("serviceName", "TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE");
        result.addStringParam("transactionAmount",inputParams.containsKey("amount") && inputParams.get("amount")!=null? inputParams.get("amount").toString():"");
		result.addStringParam("transactionId", "123");
        //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
        return result;
	}
	private Result stubValidateResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", "12345");
        result.addStringParam("status", "success");
        result.addStringParam("totalAmount",inputParams.get("amount").toString());
        result.addStringParam("charges", "");
        //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
        return result;
	}
	
	private Result stubDeleteTransactionResponse() {
		
		Result result = new Result();
		result.addStringParam("transactionId", String.format("%06d", new java.util.Date().getTime()%1000000));
        result.addStringParam("status", "Sent");
        result.addStringParam("message","Success! Your transaction has been completed");
        //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
        return result;

	}
	
}
