package com.temenos.dbx.product.transactionservices.resource.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
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
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.P2PTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;
import com.temenos.dbx.product.transactionservices.resource.api.P2PTransactionResource;

public class P2PTransactionResourceImpl implements P2PTransactionResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
		DBPAPIAbstractFactoryImpl.getBackendDelegate(P2PTransactionBackendDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	    P2PTransactionBackendDelegate p2pTransctionBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(P2PTransactionBackendDelegate.class);
		
		P2PTransactionDTO p2pDTO = null;
		Result result = new Result();
		Double amount = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		inputParams.put("legalEntityId", legalEntityId);
		String featureActionId = null;
		
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
		
		String amountValue = inputParams.get("amount").toString();
		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();
		String personId = null;
		String toAccountNumber = null;
		String p2pContact = null;
		String baseCurrency  = application.getBaseCurrencyFromCache();
		String transactionCurrency = inputParams.get("transactionCurrency") != null ?inputParams.get("transactionCurrency").toString() : baseCurrency;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
	    
	    
		/*if(amountValue == null || amountValue == "") {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}*/
		if(inputParams.get("personId") != null) {
			personId = inputParams.get("personId").toString();
		}
		if(inputParams.get("toAccountNumber") != null) {
			toAccountNumber = inputParams.get("toAccountNumber").toString();
		}
		if(inputParams.get("p2pContact") != null) {
			p2pContact = inputParams.get("p2pContact").toString();
		}

		if(personId == null && toAccountNumber == null && p2pContact == null) {
			return ErrorCodeEnum.ERR_20700.setErrorCode(new Result());
		}
		featureActionId = FeatureAction.P2P_CREATE;
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
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
		
		try {
			p2pDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		String date = p2pDTO.getScheduledDate() == null ? 
				(p2pDTO.getProcessingDate() == null ? 
						(p2pDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: p2pDTO.getFrequencyStartDate())
						: p2pDTO.getProcessingDate()) 
				: p2pDTO.getScheduledDate();
								
		String backendid= inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String requestid = "";
	
		if("true".equalsIgnoreCase(validate)) {
			P2PTransactionBackendDTO backendObj = new P2PTransactionBackendDTO();
			backendObj =  backendObj.convert(p2pDTO);
			P2PTransactionDTO validatep2pDto = p2pTransctionBackendDelegate.validateTransaction(backendObj,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validatep2pDto).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response for p2p transfer: ", e).log();
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
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(backendid);
		transactionStatusDTO.setServiceCharge(serviceCharge);
				
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		
		TransactionStatusEnum transactionStatus =transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
								
		p2pDTO.setStatus(transactionStatus.getStatus());
		p2pDTO.setTransactionAmount(p2pDTO.getTransactionAmount());
		try {
			p2pDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		}
		catch(NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		p2pDTO.setRequestId(transactionStatusDTO.getRequestId());
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		p2pDTO.setConfirmationNumber(confirmationNumber);
		p2pDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		//p2pDTO.setTransactionId(null);
		
		P2PTransactionDTO p2pdbxdto = p2pTransactionDelegate.createTransactionAtDBX(p2pDTO);
		
		if(p2pdbxdto == null) {
			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		P2PTransactionBackendDTO p2pBackendDTO = new P2PTransactionBackendDTO();
		p2pBackendDTO = p2pBackendDTO.convert(p2pdbxdto);
		
		try {
			p2pBackendDTO.setAmount(Double.parseDouble(p2pBackendDTO.getTransactionAmount()));
			p2pdbxdto.setAmount(Double.parseDouble(p2pBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		try {
			String responseObj = new JSONObject(p2pBackendDTO).toString();
			result = JSONToResult.convert(responseObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for P2P transfer: ", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
		}
		
		String referenceId = p2pdbxdto.getTransactionId();
		String beneficiaryEmail = inputParams.get("beneficiaryEmail") == null ? "" : inputParams.get("beneficiaryEmail").toString();
        String beneficiaryPhone = inputParams.get("beneficiaryPhone") == null ? "" : inputParams.get("beneficiaryPhone").toString();
        String beneficiaryName = inputParams.get("beneficiaryName") == null ? "" : inputParams.get("beneficiaryName").toString();
        p2pBackendDTO.setBeneficiaryName(beneficiaryName);
        p2pBackendDTO.setBeneficiaryPhone(beneficiaryPhone);
        p2pBackendDTO.setBeneficiaryEmail(beneficiaryEmail);
		
		
		if (transactionStatus == TransactionStatusEnum.SENT) {
			P2PTransactionDTO p2pbackendtrxDto=new P2PTransactionDTO();
			if(StringUtils.isEmpty(backendid)) {
				p2pBackendDTO.setTransactionId(null);
				p2pbackendtrxDto = p2pTransctionBackendDelegate.createTransactionWithoutApproval(p2pBackendDTO, request);					
				if(p2pbackendtrxDto == null) {	
					p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12600.setErrorCode(result);
				}
			}
			else {
				String frequency = StringUtils.isEmpty(p2pDTO.getFrequencyTypeId()) ? null : p2pDTO.getFrequencyTypeId();
				p2pbackendtrxDto = p2pTransactionDelegate.approveTransaction(backendid, request, frequency);
				if(p2pbackendtrxDto == null) {	
					p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			if(p2pbackendtrxDto.getDbpErrCode() != null || p2pbackendtrxDto.getDbpErrMsg() != null) {
				p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pbackendtrxDto.getDbpErrMsg());
			}
			String refId = p2pbackendtrxDto.getReferenceId();
			if(refId == null || "".equals(refId)) {
				p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.EXECUTED.getStatus(), p2pbackendtrxDto.getReferenceId());
			result.addParam(new Param("referenceId", refId));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId= null; 
			if(StringUtils.isEmpty(backendid))
			{
				P2PTransactionDTO p2ppendingtxnDto = p2pTransactionDelegate.createPendingTransaction(p2pdbxdto, request);
				if(p2ppendingtxnDto == null)
				{
					p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(p2ppendingtxnDto.getDbpErrCode() != null || p2ppendingtxnDto.getDbpErrMsg() != null) {
					p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2ppendingtxnDto.getDbpErrMsg());
				}
				backendid = p2ppendingtxnDto.getReferenceId();
			}
				pendingrefId = backendid;
				p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId,transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					p2pTransctionBackendDelegate.deleteTransaction(backendid, null, request);
					p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					p2pTransctionBackendDelegate.deleteTransaction(backendid, null, request);
					p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),backendid);
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
				p2pTransactionDelegate.updateStatusUsingTransactionId(referenceId,transactionStatus.toString(), pendingrefId);
			}
		}
		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
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
		P2PTransactionDTO p2pTransactionDTO = null;
		
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
			P2PTransactionBusinessDelegate p2pTransactionDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
			p2pTransactionDTO = p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
			if(p2pTransactionDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			JSONObject p2pTransactionJSON = new JSONObject(p2pTransactionDTO);
			result = JSONToResult.convert(p2pTransactionJSON.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking p2ptransaction business delegate",exp).log();
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
    	
    	P2PTransactionBusinessDelegate p2pBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
    	
    	if(referenceId != null) {
    		status = TransactionStatusEnum.EXECUTED;
    		confirmationNumber = referenceId.getValue();
    	}
    	else {
    		status = TransactionStatusEnum.FAILED;
    	}
    	inputParams.put("status", status.getStatus());
    	p2pBusinessDelegate.updateStatusUsingTransactionId(transactionId, status.getStatus(), confirmationNumber);
    	
    	result.addParam(new Param("dbxtransactionId", transactionId));
    	*/
    	return result;
    }
    
    @Override
	public Result editTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
		P2PTransactionBackendDelegate p2pTransctionBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(P2PTransactionBackendDelegate.class);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		
		String featureActionId = FeatureAction.P2P_CREATE;
		P2PTransactionDTO p2pDTO = null;
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
			p2pDTO = p2pTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", Arrays.asList(companyId), createdby, legalEntityId);
			if(p2pDTO == null) {
				p2pDTO = p2pTransctionBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(p2pDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
				p2pDTO = p2pTransactionDelegate.createTransactionAtDBX(p2pDTO);
			}
		}
		else {
			alert.prepareError("confirmationNumber is missing in the payload which is mandatory for edit").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
		}
		
		String transactionId = p2pDTO.getTransactionId();
		String oldRequestId = p2pDTO.getRequestId();
		
		//Authorization checks on transcationId
		/*
		if(p2pDTO.getCompanyId() != null && ! p2pDTO.getCompanyId().equals(companyId)) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isBusinessUser && ! CustomerSession.IsCombinedUser(customer) &&  ! CustomerSession.IsCombinedUser(customer) && ! p2pDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = p2pDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		P2PTransactionDTO requestDTO = null;
		inputParams.put("createdby", createdby);
		inputParams.put("companyId", companyId);
		inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		
		try {
			requestDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PTransactionDTO.class);
			requestDTO.setConfirmationNumber(null);
			requestDTO.setTransactionId(null);
			p2pDTO = p2pDTO.updateValues(requestDTO);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(p2pDTO == null) {
			alert.prepareError("Error occured while fetching the input params: " ).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		//Authorization checks on newfromAccountNumber and featureactionId
		String newfromAccountNumber = p2pDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, newfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		String date = p2pDTO.getScheduledDate() == null ? 
				(p2pDTO.getProcessingDate() == null ? 
						(p2pDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: p2pDTO.getFrequencyStartDate())
						: p2pDTO.getProcessingDate()) 
				: p2pDTO.getScheduledDate();
		
		//Transaction limit checks on amount and featureactionId
		Double amount = p2pDTO.getAmount();
		String transactionAmount = inputParams.get("transactionAmount") != null ? inputParams.get("transactionAmount").toString() : null;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
	    
	    if("0".equals(transactionAmount) || "0.0".equals(transactionAmount) || "0.00".equals(transactionAmount)) {
			transactionAmount = amount +"";
		}
	    
	    if( !StringUtils.isEmpty(transactionAmount) && !StringUtils.isEmpty(serviceCharge)) {
			amount = Double.parseDouble(transactionAmount) + Double.parseDouble(serviceCharge);
		}
		else if(!StringUtils.isEmpty(serviceCharge)) {
			amount = amount + Double.parseDouble(serviceCharge);
		}
		else if(!StringUtils.isEmpty(transactionAmount)) {
			amount = Double.parseDouble(transactionAmount);
		}
	    
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		
		if("true".equalsIgnoreCase(validate)) {
			P2PTransactionBackendDTO backendObj = new P2PTransactionBackendDTO();
			backendObj =  backendObj.convert(p2pDTO);
			P2PTransactionDTO validateownaccountDTO = p2pTransctionBackendDelegate.validateTransaction(backendObj,request);
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
		transactionStatusDTO.setTransactionCurrency(p2pDTO.getTransactionCurrency());
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
		
		//these things should come from limits Engine after proper integration with validate call
		TransactionStatusEnum transactionStatus =transactionStatusDTO.getStatus();
		p2pDTO.setStatus(transactionStatus.getStatus());
		//p2pDTO.setRequestId(null);
		p2pDTO.setConfirmationNumber(confirmationNumber+"");
		p2pDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		try {
			p2pDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		}
		catch(NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		p2pDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		
		P2PTransactionBackendDTO p2pBackendDTO = new P2PTransactionBackendDTO();
		p2pBackendDTO = p2pBackendDTO.convert(p2pDTO);
		
		try {
			p2pBackendDTO.setAmount(Double.parseDouble(p2pBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		String requestObj = null;
		try {
			requestObj = new JSONObject(p2pBackendDTO).toString();
			result = JSONToResult.convert(requestObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for p2p transfer: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		String referenceId = null;
		P2PTransactionDTO resDTO = null;
		if (transactionStatus == TransactionStatusEnum.SENT) {	

				if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
					p2pBackendDTO.setTransactionId(confirmationNumber);
					resDTO = p2pTransctionBackendDelegate.editTransactionWithoutApproval(p2pBackendDTO, request);
				}
				
				if(resDTO == null) {
					p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12600.setErrorCode(result);
				}
				
				if(resDTO.getDbpErrMsg() != null && !resDTO.getDbpErrMsg().isEmpty()) {
					p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
	                return ErrorCodeEnum.ERR_00000.setErrorCode(result, resDTO.getDbpErrMsg());
				}
				
				referenceId = resDTO.getReferenceId();

				if(StringUtils.isEmpty(referenceId)) {
					p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Failed to edit the transaction at backend").log();
					return ErrorCodeEnum.ERR_12602.setErrorCode(new Result());
				}
								
				p2pDTO = resDTO;
				p2pDTO.setConfirmationNumber(referenceId);
				p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), referenceId);
				
				result.addParam(new Param("referenceId", resDTO.getReferenceId()));
				result.addParam(new Param("status", transactionStatus.getStatus()));
		        result.addParam(new Param("message", transactionStatus.getMessage()));
				
		}else if(transactionStatus == TransactionStatusEnum.PENDING){
		
				String requestId = transactionStatusDTO.getRequestId();
	    		referenceId = confirmationNumber;
	    		p2pBackendDTO.setTransactionId(confirmationNumber);
	    		P2PTransactionDTO pendingtransactionDTO = p2pTransctionBackendDelegate.editTransactionWithApproval(p2pBackendDTO, request);
	    		
	    		if(pendingtransactionDTO == null)
	    		{
	    			p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
	    			alert.prepareError("Error occured while creating entry into the backend table: ").log();
	    			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
	    		}
	    		
	    		if(pendingtransactionDTO.getDbpErrCode() != null || pendingtransactionDTO.getDbpErrMsg() != null) {
	    			p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
	                return ErrorCodeEnum.ERR_00000.setErrorCode(result, pendingtransactionDTO.getDbpErrMsg());
	    		}
	    		
	    		String backendid = pendingtransactionDTO.getReferenceId();
	    		p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
	            transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, backendid, transactionStatusDTO.isSelfApproved(), featureActionId, request);
	            if(transactionStatusDTO == null) 
				{							
					p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
					result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
					return result;
				}
				transactionStatus=transactionStatusDTO.getStatus();
	            p2pTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
	            p2pDTO.setConfirmationNumber(backendid);
	            p2pDTO.setStatus(transactionStatus.toString());
	            p2pDTO.setRequestId(requestId);
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
		
		//delete the old transaction entry from DBX
        if(referenceId != null && !referenceId.isEmpty()) {
        	p2pDTO.setTransactionId(transactionId);
        	p2pTransactionDelegate.updateTransactionAtDBX(p2pDTO);
			
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
		
		P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
		
		P2PTransactionBackendDelegate p2pTransctionBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				   .getFactoryInstance(BackendDelegateFactory.class)
				   .getBackendDelegate(P2PTransactionBackendDelegate.class);
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		String featureActionId = FeatureAction.P2P_CREATE;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		P2PTransactionDTO p2pDTO = null;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];

		Object transactionIdParam = inputParams.get("confirmationNumber");
		Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");
		String transactionId = null;
		String confirmationNumber = null;
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(createdby);
		contracts.add(CustomerSession.getCompanyId(customer));
		
		if(confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
			confirmationNumber = confirmationNumberParam.toString();
			p2pDTO = p2pTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", contracts, createdby, null);
			if(p2pDTO == null) {
				p2pDTO = p2pTransctionBackendDelegate.cancelTransactionOccurrence(confirmationNumber, request);
				
				if(p2pDTO.getDbpErrMsg() != null && !p2pDTO.getDbpErrMsg().isEmpty()) {
					alert.prepareError(p2pDTO.getDbpErrMsg()).log();
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pDTO.getDbpErrMsg());
				}
				
				String referenceId = p2pDTO.getReferenceId();
				transactionId = p2pDTO.getTransactionId();
				if(referenceId == null || "".equals(referenceId)) {
					alert.prepareError("Failed to cancel  transaction occurence at backend").log();
					return ErrorCodeEnum.ERR_12603.setErrorCode(result);
				}
				result.addParam(new Param("transactionId", confirmationNumber+""));
				return result;
			}
		}
		else {
			alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for cancel occurrence").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		
		transactionId = p2pDTO.getTransactionId();
		String oldrequestId = p2pDTO.getRequestId();
		
		//Authorization checks on transcationId
		/*
		if(p2pDTO.getCompanyId() != null && ! contracts.contains(p2pDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && !CustomerSession.IsCombinedUser(customer) && ! p2pDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = p2pDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
			
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
			p2pDTO = p2pTransctionBackendDelegate.cancelTransactionOccurrence(confirmationNumber+"", request);
		}
		else {
			
			p2pTransactionDelegate.deleteTransactionAtDBX(transactionId);
			
			if(oldrequestId != null) {
				approvalQueueDelegate.deleteTransactionFromApprovalQueue(oldrequestId);
			}
		}
		
		if(p2pDTO.getDbpErrMsg() != null && !p2pDTO.getDbpErrMsg().isEmpty()) {
			alert.prepareError(p2pDTO.getDbpErrMsg()).log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pDTO.getDbpErrMsg());
		}
		
		String referenceId = p2pDTO.getReferenceId();
		
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)
				&& (referenceId == null || "".equals(referenceId))) {
			alert.prepareError("Failed to cancel  transaction occurence at backend").log();
			return ErrorCodeEnum.ERR_12603.setErrorCode(result);
		}
		
		result.addParam(new Param("transactionId", confirmationNumber+""));
		
		return result;
	}
	
	@Override
	public Result deleteTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
		
		P2PTransactionBackendDelegate p2pTransctionBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				   .getFactoryInstance(BackendDelegateFactory.class)
				   .getBackendDelegate(P2PTransactionBackendDelegate.class);
		
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		String featureActionId = FeatureAction.P2P_CREATE;
		
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if (PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
			return stubDeleteTransactionResponse();
		}
	
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		P2PTransactionDTO p2pDTO = null;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];

		Object transactionIdParam = inputParams.get("confirmationNumber");
		Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");
		String transactionId = null;
		String confirmationNumber = null;
		String transactionType = null;
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(createdby);
		contracts.add(CustomerSession.getCompanyId(customer));
		
		if(confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
			confirmationNumber = confirmationNumberParam.toString();
			p2pDTO = p2pTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", contracts, createdby, legalEntityId);
			if(p2pDTO == null) {
				p2pDTO = p2pTransctionBackendDelegate.deleteTransaction(confirmationNumber+"", transactionType, request);
				String referenceId = p2pDTO.getReferenceId();
				
				if(p2pDTO.getDbpErrMsg() != null && !p2pDTO.getDbpErrMsg().isEmpty()) {
					alert.prepareError(p2pDTO.getDbpErrMsg()).log();
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pDTO.getDbpErrMsg());
				}
				
				if(referenceId == null || "".equals(referenceId)) {
					alert.prepareError("Failed to delete the transaction at backend").log();
					return ErrorCodeEnum.ERR_12603.setErrorCode(result);
				}
				result.addParam(new Param("transactionId", confirmationNumber+""));
				return result;
			}
		}
		else {
			alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for delete").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		
		transactionId = p2pDTO.getTransactionId();
		transactionType = p2pDTO.getTransactionType();
		String oldrequestId = p2pDTO.getRequestId();
		
		//Authorization checks on transcationId
		if(p2pDTO.getCompanyId() != null && ! contracts.contains(p2pDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! CustomerSession.IsCombinedUser(customer) && ! p2pDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = p2pDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
			p2pDTO = p2pTransctionBackendDelegate.deleteTransaction(confirmationNumber+"", transactionType, request);
		}
		
		if(p2pDTO.getDbpErrMsg() != null && !p2pDTO.getDbpErrMsg().isEmpty()) {
			alert.prepareError(p2pDTO.getDbpErrMsg()).log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pDTO.getDbpErrMsg());
		}
		
		String referenceId = p2pDTO.getReferenceId();
		
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)
				&& (referenceId == null || "".equals(referenceId))) {
			alert.prepareError("Failed to delete the transaction at backend").log();
			return ErrorCodeEnum.ERR_12603.setErrorCode(result);
		}
		
		p2pTransactionDelegate.deleteTransactionAtDBX(transactionId);
		 
		if(oldrequestId != null) {
			approvalQueueDelegate.deleteTransactionFromApprovalQueue(oldrequestId);
		}
		
		result.addParam(new Param("transactionId", confirmationNumber+""));
		
		return result;
	}
	
	private Result stubCommitResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", String.format("%06d", new java.util.Date().getTime()%1000000));
        result.addStringParam("status", "Sent");
		result.addStringParam("message", "Success! Your transaction has been completed");
        result.addStringParam("totalAmount",inputParams.containsKey("amount") && inputParams.get("amount")!=null?inputParams.get("amount").toString():"");
        result.addStringParam("charges", "");
        result.addStringParam("fromAccountNumber", inputParams.containsKey("fromAccountNumber") && inputParams.get("fromAccountNumber")!=null?inputParams.get("fromAccountNumber").toString():"");
		result.addStringParam("scheduledDate",inputParams.containsKey("scheduledDate") && inputParams.get("scheduledDate")!=null? inputParams.get("scheduledDate").toString():"");
        result.addStringParam("isScheduled",inputParams.containsKey("isScheduled") && inputParams.get("isScheduled")!=null? inputParams.get("isScheduled").toString():"");
        result.addStringParam("fromAccountCurrency", inputParams.containsKey("fromAccountCurrency") && inputParams.get("fromAccountCurrency")!=null?inputParams.get("fromAccountCurrency").toString():"");
        result.addStringParam("frequencyType", inputParams.containsKey("frequencyType") && inputParams.get("frequencyType")!=null?inputParams.get("frequencyType").toString():"");
        result.addStringParam("beneficiaryId","");
        result.addStringParam("transactionCurrency",inputParams.containsKey("transactionCurrency") && inputParams.get("transactionCurrency")!=null? inputParams.get("transactionCurrency").toString():"");
        result.addStringParam("transactionType",inputParams.containsKey("transactionType") && inputParams.get("transactionType")!=null?inputParams.get("transactionType").toString():"");
        result.addStringParam("beneficiaryName", inputParams.containsKey("beneficiaryName") && inputParams.get("beneficiaryName")!=null?inputParams.get("beneficiaryName").toString():"");
        result.addStringParam("paidBy", inputParams.containsKey("paidBy") && inputParams.get("paidBy")!=null? inputParams.get("paidBy").toString():"");
        result.addStringParam("serviceName", "P2P_CREATE");
        result.addStringParam("transactionAmount", inputParams.containsKey("transactionAmount") && inputParams.get("transactionAmount")!=null?inputParams.get("transactionAmount").toString():"");
		result.addStringParam("personId", "70425110");
        result.addStringParam("beneficiaryPhone", inputParams.containsKey("beneficiaryPhone") && inputParams.get("beneficiaryPhone")!=null?inputParams.get("beneficiaryPhone").toString():"");
		result.addStringParam("beneficiaryEmail", inputParams.containsKey("beneficiaryEmail") && inputParams.get("beneficiaryEmail")!=null?inputParams.get("beneficiaryEmail").toString():"");
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
		result.addStringParam("referenceId", String.format("%06d", new java.util.Date().getTime()%1000000));
        result.addStringParam("status", "Sent");
        result.addStringParam("message","Success! Your transaction has been completed");
        //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
        return result;

	}

}
