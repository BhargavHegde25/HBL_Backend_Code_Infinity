package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.utills.HBLUtility;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
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
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.BillPayTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.dbx.product.transactionservices.resource.impl.BillPayTransactionResourceImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class BillPayTransactionResourceImplExtn extends BillPayTransactionResourceImpl{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	@Override
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
		BillPayTransactionBackendDelegate billpayBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BillPayTransactionBackendDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		BillPayTransactionDTO billpayDTO = null;
		Result result = new Result();
		Double amount = null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		String featureActionId = null;
		
		inputParams.put("legalEntityId", legalEntityId);

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
		
		featureActionId = FeatureAction.BILL_PAY_CREATE;
		
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
		inputParams.put("isScheduled ", "0");
		String paymentAggregator = inputParams.get("paymentAggregator") != null ? inputParams.get("paymentAggregator").toString() : "";
		
		try {
			billpayDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), BillPayTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		String date = billpayDTO.getScheduledDate() == null ? 
				(billpayDTO.getProcessingDate() == null ? 
						(billpayDTO.getFrequencystartdate() == null ? 
								application.getServerTimeStamp()
								: billpayDTO.getFrequencystartdate())
						: billpayDTO.getProcessingDate()) 
				: billpayDTO.getScheduledDate();

		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String requestid = "";
		if("true".equalsIgnoreCase(validate)) {
			BillPayTransactionBackendDTO billpayBackendDTO = new BillPayTransactionBackendDTO();
			billpayBackendDTO = billpayBackendDTO.convert(billpayDTO);
			
			BillPayTransactionDTO validatebillpayDTO = billpayBackendDelegate.validateTransaction(billpayBackendDTO,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validatebillpayDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response for billpay transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		}	
		String transactionAmount=inputParams.get("transactionAmount").toString();
		
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
		transactionStatusDTO.setTransactionAmount(transactionAmount);
		
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			result.addParam(new Param("status", ErrorCodeEnum.ERR_10420.getMessage())); 
			return result;
		}
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		
		
		billpayDTO.setStatus(transactionStatus.getStatus());
		//billpayDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		billpayDTO.setTransactionAmount(transactionAmount);
		try {
			billpayDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		billpayDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		billpayDTO.setTransactionId(null);
		billpayDTO.setRequestId(transactionStatusDTO.getRequestId());
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		billpayDTO.setConfirmationNumber(confirmationNumber);
		String channel=HBLUtility.getDeviceInfo(request).get("channel_id").toString();
		alert.prepareError("HBL::BillPayTransactionResourceImplExtn::channel:"+channel).log();
		if(channel.equalsIgnoreCase("desktop")) {
			channel=HBLConstants.ONLINE_BANKING;
		}else {
			channel=HBLConstants.MOBILE_BANKING;
		}
		billpayDTO.setPaidBy(channel);
		if(StringUtils.isBlank(billpayDTO.getToAccountNumber())) {
		billpayDTO.setToAccountNumber(billpayDTO.getPayeeName());
		}
		//String brokerName=paymentAggregator;
		billpayDTO.setPayPersonName(paymentAggregator);
		JSONObject externalApiPayload = inputParams.get("externalApiPayload")!=null? (JSONObject) inputParams.get("externalApiPayload"):new JSONObject();
		externalApiPayload.put("paymentId",  inputParams.get("paymentId"));
		HashMap<String, Object> payload = (HashMap<String, Object>) externalApiPayload.toMap();
		alert.prepareError("HBL::BillPayTransactionResourceImplExtn::externalApiPayload:"+payload).log();
		billpayDTO.setExternalApiPayload(payload);
		BillPayTransactionDTO billpaydbxdto = billpayTransactionDelegate.createTransactionAtDBX(billpayDTO);
		if(billpaydbxdto == null) {
			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if(billpaydbxdto.getDbpErrCode() != null || billpaydbxdto.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", billpaydbxdto.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", billpaydbxdto.getDbpErrMsg()));
			return result;
		}		

		BillPayTransactionBackendDTO billpayBackendDTO = new BillPayTransactionBackendDTO();
		billpayBackendDTO = billpayBackendDTO.convert(billpaydbxdto);
		
		try {
			billpayBackendDTO.setAmount(Double.parseDouble(billpayBackendDTO.getTransactionAmount()));
			billpaydbxdto.setAmount(Double.parseDouble(billpayBackendDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		String requestObj = null;
		try {
			requestObj = new JSONObject(billpayBackendDTO).toString();
			result = JSONToResult.convert(requestObj);
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for BillPay transfer: ", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
		}
		
		String referenceId = billpaydbxdto.getTransactionId();
		if (transactionStatus == TransactionStatusEnum.SENT) {
			BillPayTransactionDTO billpaybackendtrxDto= new BillPayTransactionDTO();
			if(StringUtils.isEmpty(backendid)) {
				billpayBackendDTO.setTransactionId(null);
				String beneficiaryName = inputParams.get("PayeeName") != null ? inputParams.get("PayeeName").toString() : "";
				inputParams.put("billpaytransfersTransactionId", referenceId);
				inputParams.put("beneficiaryName",beneficiaryName);
				payload= new HashMap();
				payload.put("externalApiPayload", externalApiPayload);
				payload.put("transactionData", inputParams);
				billpayBackendDTO.setExternalApiPayload(payload);
				//billpayBackendDTO.setExternalApiPayload(payloadForActualService(inputParams, referenceId));
				billpaybackendtrxDto = billpayBackendDelegate.createTransactionWithoutApproval(billpayBackendDTO, request);					
				if(billpaybackendtrxDto == null) {	
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,  TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12000.setErrorCode(result);
				}
			}
			else {
				String frequency = StringUtils.isEmpty(billpayDTO.getFrequencyTypeId()) ? null : billpayDTO.getFrequencyTypeId();
				billpaybackendtrxDto = billpayTransactionDelegate.approveTransaction(backendid, request, frequency);
				if(billpaybackendtrxDto == null) {	
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			String requestId=billpaybackendtrxDto.getRequestId();
			alert.prepareError("BillPayTransactionResourceImplExtn:requestId:" + requestId).log();
			if(billpaybackendtrxDto.getDbpErrCode() != null || billpaybackendtrxDto.getDbpErrMsg() != null) {
				String dbpErrMsg=billpaybackendtrxDto.getDbpErrMsg();
				String dbpErrCode=billpaybackendtrxDto.getDbpErrCode();
				String status=billpaybackendtrxDto.getStatus();
				result.addParam(new Param("referenceId", billpaybackendtrxDto.getInitiationId()));
				if(dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage()) || dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())) {
				result.addParam(new Param("legalEntityId", legalEntityId));
				}
				result.addParam(new Param("message", billpaybackendtrxDto.getMessage()));
				result.addParam(new Param("responseMessage", billpaybackendtrxDto.getDescription()));
				String paymentOrderId=billpaybackendtrxDto.getInitiationId();
				result.addParam(new Param("paymentId", billpaybackendtrxDto.getInitiationId()));
				updateStatusUsingTransactionId(referenceId,  TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, paymentOrderId, requestId, billpaybackendtrxDto.getExternalServiceResponse(),billpaybackendtrxDto.getDescription());
				result.addParam(new Param("dbpErrCode", dbpErrCode));
				result.addParam(new Param("dbpErrMsg", dbpErrMsg));
				result.addParam(new Param("status", status));
				result.addParam(new Param("requestId", requestId));
				return result;
			}
			String refId = billpaybackendtrxDto.getReferenceId();
			if(refId == null || "".equals(refId)) {
				//billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				updateStatusUsingTransactionId(referenceId,  TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, billpaybackendtrxDto.getInitiationId(), billpaybackendtrxDto.getRequestId(), billpaybackendtrxDto.getExternalServiceResponse(),billpaybackendtrxDto.getDescription());
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			JSONObject externalApiResponseObject = billpaybackendtrxDto.getExternalServiceResponse();
			if(externalApiResponseObject!=null) {
				JSONObject ConfirmBillPaidResult = new JSONObject();
				JSONObject serviceResponse = billpaybackendtrxDto.getServiceResponse();
				if(serviceResponse!=null&& paymentAggregator.equalsIgnoreCase("NEA")) {
				JSONArray RETURN_CONFIRMPAID_NATIVE = new JSONArray();
				RETURN_CONFIRMPAID_NATIVE.put(serviceResponse);
				ConfirmBillPaidResult.put("RETURN_CONFIRMPAID_NATIVE", RETURN_CONFIRMPAID_NATIVE);
				result=JSONToResult.convert(ConfirmBillPaidResult.toString());
				}
			 }
			updateStatusUsingTransactionId(referenceId,TransactionStatusEnum.EXECUTED.getStatus(), refId, billpaybackendtrxDto.getInitiationId(), requestId, externalApiResponseObject,billpaybackendtrxDto.getDescription());
			result.addParam(new Param("referenceId", refId));
			result.addParam(new Param("paymentId", billpaybackendtrxDto.getInitiationId()));
			result.addParam(new Param("responseMessage", billpaybackendtrxDto.getDescription()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
	        result.addParam(new Param("legalEntityId", legalEntityId));
	        result.addParam(new Param("httpStatusCode", "200"));
	        result.addParam(new Param("requestId", requestId));
	        result.addParam(new Param("toAccountNumber", billpayDTO.getToAccountNumber()));
	        result.addParam(new Param("transactionAmount", billpayDTO.getTransactionCurrency()+" "+ billpayDTO.getTransactionAmount()));
	        result.addParam(new Param("notes", billpaybackendtrxDto.getTransactionsNotes()));
	        result.addParam(new Param("serviceCharge", billpayDTO.getTransactionCurrency()+" "+billpayDTO.getServiceCharge()));
	        result.addParam(new Param("amount", billpayDTO.getTransactionCurrency()+" "+String.valueOf(billpayDTO.getTransactionAmount())));
	        result.addParam(new Param("paymentStatus", transactionStatus.getStatus().equalsIgnoreCase("SENT")?"Successful":transactionStatus.getStatus()));
	        result.addParam(new Param("transactionDate", billpayDTO.getScheduledDate().substring(0, 10)));
	        result.addParam(new Param("bankName", "Himalayan Bank Limited"));
	        String customerName=(String) customer.get("FirstName");
	        result.addParam(new Param("customerName", customerName));
	        
	        
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId = null;
			if(StringUtils.isEmpty(backendid))
			{
				BillPayTransactionDTO billpaypendingtxnDto = billpayTransactionDelegate.createPendingTransaction(billpaydbxdto, request);
				if(billpaypendingtxnDto == null)
				{
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(billpaypendingtxnDto.getDbpErrCode() != null || billpaypendingtxnDto.getDbpErrMsg() != null) {
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, billpaypendingtxnDto.getDbpErrMsg());
				}
				backendid = billpaypendingtxnDto.getReferenceId();
			}
				pendingrefId= backendid;
				billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					billpayBackendDelegate.deleteTransaction(backendid, null, request);
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					billpayBackendDelegate.deleteTransaction(backendid, null, request);
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,TransactionStatusEnum.FAILED.getStatus(), backendid);
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
				billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,transactionStatus.toString(), pendingrefId);
			}
			 //Code snippet added for triggering the alerts
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,
						inputParams, null,  confirmationNumber, requestid, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
			}
		}
		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		try {
			//_logTransaction(request,response,inputArray,result,transactionStatus,transactionStatusDTO.getConfirmationNumber(),billpaydbxdto,requestid);
		} catch(Exception e) {
			alert.prepareError("Exception Occured at _logTransaction in BillPayTransactionResourceImplExtn",e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}

		return result;
	}
	public HashMap<String, Object>  payloadForActualService(Map<String, Object> inputParams, String transactionid) {
		HashMap<String, Object>payload = new HashMap<String, Object>();
		JSONObject externalApiPayload = inputParams.get("externalApiPayload")!=null? (JSONObject) inputParams.get("externalApiPayload"):new JSONObject();
		String beneficiaryName = inputParams.get("PayeeName") != null ? inputParams.get("PayeeName").toString() : "";
		inputParams.remove("externalApiPayload");
		inputParams.remove("transactionDetails");
		inputParams.put("billpaytransfersTransactionId", transactionid);
		inputParams.put("beneficiaryName",beneficiaryName);
		payload.put("externalApiPayload", externalApiPayload);
		payload.put("transactionData", inputParams);
		alert.prepareError("BillPayTransactionResourceImplExtn:payloadForActualService:"+payload).log();;
		return payload;
		
	}
	public BillPayTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber, String paymentOrderId, String requestId, JSONObject externalServiceResonse, String description) {

		List<BillPayTransactionDTO> billpayTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BILLPAYTRANSFERS_UPDATE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		requestParams.put("status", status);
		if(StringUtils.isNotBlank(paymentOrderId))
		requestParams.put("paymentId", paymentOrderId);
		if(StringUtils.isNotBlank(confirmationNumber))
		requestParams.put("confirmationNumber", confirmationNumber);
		if(externalServiceResonse!=null)
		requestParams.put("externalServiceResponse", externalServiceResonse.toString());
		if(StringUtils.isNotBlank(requestId))
		requestParams.put("requestId", requestId);
		if(StringUtils.isNotBlank(description))
		requestParams.put("description", description);
		//requestParams.put("description", paymentOrderId);
		alert.prepareError("updateStatusUsingTransactionId :requestParams:"+requestParams).log();
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray billpayJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			billpayTransactionDTO = JSONUtils.parseAsList(billpayJsonArray.toString(), BillPayTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the billpaytransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the billpaytransaction",exp).log();
			return null;
		}
		
		if(billpayTransactionDTO != null && billpayTransactionDTO.size() != 0)
			return billpayTransactionDTO.get(0);
		
		return null;
	}
private void _logTransaction(DataControllerRequest request,DataControllerResponse response,Object[] inputArray,Result result, TransactionStatusEnum transactionStatus, String referenceId,BillPayTransactionDTO intrabankDTO,String requestId) {
	alert.prepareError("_logTransaction in BillPayTransactionResourceImplExtn").log();
		String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE)) return;
		
			ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApproversBusinessDelegate.class);
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);

			AuditLog auditLog = new AuditLog();

			String eventType = Constants.FEATURE_BILL_PAY;
			String eventSubType = "";
			String producer =Constants.BILL_PAY_CREATE;
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

			JsonObject customParams = new JsonObject();
			customParams.addProperty("referenceId", result.getParamValueByName("referenceId"));
			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);

			eventSubType = auditLog.deriveSubTypeForBillPayment(true);
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
					if (intrabankDTO == null) {
						statusID = Constants.SID_EVENT_FAILURE;
					}
					if (intrabankDTO.getDbpErrMsg() != null && !intrabankDTO.getDbpErrMsg().isEmpty()) {
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
			alert.prepareError("Before Dispatch Logs at in BillPayTransactionResourceImplExtn.producer: "+producer).log();
			Result jj = EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, "",
                    CustomerSession.getCustomerId(customer), "",  customParams);
		
	}

}
