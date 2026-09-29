package com.hbl.productservicesExtn.impl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.utills.HBLUtility;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
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
import com.temenos.dbx.product.transactionservices.resource.impl.OwnAccountFundTransferResourceImpl;

import eu.bitwalker.useragentutils.UserAgent;

public class OwnAccountFundTransferResourceImplExtn extends OwnAccountFundTransferResourceImpl{

		private static final Logger LOG = LogManager.getLogger(OwnAccountFundTransferResourceImplExtn.class);
		CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
		ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
		TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
		OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		OwnAccountFundTransferBackendDelegate ownAccFundTransferBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(OwnAccountFundTransferBackendDelegate.class);
	    @Override
		public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
				DataControllerResponse response) {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
			LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::createTransaction:");
			OwnAccountFundTransferDTO ownaccountDTO = null;
			Result result = new Result();
			Double amount = null;
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String createdby = CustomerSession.getCustomerId(customer);
			String legalEntityId = (String) customer.get("legalEntityId");
			String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
			CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
			String contractId = account.getContractId();
			String coreCustomerId = account.getCoreCustomerId();
			String companyId = account.getOrganizationId();
			
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
			String baseCurrency  = application.getBaseCurrencyFromCache();
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
				LOG.error("Invalid amount value", e);
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
				LOG.error("Error occured while fetching the input params: " , e);
				return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());			
			}
			
			String date = ownaccountDTO.getScheduledDate() == null ? 
					(ownaccountDTO.getProcessingDate() == null ? 
							(ownaccountDTO.getFrequencyStartDate() == null ? 
									application.getServerTimeStamp()
									: ownaccountDTO.getFrequencyStartDate())
							: ownaccountDTO.getProcessingDate()) 
					: ownaccountDTO.getScheduledDate();
									
			String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
			String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
			String beneficiaryId = inputParams.get("beneficiaryId") == null ? null : inputParams.get("beneficiaryId").toString();
			String requestid = "";
		
			if("true".equalsIgnoreCase(validate)) {
				OwnAccountFundTransferBackendDTO backendObj = new OwnAccountFundTransferBackendDTO();
				backendObj =  backendObj.convert(ownaccountDTO);
				OwnAccountFundTransferDTO validateownaccountDTO = ownAccFundTransferBackendDelegate.validateTransaction(backendObj,request);
				try {
					if(validateownaccountDTO!=null && validateownaccountDTO.getDbpErrCode() == null  && validateownaccountDTO.getReferenceId() != null  && validateownaccountDTO.getExchangeRate()!=null) {
						Double exchangeRate = Double.parseDouble(validateownaccountDTO.getExchangeRate());
						LOG.debug("OwnAccountFundTransferResourceImplExtn validate flow: exchangeRate:"+ exchangeRate);
						String fromAccountCurrency= backendObj.getFromAccountCurrency();
						String toAccountCurrency= backendObj.getToAccountCurrency();
						Double convertedAmount;
						if(StringUtils.isNotBlank(transactionCurrency) && StringUtils.isNotBlank(fromAccountCurrency)) {
							if (transactionCurrency.equalsIgnoreCase(fromAccountCurrency) && transactionCurrency.equalsIgnoreCase("NPR")) {
								validateownaccountDTO.setTotalAmount(amount.toString());
								validateownaccountDTO.setConvertedAmount(amount.toString());
							} 
							// Converting fromAccountCurrency to Base Currency amount
							else if (transactionCurrency.equalsIgnoreCase(fromAccountCurrency) && !transactionCurrency.equalsIgnoreCase("NPR")) {
								Double equilentNPRAmount = exchangeRate * amount;
								equilentNPRAmount = (double) Math.round(equilentNPRAmount * 100.0) / 100.0;
								validateownaccountDTO.setTotalAmount(equilentNPRAmount.toString());
								validateownaccountDTO.setConvertedAmount(amount.toString());
							} 
							else if (transactionCurrency.equalsIgnoreCase("NPR") && !fromAccountCurrency.equalsIgnoreCase("NPR")) {
								convertedAmount = amount / exchangeRate;
								convertedAmount = (double) Math.round(convertedAmount * 100.0) / 100.0;
								validateownaccountDTO.setConvertedAmount(convertedAmount.toString());
								validateownaccountDTO.setTotalAmount(amount.toString());
							} 
							else {
								convertedAmount = amount / exchangeRate;
								convertedAmount = (double) Math.round(convertedAmount * 100.0) / 100.0;
								Double equilentNPRAmount= exchangeRate * amount;
								equilentNPRAmount = (double) Math.round(equilentNPRAmount * 100.0) / 100.0;
								validateownaccountDTO.setConvertedAmount(convertedAmount.toString());
								validateownaccountDTO.setTotalAmount(equilentNPRAmount.toString());
							}
							
						}
					}
					else if(validateownaccountDTO!=null && validateownaccountDTO.getDbpErrCode() == null  && validateownaccountDTO.getReferenceId() != null  && validateownaccountDTO.getExchangeRate()==null) {
						validateownaccountDTO.setTotalAmount(amount.toString());
						validateownaccountDTO.setConvertedAmount(amount.toString());
					}
					
					 result = JSONToResult.convert(new JSONObject(validateownaccountDTO).toString());
					 LOG.debug("OwnAccountFundTransferResourceImplExtn validate flow: result:"+ ResultToJSON.convert(result));
					 return result;
				} catch (JSONException e) {
					LOG.error("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e);
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
			try {
				LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::transactionStatusDTO:"+JSONUtils.stringify(transactionStatusDTO));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::transactionStatusDTO exception:"+e.getLocalizedMessage());
				e.printStackTrace();
			}
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
				ownaccountDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
			}
			catch(NumberFormatException e) {
				LOG.error("Invalid amount value", e);
				return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
			}
			
			ownaccountDTO.setRequestId(transactionStatusDTO.getRequestId());
			String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
			ownaccountDTO.setConfirmationNumber(confirmationNumber);
			ownaccountDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
			ownaccountDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
			ownaccountDTO.setLegalEntityId(legalEntityId);
			//ownaccountDTO.setTransactionId(null);
			String channel=HBLUtility.getDeviceInfo(request).get("channel_id").toString();
			LOG.debug("HBL::OwnAccountFundTransferResourceImplExtn::channel:"+channel);
			if(channel.equalsIgnoreCase("desktop")) {
				channel=HBLConstants.ONLINE_BANKING;
			}else {
				channel=HBLConstants.MOBILE_BANKING;
			}
			ownaccountDTO.setPaidBy(channel);
			OwnAccountFundTransferDTO ownaccountdbxDTO = ownaccountTransactionDelegate.createTransactionAtDBX(ownaccountDTO);				
			if(ownaccountdbxDTO == null) {
				LOG.error("Error occured while creating entry into the DBX table: ");
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
				LOG.error("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e);
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
				LOG.error("Invalid amount value", e);
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
						LOG.error("Error occured while creating entry into the backend table: ");
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
					LOG.error("Failed at pushAlertsForApprovalRequests "+e);
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
				LOG.error("Exception occured while invoking ownaccounttransaction business delegate",exp);
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
	                    LOG.error("Record doesn't exist");
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
				LOG.error("confirmationNumber is missing in the payload which is mandatory for edit");
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
				LOG.error("Error occured while fetching the input params: " , e);
				return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
			}
			
			if(ownaccountDTO == null) {
				LOG.error("Error occured while fetching the input params: " );
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
					LOG.error("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e);
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
				LOG.error("Invalid amount value", e);
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
				LOG.error("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e);
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}

			String referenceId = null;
			OwnAccountFundTransferDTO resDTO = null;
			
			try {
				ownaccountBackendDTO.setAmount(Double.parseDouble(ownaccountBackendDTO.getTransactionAmount()));
			} catch (Exception e) {
				LOG.error("Invalid amount value", e);
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
						LOG.error("Failed to edit the transaction at backend");
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
	    			LOG.error("Error occured while creating entry into the backend table: ");
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
				LOG.error(e);
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
						LOG.error("Record doesn't exist");
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
				LOG.error("confirmationNumber or transactionId is missing in the payload which is mandatory for cancel occurrence");
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
					LOG.error("Error occured while creating entry into the backend table: ");
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
				LOG.error("Failed at pushAlertsForApprovalRequests "+e);
			}

			// ADP-7058 update additional meta data
			try{
				approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
			} catch(Exception e){
				LOG.error(e);
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
						LOG.error("Record doesn't exist");
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
				LOG.error("confirmationNumber or transactionId is missing in the payload which is mandatory for delete");
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
					LOG.error("Error occured while creating entry into the backend table: ");
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
				LOG.error("Failed at pushAlertsForApprovalRequests "+e);
			}

			// ADP-7058 update additional meta data
			try{
				approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
			} catch(Exception e){
				LOG.error(e);
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
				String serviceName = result.getParamValueByName("serviceName");
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
				String status="";
				eventSubType = auditLog.deriveSubTypeForInternalTransfer(isScheduled, frequencyType);
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
				customParams.addProperty("toAccountName", customParams.get("FirstName").getAsString());
				customParams.addProperty("bankName", "Himalayan Bank Limited");
				customParams.addProperty("customerName", customer.get("FullName")!=null?customer.get("FullName").toString():"");
				customParams.addProperty("toAccountNumber", maskAccountNumber(toAccountNumber, 0, toAccountNumber.length()-4, 'X'));
				customParams.addProperty("fromAccountNumber", maskAccountNumber(fromAccountNumber, 0, toAccountNumber.length()-4, 'X'));
				String scheduledDate= result.getParamValueByName("scheduledDate");
				String amount= result.getParamValueByName(Constants.AMOUNT);
				amount=formatAmount(amount);
				String transactionCurrency= request.getParameter("transactionCurrency"); 
				if(StringUtils.isNotBlank(transactionCurrency))
				customParams.addProperty(Constants.AMOUNT, transactionCurrency+ " "+amount);
				if(scheduledDate!=null)
				customParams.addProperty("scheduledDate", scheduledDate.length()>10?scheduledDate.substring(0, 10):scheduledDate);
				if (transactionStatus.toString().contains("DENIED")) {
					statusID = Constants.SID_EVENT_FAILURE;
					customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
				} else {
					switch (transactionStatus) {
					case SENT:
						referenceId = customParams.get("referenceId").getAsString();
						if (ownaccountDTO.getDbpErrMsg() != null && !ownaccountDTO.getDbpErrMsg().isEmpty()) {
							statusID = Constants.SID_EVENT_FAILURE;
							status= Constants.STATUS_FAIL.toLowerCase();
						}
						if (referenceId == null || "".equals(referenceId)) {
							statusID = Constants.SID_EVENT_FAILURE;
							status= Constants.STATUS_FAIL.toLowerCase();
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
				
				EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null,
						CustomerSession.getCustomerId(customer), null,  customParams);
			} catch (Exception e) {
				LOG.error("Error while pushing to Audit Engine.");
			}
		}
		public JSONObject getDeviceInfo(DataControllerRequest requestInstance){
			String reportingParams= requestInstance.getHeader("X-Kony-ReportingParams");
			
			JSONObject reportingParamsJson;
			JSONObject deviceInfo = new JSONObject();
			try {
				reportingParamsJson = new JSONObject(
				        URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
			String deviceId = reportingParamsJson.optString("did");
	        String channel_id = reportingParamsJson.optString("chnl");
	        String appId = reportingParamsJson.optString("aid");
	        String deviceName = new String();
	        String operatingSystem = new String();
	        if (channel_id.equalsIgnoreCase("desktop")) {
				String userAgentstr = getUserAgentStr(requestInstance.getHeader("User-Agent"));
	            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentstr);
	            StringBuilder sb = new StringBuilder();
	            sb.append(userAgent.getBrowser().getName()).append(" ")
	                    .append(userAgent.getBrowserVersion().getVersion());
	            deviceName = sb.toString();
	            operatingSystem = userAgent.getOperatingSystem().getName();
	        } else {
	            deviceName = reportingParamsJson.optString("dm");
	            operatingSystem = reportingParamsJson.optString("plat") + " " + reportingParamsJson.optString("os");
	        }
	        deviceInfo.put("deviceName", deviceName);
	        deviceInfo.put("operatingSystem", operatingSystem);
	        deviceInfo.put("channel_id", channel_id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return deviceInfo;
	}
		private static String getUserAgentStr(String userAgentstr) {
			if (StringUtils.isBlank(userAgentstr))
				return userAgentstr;
			Pattern pattern = Pattern.compile("(?:Edg/(([0-9]+).([0-9]*)))", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(userAgentstr);
			if (matcher.find()) {
				userAgentstr = userAgentstr.replace(matcher.group(), matcher.group().replace("Edg", "Edge"));
			}
			return userAgentstr;
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
