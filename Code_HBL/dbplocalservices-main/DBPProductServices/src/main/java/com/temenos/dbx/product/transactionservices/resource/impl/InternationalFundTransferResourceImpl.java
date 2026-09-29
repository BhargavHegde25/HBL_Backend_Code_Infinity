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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
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
import com.konylabs.middleware.dataobject.Record;
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
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InterBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InternationalFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferDTO;
import com.temenos.dbx.product.transactionservices.resource.api.InternationalFundTransferResource;

public class InternationalFundTransferResourceImpl implements InternationalFundTransferResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
    InternationalFundTransferBackendDelegate internationalfundBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InternationalFundTransferBackendDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
	InterBankFundTransferBackendDelegate interbankBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InterBankFundTransferBackendDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	

    
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		InternationalFundTransferDTO internationfundtransferDTO = null;
		Result result = new Result();
		Double amount = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		
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
		
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		if(StringUtils.isEmpty(legalEntityId))
			legalEntityId=(String) customer.get("legalEntityId");

		String featureActionId = null;
		
		String amountValue = inputParams.get("amount").toString();
		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();

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

		String transactionCurrency = inputParams.get("transactionCurrency") != null ?inputParams.get("transactionCurrency").toString() : baseCurrency;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
	    String frequencyType = inputParams.get("frequencyType") == null ? null : inputParams.get("frequencyType").toString();
	    
		if(amountValue == null || amountValue == "") {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}
		
		featureActionId = FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE;
		
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
		
		try {
			internationfundtransferDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalFundTransferDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}
			
		String date = internationfundtransferDTO.getScheduledDate() == null ? 
				(internationfundtransferDTO.getProcessingDate() == null ? 
						(internationfundtransferDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: internationfundtransferDTO.getFrequencyStartDate())
						: internationfundtransferDTO.getProcessingDate()) 
				: internationfundtransferDTO.getScheduledDate();
								

		String transactionId = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		TransactionStatusDTO transactionStatusDTO = null;
		
		if("true".equalsIgnoreCase(validate)) {
			InternationalFundTransferBackendDTO internationalFundTransferBackendDTO = new InternationalFundTransferBackendDTO();
			internationalFundTransferBackendDTO = internationalFundTransferBackendDTO.convert(internationfundtransferDTO);
			
			InternationalFundTransferDTO validateaccountDTO = internationalfundBackendDelegate.validateTransaction(internationalFundTransferBackendDTO,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validateaccountDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for InternationalFund transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		}
		String payeeVerificationStatus = "";
		String payeeVerificationErrMsg = "";
		InterBankFundTransferResourceImpl interBankFundTransferResourceImpl = new InterBankFundTransferResourceImpl();
		interBankFundTransferResourceImpl.verifyPayeeAndUpdateDB(request, inputParams ,result, payeeVerificationStatus, payeeVerificationErrMsg, true);
		payeeVerificationStatus= null != result.getParamValueByName("payeeVerificationStatus") ? result.getParamValueByName("payeeVerificationStatus") : "";
		payeeVerificationErrMsg= null !=result.getParamValueByName("payeeVerificationErrMsg")? result.getParamValueByName("payeeVerificationErrMsg") : "";
		if(payeeVerificationStatus.equalsIgnoreCase("Failure")) {
			return result;
		}
		transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(fromAccountNumber);
		transactionStatusDTO.setAmount(amount);
		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setTransactionCurrency(transactionCurrency);
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(transactionId);
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
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		
		internationfundtransferDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		try {
			if(transactionCurrency!=baseCurrency)
				internationfundtransferDTO.setAmount(_getConvertedAmount(Double.parseDouble(amountValue),transactionCurrency,baseCurrency,request));
			else
				internationfundtransferDTO.setAmount(amount);
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		internationfundtransferDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		internationfundtransferDTO.setRequestId(transactionStatusDTO.getRequestId());
		internationfundtransferDTO.setStatus(transactionStatus.getStatus());
		
		String confirmationNumber = (StringUtils.isEmpty(transactionId)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : transactionId;
		internationfundtransferDTO.setConfirmationNumber(confirmationNumber);
		internationfundtransferDTO.setLegalEntityId(legalEntityId);
		
		InternationalFundTransferDTO internationalFundAccountdbxDTO = internationalFundTransferDelegate.createTransactionAtDBX(internationfundtransferDTO);
		
		if(internationalFundAccountdbxDTO == null) {
			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		
		if(internationalFundAccountdbxDTO.getDbpErrCode() != null || internationalFundAccountdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", internationalFundAccountdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", internationalFundAccountdbxDTO.getDbpErrMsg()));
			return result;
		}
		
		internationalFundAccountdbxDTO.setValidate(validate);
		
		InternationalFundTransferBackendDTO internationfundtransferBackendDTO = new InternationalFundTransferBackendDTO();
		internationfundtransferBackendDTO = internationfundtransferBackendDTO.convert(internationalFundAccountdbxDTO);
		
		String creditValueDate = inputParams.get("creditValueDate") == null ? "" : inputParams.get("creditValueDate").toString();
        String totalAmount = inputParams.get("totalAmount") == null ? "" : inputParams.get("totalAmount").toString();
        String exchangeRate = inputParams.get("exchangeRate") == null ? "" : inputParams.get("exchangeRate").toString();
        String intermediaryBicCode = inputParams.get("intermediaryBicCode") == null ? "" : inputParams.get("intermediaryBicCode").toString();
        String clearingCode = inputParams.get("clearingCode") == null ? "" : inputParams.get("clearingCode").toString();
        String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
        String beneficiaryBankName = inputParams.get("beneficiaryBankName") == null ? "" : inputParams.get("beneficiaryBankName").toString();
        String beneficiaryAddressLine2 = inputParams.get("beneficiaryAddressLine2") == null ? "" : inputParams.get("beneficiaryAddressLine2").toString();
        String beneficiaryPhone = inputParams.get("beneficiaryPhone") == null ? "" : inputParams.get("beneficiaryPhone").toString();
        String beneficiaryEmail = inputParams.get("beneficiaryEmail") == null ? "" : inputParams.get("beneficiaryEmail").toString();
        String beneficiaryState = inputParams.get("beneficiaryState") == null ? "" : inputParams.get("beneficiaryState").toString();
		String beneficiaryAddressLine1= inputParams.get("beneficiaryAddressLine1") == null ? "" : inputParams.get("beneficiaryAddressLine1").toString();
		String beneficiaryCity= inputParams.get("beneficiaryCity") == null ? "" : inputParams.get("beneficiaryCity").toString();
		String beneficiaryZipcode= inputParams.get("beneficiaryZipcode") == null ? "" : inputParams.get("beneficiaryZipcode").toString();
		String beneficiarycountry= inputParams.get("beneficiarycountry") == null ? "" : inputParams.get("beneficiarycountry").toString();
		String purposeCode= inputParams.get("purposeCode") == null ? "" : inputParams.get("purposeCode").toString();
		String clearingIdentifierCode= inputParams.get("clearingIdentifierCode") == null ? "" : inputParams.get("clearingIdentifierCode").toString();
		String streetName= inputParams.get("streetName") == null ? "" : inputParams.get("streetName").toString();
		String townName= inputParams.get("townName") == null ? "" : inputParams.get("townName").toString();
		String countryName= inputParams.get("countryName") == null ? "" : inputParams.get("countryName").toString();
		
        internationfundtransferBackendDTO.setCreditValueDate(creditValueDate);
        internationfundtransferBackendDTO.setTotalAmount(totalAmount);
        internationfundtransferBackendDTO.setExchangeRate(exchangeRate);
        internationfundtransferBackendDTO.setIntermediaryBicCode(intermediaryBicCode);
        internationfundtransferBackendDTO.setClearingCode(clearingCode);
        internationfundtransferBackendDTO.setOverrides(overrides);
        internationfundtransferBackendDTO.setBeneficiaryBankName(beneficiaryBankName);
        internationfundtransferBackendDTO.setBeneficiaryAddressLine2(beneficiaryAddressLine2);
		internationfundtransferBackendDTO.setBeneficiaryAddressLine1(beneficiaryAddressLine1);
        internationfundtransferBackendDTO.setBeneficiaryPhone(beneficiaryPhone);
        internationfundtransferBackendDTO.setBeneficiaryEmail(beneficiaryEmail);
        internationfundtransferBackendDTO.setBeneficiaryState(beneficiaryState);
		internationfundtransferBackendDTO.setBeneficiaryCity(beneficiaryCity);
		internationfundtransferBackendDTO.setBeneficiaryZipcode(beneficiaryZipcode);
		internationfundtransferBackendDTO.setBeneficiarycountry(beneficiarycountry);
		internationfundtransferBackendDTO.setPurposeCode(purposeCode);
		internationfundtransferBackendDTO.setClearingIdentifierCode(clearingIdentifierCode);
		internationfundtransferBackendDTO.setStreetName(streetName);
		internationfundtransferBackendDTO.setTownName(townName);
		internationfundtransferBackendDTO.setCountryName(countryName);
		internationfundtransferBackendDTO.setPayeeVerificationStatus(payeeVerificationStatus);
		internationfundtransferBackendDTO.setPayeeVerificationErrMsg(payeeVerificationErrMsg);
		
		String beneficiaryId = inputParams.get("beneficiaryId") == null ? null : inputParams.get("beneficiaryId").toString();
		internationfundtransferBackendDTO.setBeneficiaryId(beneficiaryId);
		
		try {
			internationfundtransferBackendDTO.setAmount(Double.parseDouble(internationfundtransferBackendDTO.getTransactionAmount()));
			internationalFundAccountdbxDTO.setAmount(Double.parseDouble(internationfundtransferBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		try {
			String responseObj = new JSONObject(internationfundtransferBackendDTO).toString();
			result = JSONToResult.convert(responseObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for External transfer: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		String referenceId = internationalFundAccountdbxDTO.getTransactionId();
		String requestId = "";
		String createWithPaymentId = inputParams.get("createWithPaymentId") == null ? ""
                : inputParams.get("createWithPaymentId").toString();
		
		if(transactionStatus == TransactionStatusEnum.SENT ) {
			InternationalFundTransferDTO internationalfundtransferDTO= new InternationalFundTransferDTO();
			if (StringUtils.isEmpty(transactionId)
                    || (StringUtils.isNotBlank(transactionId) && createWithPaymentId.equalsIgnoreCase("true"))) {
			    if(StringUtils.isNotBlank(transactionId) && createWithPaymentId.equalsIgnoreCase("true")){
			        internationfundtransferBackendDTO.setTransactionId(transactionId);
			        String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
			        internationfundtransferBackendDTO.setCharges(charges);
                } else {
                    internationfundtransferBackendDTO.setTransactionId(null);
                }
				internationalfundtransferDTO = internationalfundBackendDelegate.createTransactionWithoutApproval(internationfundtransferBackendDTO, request);					
				if(internationalfundtransferDTO == null) {	
					internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12600.setErrorCode(result);
				}
			}
			else {
				String frequency = StringUtils.isEmpty(internationfundtransferDTO.getFrequencyTypeId()) ? null : internationfundtransferDTO.getFrequencyTypeId();
				internationalfundtransferDTO = internationalFundTransferDelegate.approveTransaction(transactionId, request, frequency);
				if(internationalfundtransferDTO == null) {	
					internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			
			if(internationalfundtransferDTO.getDbpErrCode() != null || internationalfundtransferDTO.getDbpErrMsg() != null) {
				internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				result.addParam(new Param("errorDetails", internationalfundtransferDTO.getErrorDetails()));
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalfundtransferDTO.getDbpErrMsg());  
			}
			
			String refId = internationalfundtransferDTO.getReferenceId();
			if(refId == null || "".equals(refId)) {
				internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			internationfundtransferDTO=internationalfundtransferDTO;
			internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.EXECUTED.getStatus(), internationalfundtransferDTO.getReferenceId());
			result.addParam(new Param("referenceId", internationalfundtransferDTO.getReferenceId()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			internationalFundAccountdbxDTO.setCreditValueDate(creditValueDate);
			internationalFundAccountdbxDTO.setTotalAmount(totalAmount);
			internationalFundAccountdbxDTO.setExchangeRate(exchangeRate);
			internationalFundAccountdbxDTO.setIntermediaryBicCode(intermediaryBicCode);
			internationalFundAccountdbxDTO.setClearingCode(clearingCode);
			requestId = transactionStatusDTO.getRequestId();
			String pendingrefId =null;
			if (StringUtils.isEmpty(transactionId)
                    || (StringUtils.isNotBlank(transactionId) && createWithPaymentId.equalsIgnoreCase("true"))) {
			    if(StringUtils.isNotBlank(transactionId)  && createWithPaymentId.equalsIgnoreCase("true")){
			        internationalFundAccountdbxDTO.setTransactionId(transactionId);
			        String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
			        internationalFundAccountdbxDTO.setCharges(charges);
                } 
				InternationalFundTransferDTO internationalFundTransferPendingtransactionDTO = internationalFundTransferDelegate.createPendingTransaction(internationalFundAccountdbxDTO, request);
				if(internationalFundTransferPendingtransactionDTO == null)
				{
					internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				
				if(internationalFundTransferPendingtransactionDTO.getDbpErrCode() != null || internationalFundTransferPendingtransactionDTO.getDbpErrMsg() != null) {
					internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					result.addParam(new Param("errorDetails", internationalFundTransferPendingtransactionDTO.getErrorDetails()));
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalFundTransferPendingtransactionDTO.getDbpErrMsg());  
				}
				transactionId = internationalFundTransferPendingtransactionDTO.getReferenceId();
				internationfundtransferDTO=internationalFundTransferPendingtransactionDTO;
			}
				pendingrefId = transactionId;
				internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId,transactionStatus.toString(), transactionId);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, transactionId, isSelfApproved, featureActionId, request);
				
				if(transactionStatusDTO == null) 
				{							
					internationalfundBackendDelegate.deleteTransactionWithoutApproval(transactionId, null, frequencyType, request);
					internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), transactionId);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}
				
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					internationalfundBackendDelegate.deleteTransactionWithoutApproval(transactionId, null, frequencyType, request);
					internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), transactionId);
					result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
					return result;
				}
				
				transactionStatus = transactionStatusDTO.getStatus();
				transactionId = transactionStatusDTO.getConfirmationNumber();
				

			result.addParam(new Param("requestId", requestId));
			

			if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
				result.addParam(new Param("referenceId", transactionId));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
				result.addParam(new Param("referenceId", pendingrefId));
				internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId,transactionStatus.toString(), pendingrefId);
			}
			
			//code snippet being added for alerts on International transfer
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null,  
						transactionId, requestId, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
			}
		}

		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		
		if(internationfundtransferDTO.getOverrides() != null) {
			result.addParam(new Param("overrides",internationfundtransferDTO.getOverrides()));
		}
		if(internationfundtransferDTO.getOverrideList() != null) {
			result.addParam(new Param("overrideList",internationfundtransferDTO.getOverrideList()));
		}
		if(internationfundtransferDTO.getCharges() != null) {
			result.addParam(new Param("charges",internationfundtransferDTO.getCharges()));
		}
		if(internationfundtransferDTO.getExchangeRate() != null) {
			result.addParam(new Param("exchangeRate",internationfundtransferDTO.getExchangeRate()));
		}
		if(internationfundtransferDTO.getTotalAmount() != null) {
			result.addParam(new Param("totalAmount",internationfundtransferDTO.getTotalAmount()));
		}
		if(internationfundtransferDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", internationfundtransferDTO.getMessageDetails()));
        }
		if(internationfundtransferDTO.getQuoteCurrency() != null) {
            result.addParam(new Param("quoteCurrency", internationfundtransferDTO.getQuoteCurrency()));
        }

		try {
			_logTransaction(request,response,inputArray,result,transactionStatus,referenceId,internationfundtransferDTO,requestId);
		}
		catch (Exception e){
			alert.prepareError("Error occured while audit logging.", e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		return result;
	}
	private Result stubCommitResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", String.format("%06d", new java.util.Date().getTime()%1000000));
        result.addStringParam("status", "Sent");
		result.addStringParam("message", "Success! Your transaction has been completed");
        result.addStringParam("totalAmount",inputParams.containsKey("totalAmount") && inputParams.get("totalAmount")!=null ?inputParams.get("totalAmount").toString():"");
        result.addStringParam("charges", "");
        result.addStringParam("fromAccountNumber",inputParams.containsKey("fromAccountNumber") && inputParams.get("fromAccountNumber")!=null? inputParams.get("fromAccountNumber").toString():"");
		result.addStringParam("scheduledDate",inputParams.containsKey("scheduledDate") && inputParams.get("scheduledDate")!=null? inputParams.get("scheduledDate").toString():"");
        result.addStringParam("swiftCode",inputParams.containsKey("swiftCode") && inputParams.get("swiftCode")!=null? inputParams.get("swiftCode").toString():"");
        result.addStringParam("isScheduled", inputParams.containsKey("isScheduled") && inputParams.get("isScheduled")!=null?inputParams.get("isScheduled").toString():"");
        result.addStringParam("fromAccountCurrency", inputParams.containsKey("fromAccountCurrency") && inputParams.get("fromAccountCurrency")!=null?inputParams.get("fromAccountCurrency").toString():"");
        result.addStringParam("frequencyType", inputParams.containsKey("frequencyType") && inputParams.get("frequencyType")!=null?inputParams.get("frequencyType").toString():"");
        result.addStringParam("toAccountNumber", inputParams.containsKey("toAccountNumber") && inputParams.get("toAccountNumber")!=null?inputParams.get("toAccountNumber").toString():"");
        result.addStringParam("transactionCurrency",inputParams.containsKey("transactionCurrency") && inputParams.get("transactionCurrency")!=null? inputParams.get("transactionCurrency").toString():"");
        result.addStringParam("transactionType",inputParams.containsKey("transactionType") && inputParams.get("transactionType")!=null?inputParams.get("transactionType").toString():"");
        result.addStringParam("beneficiaryName",inputParams.containsKey("beneficiaryName") && inputParams.get("beneficiaryName")!=null? inputParams.get("beneficiaryName").toString():"");
        result.addStringParam("paidBy",inputParams.containsKey("paidBy") && inputParams.get("paidBy")!=null? inputParams.get("paidBy").toString():"");
        result.addStringParam("serviceName", "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE");
        result.addStringParam("transactionAmount", inputParams.containsKey("amount") && inputParams.get("amount")!=null?inputParams.get("amount").toString():"");
		result.addStringParam("featureActionId", "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE");
		result.addStringParam("transactionId", "123");
        result.addStringParam("beneficiaryBankName",inputParams.containsKey("beneficiaryBankName") && inputParams.get("beneficiaryAddressLine1")!=null? inputParams.get("beneficiaryBankName").toString():"");
		result.addStringParam("beneficiaryAddressLine1", inputParams.containsKey("beneficiaryAddressLine1") && inputParams.get("beneficiaryAddressLine1")!=null?inputParams.get("beneficiaryAddressLine1").toString():"");
		result.addStringParam("beneficiaryAddressLine2", inputParams.containsKey("beneficiaryAddressLine2") && inputParams.get("beneficiaryAddressLine2")!=null?inputParams.get("beneficiaryAddressLine2").toString():"");
		result.addStringParam("beneficiaryCity", inputParams.containsKey("beneficiaryCity") && inputParams.get("beneficiaryCity")!=null?inputParams.get("beneficiaryCity").toString():"");
		result.addStringParam("beneficiaryZipcode",inputParams.containsKey("beneficiaryZipcode") && inputParams.get("beneficiaryZipcode")!=null? inputParams.get("beneficiaryZipcode").toString():"");
		result.addStringParam("beneficiarycountry",inputParams.containsKey("beneficiarycountry") && inputParams.get("beneficiarycountry")!=null? inputParams.get("beneficiarycountry").toString():"");
		//Record recordSubSet = new Record();
		/*
		 * recordSubSet.setId("messageDetails"); recordSubSet.addStringParam("id",
		 * "PI-CHARGE.BEARER.NOT.ALLOWED"); recordSubSet.addStringParam("message",
		 * "Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT");
		 * result.addRecord(recordSubSet); result.addParam(new Param("messageDetails1",
		 * "[{\"id\":\"PI-CHARGE.BEARER.NOT.ALLOWED\",\"message\":\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\"}]"
		 * )); //result.addStringParam("messageDetails2",
		 * "[{"id":\"PI-CHARGE.BEARER.NOT.ALLOWED\",\"message\":\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\"}]"
		 * );
		 */ 
		return result;
	}
	private Result stubValidateResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", "12345");
        result.addStringParam("status", "success");
        result.addStringParam("totalAmount",inputParams.get("amount").toString());
        result.addStringParam("charges", "");
        //result.addStringParam("messageDetails", [{\"id\":\"PI-CHARGE.BEARER.NOT.ALLOWED\",\"message\":\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\"}]");
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
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		
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
			InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
			internationalFundTransferDTO = internationalFundTransferDelegate.updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
			if(internationalFundTransferDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			JSONObject internationalFundTransferJSON = new JSONObject(internationalFundTransferDTO);
			result = JSONToResult.convert(internationalFundTransferJSON.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking internationfundtransfertransaction business delegate",exp).log();
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
    	
    	InternationalFundTransferBusinessDelegate internationfundtransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
    	
    	if(referenceId != null) {
    		status = TransactionStatusEnum.EXECUTED;
    		confirmationNumber = referenceId.getValue();
    	}
    	else {
    		status = TransactionStatusEnum.FAILED;
    	}
    	inputParams.put("status", status.getStatus());
    	internationfundtransferBusinessDelegate.updateStatusUsingTransactionId(transactionId, status.getStatus(), confirmationNumber);
    	
    	result.addParam(new Param("dbxtransactionId", transactionId));
    	*/
    	return result;
    }
    
    @Override
    public Result editTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
    		DataControllerResponse response) {

    	@SuppressWarnings("unchecked")
    	Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];

    	InternationalFundTransferBusinessDelegate internationalTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
    	InternationalFundTransferBackendDelegate internationalfundBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InternationalFundTransferBackendDelegate.class);
    	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
    	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
    	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);

    	String featureActionId = FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE;
    	InternationalFundTransferDTO internationalDTO = null;
    	Result result = new Result();
    	String confirmationNumber = null;

    	Map<String, Object> customer = CustomerSession.getCustomerMap(request);

    	String createdby = CustomerSession.getCustomerId(customer);
    	String legalEntityId = (String) customer.get("legalEntityId");
    	boolean isBusinessUser = CustomerSession.IsBusinessUser(customer);

    	Object transactionIdParam = inputParams.get("confirmationNumber");
    	Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");

    	String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
    	CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
    	String contractId = account.getContractId();
    	String coreCustomerId = account.getCoreCustomerId();
    	String companyId = account.getOrganizationId();

    	if(confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
    		confirmationNumber = confirmationNumberParam.toString();
    		internationalDTO = internationalTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", Arrays.asList(companyId), createdby , legalEntityId);
    		if(internationalDTO == null) {
    			internationalDTO = internationalfundBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(internationalDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}else{
				    internationalDTO.setCompanyId(companyId);
				    internationalDTO.setCreatedby(createdby);
                    internationalDTO.setFeatureActionId(featureActionId);
                    if (StringUtils.isNotBlank(inputParams.get("amount").toString())) {
                        internationalDTO.setAmount(Double.parseDouble(inputParams.get("amount").toString()));
                        internationalDTO.setTransactionAmount(inputParams.get("amount").toString());
                    }
				    internationalDTO = internationalTransactionDelegate.createTransactionAtDBX(internationalDTO);
				}
			}
    	}
    	else {
    		alert.prepareError("confirmationNumber is missing in the payload which is mandatory for edit").log();
    		return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
    	}

    	String transactionId = internationalDTO.getTransactionId();
    	String oldRequestId = internationalDTO.getRequestId();

    	//Authorization checks on transcationId
    	/*
    	if(internationalDTO.getCompanyId() != null && ! internationalDTO.getCompanyId().equals(companyId)) {
    		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	}
    	else if(!isBusinessUser && ! CustomerSession.IsCombinedUser(customer) && ! internationalDTO.getCreatedby().equals(createdby)) {
    		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	}*/

    	//Authorization checks on prevfromAccountNumber and featureactionId
    	String prevfromAccountNumber = internationalDTO.getFromAccountNumber();

    	if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
    		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	}

    	InternationalFundTransferDTO requestDTO = null;
    	inputParams.put("createdby", createdby);
    	inputParams.put("companyId", companyId);
    	inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));

    	try {
    		requestDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalFundTransferDTO.class);
    		requestDTO.setConfirmationNumber(null);
    		requestDTO.setTransactionId(null);
    		internationalDTO = internationalDTO.updateValues(requestDTO);
    	} catch (IOException e) {
    		alert.prepareError("Error occured while fetching the input params: " , e).log();
    		return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
    	}

    	if(internationalDTO == null) {
    		alert.prepareError("Error occured while fetching the input params: " ).log();
    		return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
    	}

    	//Authorization checks on newfromAccountNumber and featureactionId
    	String newfromAccountNumber = internationalDTO.getFromAccountNumber();
    	if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, newfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
    		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	}

    	String date = internationalDTO.getScheduledDate() == null ? 
    			(internationalDTO.getProcessingDate() == null ? 
    					(internationalDTO.getFrequencyStartDate() == null ? 
    							application.getServerTimeStamp()
    							: internationalDTO.getFrequencyStartDate())
    					: internationalDTO.getProcessingDate()) 
    			: internationalDTO.getScheduledDate();

    	//Transaction limit checks on amount and featureactionId
    	Double amount = internationalDTO.getAmount();
    	String transactionAmount = inputParams.get("transactionAmount") != null ? inputParams.get("transactionAmount").toString() : null;
    	String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;

    	if("0".equals(transactionAmount) || "0.0".equals(transactionAmount) || "0.00".equals(transactionAmount)) {
    		transactionAmount = amount + "";
    	}

    	String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();

    	if("true".equalsIgnoreCase(validate)) {
    		InternationalFundTransferBackendDTO internationalFundTransferBackendDTO = new InternationalFundTransferBackendDTO();
    		internationalFundTransferBackendDTO =  internationalFundTransferBackendDTO.convert(internationalDTO);
    		InternationalFundTransferDTO validateDTO = internationalfundBackendDelegate.validateTransaction(internationalFundTransferBackendDTO,request);
    		try {
    			result = JSONToResult.convert(new JSONObject(validateDTO).toString());
    			return result;
    		} catch (JSONException e) {
    			alert.prepareError("Error occured while converting the response from Line of Business service for international fund transfer: ", e).log();
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
    	transactionStatusDTO.setTransactionCurrency(internationalDTO.getTransactionCurrency());
    	transactionStatusDTO.setFeatureActionID(featureActionId);
    	transactionStatusDTO.setServiceCharge(internationalDTO.getServiceCharge());
    	transactionStatusDTO.setRequestId(oldRequestId);
    	
    	transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
    	if(transactionStatusDTO == null) {			
    		return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
    	}
    	if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
    		result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
    		result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
    		return result;
    	}

    	//these things should come from limits Engine after proper integration with validate call

    	TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();

    	internationalDTO.setStatus(transactionStatus.getStatus());
    	//internationalDTO.setRequestId(null);
    	internationalDTO.setConfirmationNumber(confirmationNumber+"");
		internationalDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		try {
			internationalDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		internationalDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());

    	InternationalFundTransferBackendDTO internationalBackendDTO = new InternationalFundTransferBackendDTO();
    	internationalBackendDTO = internationalBackendDTO.convert(internationalDTO);

    	try {
    		internationalBackendDTO.setAmount(Double.parseDouble(internationalBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
    	
    	String requestObj = null;
    	try {
    		requestObj = new JSONObject(internationalBackendDTO).toString();
    		result = JSONToResult.convert(requestObj);
    	} catch (JSONException e) {
    		alert.prepareError("Error occured while converting the response from Line of Business service for internationalFund transfer: ", e).log();
    		return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
    	}

    	String referenceId = null;

        String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
        internationalBackendDTO.setOverrides(overrides);

    	InternationalFundTransferDTO internationalTransferDTO = null;
    	if (transactionStatus == TransactionStatusEnum.SENT) {

    		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
    			internationalBackendDTO.setTransactionId(confirmationNumber);
    			internationalTransferDTO = internationalfundBackendDelegate.editTransactionWithoutApproval(internationalBackendDTO, request);
    		}

    		if(internationalTransferDTO == null) {
    			return ErrorCodeEnum.ERR_12600.setErrorCode(result);
    		}

    		if(internationalTransferDTO.getDbpErrCode() != null || internationalTransferDTO.getDbpErrMsg() != null) {
    			internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			result.addParam(new Param("errorDetails", internationalTransferDTO.getErrorDetails()));
    			return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalTransferDTO.getDbpErrMsg());  
    		}
    		
    		referenceId = internationalTransferDTO.getReferenceId();

			if(StringUtils.isEmpty(referenceId)) {
    			internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			return ErrorCodeEnum.ERR_12602.setErrorCode(result);
    		}
    		
    		internationalDTO = internationalTransferDTO;
    		internationalDTO.setConfirmationNumber(referenceId);
    		internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), internationalDTO.getReferenceId());
    		
    		result.addParam(new Param("referenceId", internationalTransferDTO.getReferenceId()));
    		result.addParam(new Param("status", transactionStatus.getStatus()));
    		result.addParam(new Param("message", transactionStatus.getMessage()));
    	}
    	else if(transactionStatus == TransactionStatusEnum.PENDING){
    		
    		String requestId = transactionStatusDTO.getRequestId();
    		referenceId = confirmationNumber;
    		internationalBackendDTO.setTransactionId(confirmationNumber);
    		InternationalFundTransferDTO pendingtransactionDTO = internationalfundBackendDelegate.editTransactionWithApproval(internationalBackendDTO, request);
    		if(pendingtransactionDTO == null)
    		{
    			internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			alert.prepareError("Error occured while creating entry into the backend table: ").log();
    			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
    		}
    		if(pendingtransactionDTO.getDbpErrCode() != null || pendingtransactionDTO.getDbpErrMsg() != null) {
    			internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			result.addParam(new Param("errorDetails", pendingtransactionDTO.getErrorDetails()));
    			return ErrorCodeEnum.ERR_00000.setErrorCode(result, pendingtransactionDTO.getDbpErrMsg());
    		}
    		
    		String backendid = pendingtransactionDTO.getReferenceId();
            internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
            transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, backendid, transactionStatusDTO.isSelfApproved(), featureActionId, request);
            if(transactionStatusDTO == null) 
			{							
				internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), transactionId);
				return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
			}
			
			if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				internationalFundTransferDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), transactionId);
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
			transactionStatus=transactionStatusDTO.getStatus();
    		internationalTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
    		internationalDTO.setConfirmationNumber(backendid);
    		internationalDTO.setStatus(transactionStatus.toString());
    		internationalDTO.setRequestId(requestId);
    		if(pendingtransactionDTO.getMessageDetails() != null) {
                result.addParam(new Param("messageDetails", pendingtransactionDTO.getMessageDetails()));
            }
    		
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

    	}
    	else if(transactionStatus == TransactionStatusEnum.APPROVED){
    		result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
    		result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
    		result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
    	}
    	if(StringUtils.isNotEmpty(referenceId)) {
    		internationalDTO.setTransactionId(transactionId);
    		internationalTransactionDelegate.updateTransactionAtDBX(internationalDTO);
    	}
    	
    	if(internationalDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", internationalDTO.getMessageDetails()));
        }
        
    	if(internationalDTO.getQuoteCurrency() != null) {
            result.addParam(new Param("quoteCurrency", internationalDTO.getQuoteCurrency()));
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
		
		String featureActionId = FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL;
		String requestId = "";
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		InternationalFundTransferDTO internationalDTO = null;
		
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
			internationalDTO = internationalFundTransferDelegate.fetchExecutedTranscationEntry(confirmationNumber, contracts, createdby , null);
			if(internationalDTO == null) {
				internationalDTO = internationalfundBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(internationalDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
				internationalDTO.setFeatureActionId(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE);
				internationalDTO.setCreatedby(createdby);
				CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, internationalDTO.getFromAccountNumber());
				String companyId = account.getOrganizationId();
				internationalDTO.setCompanyId(companyId);
				String transactionId = internationalDTO.getTransactionId();
				internationalDTO.setConfirmationNumber(transactionId);
				internationalDTO.setTransactionId(null);
				InternationalFundTransferDTO dbxDTO = internationalFundTransferDelegate.createTransactionAtDBX(internationalDTO);
				internationalDTO.setTransactionId(transactionId);
				if(dbxDTO != null) {
					internationalDTO.setTransactionId(dbxDTO.getTransactionId());
					internationalDTO.setRequestId(dbxDTO.getRequestId());
				}
			}
		}
		else {
			alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for cancel occurrence").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		
		String oldrequestId = internationalDTO.getRequestId();
		String transactionId = internationalDTO.getTransactionId();
		
		//Authorization checks on transcationId
		/*
		if(internationalDTO.getCompanyId() != null && ! contracts.contains(internationalDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! internationalDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = internationalDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}

		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(internationalDTO.getCompanyId());
		transactionStatusDTO.setAccountId(internationalDTO.getFromAccountNumber());
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setRequestId(oldrequestId);
		transactionStatusDTO.setAmount(internationalDTO.getAmount());
		transactionStatusDTO.setServiceCharge(internationalDTO.getServiceCharge());
		transactionStatusDTO.setTransactionCurrency(internationalDTO.getTransactionCurrency());
		
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
			InternationalFundTransferDTO internationaltransactionDTO = new InternationalFundTransferDTO();
			internationaltransactionDTO = internationalfundBackendDelegate.cancelTransactionWithoutApproval(confirmationNumber, request);					
			if(internationaltransactionDTO == null) {	
				return ErrorCodeEnum.ERR_12600.setErrorCode(result);
			}
			if(internationaltransactionDTO.getDbpErrCode() != null || internationaltransactionDTO.getDbpErrMsg() != null) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationaltransactionDTO.getDbpErrMsg());
			}
			String referenceId = internationaltransactionDTO.getReferenceId();
			if(referenceId == null || "".equals(referenceId)) {
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			result.addParam(new Param("referenceId", referenceId));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestId = transactionStatusDTO.getRequestId();
			InternationalFundTransferDTO internationalpendingtransactionDTO = new InternationalFundTransferDTO();
			internationalpendingtransactionDTO = internationalFundTransferDelegate.cancelTransactionWithApproval(confirmationNumber, transactionId, request);					
			if(internationalpendingtransactionDTO == null)
			{
				alert.prepareError("Error occured while creating entry into the backend table: ").log();
				return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
			}
			if(internationalpendingtransactionDTO.getDbpErrCode() != null || internationalpendingtransactionDTO.getDbpErrMsg() != null) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalpendingtransactionDTO.getDbpErrMsg());
			}
			confirmationNumber = internationalpendingtransactionDTO.getReferenceId();
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
			internationalFundTransferDelegate.updateRequestId(transactionId, requestId);
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
		
		result.addParam(new Param("transactionId", confirmationNumber));
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
	
	@Override
	public Result deleteTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		String featureActionId = FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL;
		String requestId = "";
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");	
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		InternationalFundTransferDTO internationalDTO = null;
		
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if (PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
			return stubDeleteTransactionResponse();
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];

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
			internationalDTO = internationalFundTransferDelegate.fetchExecutedTranscationEntry(confirmationNumber, contracts, createdby , legalEntityId);
			if(internationalDTO == null) {
				internationalDTO = internationalfundBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(internationalDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
				internationalDTO.setFeatureActionId(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE);
				internationalDTO.setCreatedby(createdby);
				CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, internationalDTO.getFromAccountNumber());
				String companyId = account.getOrganizationId();
				internationalDTO.setCompanyId(companyId);
				transactionId = internationalDTO.getTransactionId();
				internationalDTO.setConfirmationNumber(transactionId);
				internationalDTO.setTransactionId(null);
				InternationalFundTransferDTO dbxDTO = internationalFundTransferDelegate.createTransactionAtDBX(internationalDTO);
				internationalDTO.setTransactionId(transactionId);
				if(dbxDTO != null) {
					internationalDTO.setTransactionId(dbxDTO.getTransactionId());
					internationalDTO.setRequestId(dbxDTO.getRequestId());
				}
			}
		}
		else {
			alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for delete").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(result);
		}
		
		transactionId = internationalDTO.getTransactionId();
		transactionType = internationalDTO.getTransactionType();
		String oldrequestId = internationalDTO.getRequestId();
		
		
		//Authorization checks on transcationId
		/*
		if(internationalDTO.getCompanyId() != null && ! contracts.contains(internationalDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! internationalDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = internationalDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(internationalDTO.getCompanyId());
		transactionStatusDTO.setAccountId(internationalDTO.getFromAccountNumber());
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setRequestId(oldrequestId);
		transactionStatusDTO.setAmount(internationalDTO.getAmount());
		transactionStatusDTO.setServiceCharge(internationalDTO.getServiceCharge());
		transactionStatusDTO.setTransactionCurrency(internationalDTO.getTransactionCurrency());
		
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
			InternationalFundTransferDTO internationaltransactionDTO = new InternationalFundTransferDTO();
			internationaltransactionDTO = internationalfundBackendDelegate.deleteTransactionWithoutApproval(confirmationNumber, transactionType, frequencyType, request);					
			if(internationaltransactionDTO == null) {	
				return ErrorCodeEnum.ERR_12600.setErrorCode(result);
			}
			if(internationaltransactionDTO.getDbpErrCode() != null || internationaltransactionDTO.getDbpErrMsg() != null) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationaltransactionDTO.getDbpErrMsg());
			}
			String referenceId = internationaltransactionDTO.getReferenceId();
			if(referenceId == null || "".equals(referenceId)) {
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			internationalFundTransferDelegate.deleteTransactionAtDBX(transactionId);
			result.addParam(new Param("referenceId", referenceId));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestId = transactionStatusDTO.getRequestId();
			InternationalFundTransferDTO internationalpendingtransactionDTO = new InternationalFundTransferDTO();
			internationalpendingtransactionDTO = internationalFundTransferDelegate.deleteTransactionWithApproval(confirmationNumber, transactionType, frequencyType, transactionId, request);					
			if(internationalpendingtransactionDTO == null)
			{
				alert.prepareError("Error occured while creating entry into the backend table: ").log();
				return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
			}
			if(internationalpendingtransactionDTO.getDbpErrCode() != null || internationalpendingtransactionDTO.getDbpErrMsg() != null) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalpendingtransactionDTO.getDbpErrMsg());
			}
			confirmationNumber = internationalpendingtransactionDTO.getReferenceId();
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
			internationalFundTransferDelegate.updateRequestId(transactionId, requestId);
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
		inputParams.put("amount", internationalDTO.getAmount());
		inputParams.put("scheduledDate", internationalDTO.getScheduledDate());
		inputParams.put("fromAccountNumber", internationalDTO.getFromAccountNumber());
		inputParams.put("toAccountNumber", internationalDTO.getToAccountNumber());
		inputParams.put("isScheduled", internationalDTO.getIsScheduled());
		inputParams.put("Frequency", internationalDTO.getFrequencyTypeId());
		inputParams.put("NoofRecurrences", internationalDTO.getNumberOfRecurrences());
		inputParams.put("TransferDate", internationalDTO.getScheduledDate());
		inputParams.put("EndBy", internationalDTO.getFrequencyEndDate());
		
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
	 * @param internationfundtransferDTO
	 * @param referenceId
	 * @param requestId
	 */
	private void _logTransaction(DataControllerRequest request,DataControllerResponse response,Object[] inputArray,Result result, TransactionStatusEnum transactionStatus, String referenceId,InternationalFundTransferDTO internationfundtransferDTO,String requestId) {
		
		String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
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

			eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE);
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
					if (internationfundtransferDTO == null) {
						statusID = Constants.SID_EVENT_FAILURE;
					}
					if (internationfundtransferDTO.getDbpErrMsg() != null
							&& !internationfundtransferDTO.getDbpErrMsg().isEmpty()) {
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
	private Result stubDeleteTransactionResponse() {
		
		Result result = new Result();
		result.addStringParam("transactionId", String.format("%06d", new java.util.Date().getTime()%1000000));
        result.addStringParam("status", "Sent");
        result.addStringParam("message","Success! Your transaction has been completed");
        //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
        return result;

	}	
}