package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.utills.HBLUtility;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
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
import com.temenos.dbx.product.transactionservices.backenddelegate.api.IntraBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.resource.impl.IntraBankFundTransferResourceImpl;

public class IntraBankFundTransferResourceImplExtn extends IntraBankFundTransferResourceImpl {
	private static final Logger LOG = LogManager.getLogger(IntraBankFundTransferResourceImplExtn.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	IntraBankFundTransferBackendDelegate intrabankfundBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(IntraBankFundTransferBackendDelegate.class);
	
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		LOG.debug("Invalid amount value ##"+ inputParams);
		IntraBankFundTransferDTO intrabankDTO = null;
		Result result = new Result();
		Double amount = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		String featureActionId = null;
		
		String amountValue = inputParams.get("amount").toString();
		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();
		String baseCurrency  = application.getBaseCurrencyFromCache();
		String transactionCurrency = inputParams.get("transactionCurrency") != null ?inputParams.get("transactionCurrency").toString() : baseCurrency;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
											 
		if(amountValue == null || amountValue == "") {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}
		
		featureActionId = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		try {
			amount = Double.parseDouble(amountValue);
		}
		catch(NumberFormatException e) {
			LOG.error("Invalid amount value", e);
			return ErrorCodeEnum.ERR_10624.setErrorCode(new Result());
		}
		
		fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		inputParams.put("featureActionId", featureActionId);
		inputParams.put("companyId", companyId);
		inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		inputParams.put("createdby", createdby);
		
		try {
			intrabankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IntraBankFundTransferDTO.class);
		} catch (IOException e) {
			LOG.error("Error occured while fetching the input params: ", e);
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		
		String date = intrabankDTO.getScheduledDate() == null ? 
				(intrabankDTO.getProcessingDate() == null ? 
						(intrabankDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: intrabankDTO.getFrequencyStartDate())
						: intrabankDTO.getProcessingDate()) 
				: intrabankDTO.getScheduledDate();
								
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String beneficiaryId = inputParams.get("beneficiaryId") == null ? null : inputParams.get("beneficiaryId").toString();
		String beneficiaryName = inputParams.get("beneficiaryName") == null ? null : inputParams.get("beneficiaryName").toString();
		String paidBy = inputParams.get("paidBy") == null ? null : inputParams.get("paidBy").toString();
		String frequencyType = inputParams.get("frequencyType") == null ? null : inputParams.get("frequencyType").toString();
		String requestid = "";

		if("true".equalsIgnoreCase(validate)) {
			
			IntraBankFundTransferBackendDTO intraBankFundTransferBackendDTO = new IntraBankFundTransferBackendDTO();
			intraBankFundTransferBackendDTO = intraBankFundTransferBackendDTO.convert(intrabankDTO);
			
			IntraBankFundTransferDTO validateintrabankDTO = intrabankfundBackendDelegate.validateTransaction(intraBankFundTransferBackendDTO,request);
			try {
				if(validateintrabankDTO!=null && validateintrabankDTO.getDbpErrCode() == null  && validateintrabankDTO.getReferenceId() != null  && validateintrabankDTO.getExchangeRate()!=null) {
					Double exchangeRate = Double.parseDouble(validateintrabankDTO.getExchangeRate());
					LOG.debug("IntraBankFundTransferResourceImplExtn validate flow: exchangeRate:"+ exchangeRate);
					String fromAccountCurrency= intraBankFundTransferBackendDTO.getFromAccountCurrency();
					String toAccountCurrency= intraBankFundTransferBackendDTO.getToAccountCurrency();
					Double convertedAmount;
					if(StringUtils.isNotBlank(transactionCurrency) && StringUtils.isNotBlank(fromAccountCurrency)) {
						if (transactionCurrency.equalsIgnoreCase(fromAccountCurrency) && transactionCurrency.equalsIgnoreCase("NPR")) {
							validateintrabankDTO.setTotalAmount(amount.toString());
							validateintrabankDTO.setConvertedAmount(amount.toString());
						} 
						else if (transactionCurrency.equalsIgnoreCase(fromAccountCurrency) && !transactionCurrency.equalsIgnoreCase("NPR")) {
							Double equilentNPRAmount = exchangeRate * amount;
							equilentNPRAmount = (double) Math.round(equilentNPRAmount * 100.0) / 100.0;
							validateintrabankDTO.setTotalAmount(equilentNPRAmount.toString());
							validateintrabankDTO.setConvertedAmount(amount.toString());
						} 
						else if (transactionCurrency.equalsIgnoreCase("NPR") && !fromAccountCurrency.equalsIgnoreCase("NPR")) {
							convertedAmount = amount / exchangeRate;
							convertedAmount = (double) Math.round(convertedAmount * 100.0) / 100.0;
							validateintrabankDTO.setConvertedAmount(convertedAmount.toString());
							validateintrabankDTO.setTotalAmount(amount.toString());
						} 
						else {
							convertedAmount = amount / exchangeRate;
							convertedAmount = (double) Math.round(convertedAmount * 100.0) / 100.0;
							Double equilentNPRAmount= exchangeRate * amount;
							equilentNPRAmount = (double) Math.round(equilentNPRAmount * 100.0) / 100.0;
							validateintrabankDTO.setConvertedAmount(convertedAmount.toString());
							validateintrabankDTO.setTotalAmount(equilentNPRAmount.toString());
						}
						
					}
				}
				else if(validateintrabankDTO!=null && validateintrabankDTO.getDbpErrCode() == null  && validateintrabankDTO.getReferenceId() != null  && validateintrabankDTO.getExchangeRate()==null) {
					validateintrabankDTO.setTotalAmount(amount.toString());
					validateintrabankDTO.setConvertedAmount(amount.toString());
				}
				 result = JSONToResult.convert(new JSONObject(validateintrabankDTO).toString());
				 return result;
			} catch (JSONException e) {
				LOG.error("Error occured while converting the response from Line of Business service for intrabank transfer: ", e);
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

		intrabankDTO.setStatus(transactionStatus.getStatus());
		intrabankDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		try {
			intrabankDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		} catch (NumberFormatException e) {
			LOG.error("Invalid amount value", e);
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		intrabankDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		//intrabankDTO.setTransactionId(null);
		intrabankDTO.setRequestId(transactionStatusDTO.getRequestId());
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		intrabankDTO.setConfirmationNumber(confirmationNumber);
		intrabankDTO.setLegalEntityId(legalEntityId);
		String channel="";
		try {
		UserAgentUtil ua = new UserAgentUtil(request);
		channel=ua.getChannel();
		}catch (Exception e) {
			return ErrorCodeEnum.ERR_10163.setErrorCode(new Result());
		}
		LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::channel:"+channel);
		if(channel.equalsIgnoreCase("desktop")) {
			channel=HBLConstants.ONLINE_BANKING;
		}else if(channel.equalsIgnoreCase("mobile")){
			channel=HBLConstants.MOBILE_BANKING;
		}
		intrabankDTO.setPaidBy(channel);
		
		IntraBankFundTransferDTO intrabankdbxDTO = intrabankTransactionDelegate.createTransactionAtDBX(intrabankDTO);
		if(intrabankdbxDTO == null) {
			LOG.error("Error occured while creating entry into the DBX table: ");
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if(intrabankdbxDTO.getDbpErrCode() != null || intrabankdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", intrabankdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabankdbxDTO.getDbpErrMsg()));
			return result;
		}
		
		intrabankdbxDTO.setValidate(validate);
		intrabankdbxDTO.setPaidBy(paidBy);
		
		IntraBankFundTransferBackendDTO intrabankBackendDTO = new IntraBankFundTransferBackendDTO();
		intrabankBackendDTO = intrabankBackendDTO.convert(intrabankdbxDTO);
		
		String creditValueDate = inputParams.get("creditValueDate") == null ? "" : inputParams.get("creditValueDate").toString(); 
        String totalAmount = inputParams.get("totalAmount") == null ? "" : inputParams.get("totalAmount").toString();
        String exchangeRate = inputParams.get("exchangeRate") == null ? "" : inputParams.get("exchangeRate").toString();
        String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
        String beneficiaryBankName = inputParams.get("beneficiaryBankName") == null ? "" : inputParams.get("beneficiaryBankName").toString();
        String beneficiaryAddressLine1 = inputParams.get("beneficiaryAddressLine1") == null ? "" : inputParams.get("beneficiaryAddressLine1").toString();
        String beneficiaryAddressLine2 = inputParams.get("beneficiaryAddressLine2") == null ? "" : inputParams.get("beneficiaryAddressLine2").toString();
        String beneficiaryPhone = inputParams.get("beneficiaryPhone") == null ? "" : inputParams.get("beneficiaryPhone").toString();
        String beneficiaryEmail = inputParams.get("beneficiaryEmail") == null ? "" : inputParams.get("beneficiaryEmail").toString();
        String beneficiaryState = inputParams.get("beneficiaryState") == null ? "" : inputParams.get("beneficiaryState").toString();
        String beneficiaryCity = inputParams.get("beneficiaryCity") == null ? "" : inputParams.get("beneficiaryCity").toString();
        String beneficiaryZipcode = inputParams.get("beneficiaryZipcode") == null ? "" : inputParams.get("beneficiaryZipcode").toString();
        String beneficiarycountry = inputParams.get("beneficiarycountry") == null ? "" : inputParams.get("beneficiarycountry").toString();
       
        intrabankBackendDTO.setCreditValueDate(creditValueDate);
        intrabankBackendDTO.setTotalAmount(totalAmount);
        intrabankBackendDTO.setExchangeRate(exchangeRate);
		intrabankBackendDTO.setBeneficiaryId(beneficiaryId);
        intrabankBackendDTO.setBeneficiaryName(beneficiaryName);
        intrabankBackendDTO.setOverrides(overrides);
        intrabankBackendDTO.setBeneficiaryBankName(beneficiaryBankName);
        intrabankBackendDTO.setBeneficiaryAddressLine2(beneficiaryAddressLine2);
        intrabankBackendDTO.setBeneficiaryPhone(beneficiaryPhone);
        intrabankBackendDTO.setBeneficiaryEmail(beneficiaryEmail);
        intrabankBackendDTO.setBeneficiaryState(beneficiaryState);
        intrabankBackendDTO.setBeneficiaryAddressLine1(beneficiaryAddressLine1);
        intrabankBackendDTO.setBeneficiaryCity(beneficiaryCity);
        intrabankBackendDTO.setBeneficiarycountry(beneficiarycountry);
        intrabankBackendDTO.setBeneficiaryZipcode(beneficiaryZipcode);
		
        try {
        	intrabankBackendDTO.setAmount(Double.parseDouble(intrabankBackendDTO.getTransactionAmount()));
        	intrabankdbxDTO.setAmount(Double.parseDouble(intrabankBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			LOG.error("Invalid amount value", e);
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
        
		try {
			String responseObj = new JSONObject(intrabankBackendDTO).toString();
			result = JSONToResult.convert(responseObj);
		} catch (JSONException e) {
			LOG.error("Error occured while converting the response from Line of Business service for intrabank transfer: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		IntraBankFundTransferDTO intrabanktransactionDTO = new IntraBankFundTransferDTO();
		String transactionid = intrabankdbxDTO.getTransactionId();
        String createWithPaymentId = inputParams.get("createWithPaymentId") == null ? ""
                : inputParams.get("createWithPaymentId").toString();
		
		if(transactionStatus == TransactionStatusEnum.SENT ) {
            if (StringUtils.isEmpty(backendid)
                    || (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true"))) {
                if(StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true")){
                    intrabankBackendDTO.setTransactionId(backendid);
                    String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
                    intrabankBackendDTO.setCharges(charges);
                }else {
                    intrabankBackendDTO.setTransactionId(null);
                }
				intrabanktransactionDTO = intrabankfundBackendDelegate.createTransactionWithoutApproval(intrabankBackendDTO, request);					
				if(intrabanktransactionDTO == null) {	
					intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12601.setErrorCode(result); 
				}
			}
			else {
				String frequency = StringUtils.isEmpty(intrabankDTO.getFrequencyTypeId()) ? null : intrabankDTO.getFrequencyTypeId();
				intrabanktransactionDTO  = intrabankTransactionDelegate.approveTransaction(backendid, request,frequency);
				if(intrabanktransactionDTO == null) {	
					intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			if(intrabanktransactionDTO.getDbpErrCode() != null || intrabanktransactionDTO.getDbpErrMsg() != null) {
				intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				result.addParam(new Param("errorDetails", intrabanktransactionDTO.getErrorDetails()));
                return ErrorCodeEnum.ERR_00000.setErrorCode(result, intrabanktransactionDTO.getDbpErrMsg());
			}
			if(intrabanktransactionDTO.getReferenceId() == null || "".equals(intrabanktransactionDTO.getReferenceId())) {
				intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			intrabankDTO=intrabanktransactionDTO;
			intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.EXECUTED.getStatus(), intrabanktransactionDTO.getReferenceId());
			result.addParam(new Param("referenceId", intrabanktransactionDTO.getReferenceId()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
	        result.addParam(new Param("paymentId", intrabanktransactionDTO.getPaymentId()));
	        result.addParam(new Param("transactionId", transactionid));
	        LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::paymentId:"+intrabanktransactionDTO.getPaymentId());
	        LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::transactionId:"+transactionid);
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			intrabankdbxDTO.setCreditValueDate(creditValueDate);
			intrabankdbxDTO.setTotalAmount(totalAmount);
			intrabankdbxDTO.setExchangeRate(exchangeRate);
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId = null;
			if (StringUtils.isEmpty(backendid)
                    || (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true"))) {
			    if(StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true")){
			        intrabankdbxDTO.setTransactionId(backendid);
			        String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
			        intrabankdbxDTO.setCharges(charges);
			    } 
				IntraBankFundTransferDTO intrabankpendingtransactionDTO = intrabankTransactionDelegate.createPendingTransaction(intrabankdbxDTO, request);
				if(intrabankpendingtransactionDTO == null)
				{
					intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					LOG.error("Error occured while creating entry into the backend table: ");
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(intrabankpendingtransactionDTO.getDbpErrCode() != null || intrabankpendingtransactionDTO.getDbpErrMsg() != null) {
					intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					result.addParam(new Param("errorDetails", intrabankpendingtransactionDTO.getErrorDetails()));
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, intrabankpendingtransactionDTO.getDbpErrMsg());
				}
				backendid = intrabankpendingtransactionDTO.getReferenceId();
				intrabankDTO=intrabankpendingtransactionDTO;
			}
				pendingrefId = backendid;
				intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					intrabankfundBackendDelegate.deleteTransactionWithoutApproval(backendid, null, frequencyType, request);
					intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					intrabankfundBackendDelegate.deleteTransactionWithoutApproval(backendid, null, frequencyType, request);
					intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
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
				intrabankTransactionDelegate.updateStatusUsingTransactionId(transactionid, transactionStatus.toString(), pendingrefId);
			}
			
			//code snippet being added for alerts on intra transfer
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null,  
						backendid, requestid, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				LOG.error("Failed at pushAlertsForApprovalRequests "+e);
			}
		}

		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		
		if(intrabankDTO.getOverrides() != null) {
			result.addParam(new Param("overrides",intrabankDTO.getOverrides()));
		}
		if(intrabankDTO.getOverrideList() != null) {
			result.addParam(new Param("overrideList",intrabankDTO.getOverrideList()));
		}
		if(intrabankDTO.getCharges() != null) {
			result.addParam(new Param("charges",intrabankDTO.getCharges()));
		}
		if(intrabankDTO.getExchangeRate() != null) {
			result.addParam(new Param("exchangeRate",intrabankDTO.getExchangeRate()));
		}
		if(intrabankDTO.getTotalAmount() != null) {
			result.addParam(new Param("totalAmount",intrabankDTO.getTotalAmount()));
		}	
		if(intrabankDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", intrabankDTO.getMessageDetails()));
        }
		if(intrabankDTO.getQuoteCurrency() != null) {
            result.addParam(new Param("quoteCurrency", intrabankDTO.getQuoteCurrency()));
        }
		
		try {
			_logTransaction(request,response,inputArray,result,transactionStatus,transactionStatusDTO.getConfirmationNumber(),intrabankdbxDTO,requestid);
		} catch(Exception e) {
			LOG.error("Error occured while audit logging.",e);
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			LOG.error(e);
		}
		return result;
	}
private void _logTransaction(DataControllerRequest request,DataControllerResponse response,Object[] inputArray,Result result, TransactionStatusEnum transactionStatus, String referenceId,IntraBankFundTransferDTO intrabankDTO,String requestId) {
		
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
					FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE);
			List<Param> params = result.getAllParams();
			for (Param param : params) {
				if (request.containsKeyInRequest(param.getName())) {
					continue;
				} else {
					customParams.addProperty(param.getName(), param.getValue());
				}
			}
			customParams.addProperty("FirstName", customer.get("FullName")!=null?customer.get("FullName").toString():"");
			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);
			String benefeciaryName= request.getParameter("beneficiaryName"); 
			customParams.addProperty("toAccountName", benefeciaryName);
			customParams.addProperty("bankName", "Himalayan Bank Limited");
			customParams.addProperty("customerName", customer.get("FullName")!=null?customer.get("FullName").toString():"");
			customParams.addProperty("toAccountNumber", maskAccountNumber(toAccountNumber, 0, toAccountNumber.length()-4, 'X'));
			customParams.addProperty("fromAccountNumber", maskAccountNumber(fromAccountNumber, 0, toAccountNumber.length()-4, 'X'));
			String scheduledDate= result.getParamValueByName("scheduledDate");
			String status="";
			if(scheduledDate!=null)
			customParams.addProperty("scheduledDate", scheduledDate.length()>10?scheduledDate.substring(0, 10):scheduledDate);
			String amount= result.getParamValueByName(Constants.AMOUNT);
			amount=formatAmount(amount);
			String transactionCurrency= request.getParameter("transactionCurrency"); 
			if(StringUtils.isNotBlank(transactionCurrency))
			customParams.addProperty(Constants.AMOUNT, transactionCurrency+ " "+ amount);
			if (transactionStatus.toString().contains("DENIED")) {
				statusID = Constants.SID_EVENT_FAILURE;
				customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
			} else {
				switch (transactionStatus) {
				case SENT:
					referenceId = customParams.get("referenceId").getAsString();
					if (intrabankDTO == null) {
						statusID = Constants.SID_EVENT_FAILURE;
						status= Constants.STATUS_FAIL.toLowerCase();;
					}
					if (intrabankDTO.getDbpErrMsg() != null && !intrabankDTO.getDbpErrMsg().isEmpty()) {
						statusID = Constants.SID_EVENT_FAILURE;
						status= Constants.STATUS_FAIL.toLowerCase();;
					}
					if (referenceId == null || "".equals(referenceId)) {
						statusID = Constants.SID_EVENT_FAILURE;
						status= Constants.STATUS_FAIL.toLowerCase();;
					} else {
						statusID = Constants.SID_EVENT_SUCCESS;
						customParams.addProperty(Constants.REFERENCEID, referenceId);
						status= Constants.STATUS_SUCCESS.toLowerCase();
						if (isScheduled.equals("1")) 
							status= "scheduled";
						if (isSMEUser) {
							customParams.addProperty(Constants.APPROVERS, "Pre-Approved");
							customParams.addProperty("approvedBy", "Pre-Approved");
						}
					}
					customParams.addProperty("status", status);
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
			
			Result result1 = EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null,
                    CustomerSession.getCustomerId(customer), null,  customParams);
			LOG.debug("Log result1:"+ResultToJSON.convert(result1));
		} catch (Exception e) {
			LOG.error("Error while pushing to Audit Engine.");
		}
	}
public String formatAmount(String amount){
	 if(StringUtils.isNotBlank(amount)) {
	try {
	 BigDecimal decimalAmount= new BigDecimal(amount).setScale(2);
     amount=String.valueOf(decimalAmount);
	 }catch (Exception e) {
		 LOG.error("Exception Occured in formatAmount:",e);
	}
	 }
	 return amount;
}
public String maskAccountNumber(String data, int fromIndex, int toIndex, char maskWith) {
    StringBuilder maskedPart = new StringBuilder();
    if(StringUtils.isNotBlank(data)) {
    for(int i=fromIndex; i<toIndex; i++)
        maskedPart.append(maskWith);
    return data.replace(data.substring(fromIndex, toIndex), maskedPart.toString());
    }
    return data;
}


}
