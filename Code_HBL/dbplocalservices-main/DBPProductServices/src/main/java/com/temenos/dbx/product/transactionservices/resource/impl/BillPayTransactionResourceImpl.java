package com.temenos.dbx.product.transactionservices.resource.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
import com.temenos.dbx.product.transactionservices.resource.api.BillPayTransactionResource;

public class BillPayTransactionResourceImpl implements BillPayTransactionResource {

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
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		
		
		billpayDTO.setStatus(transactionStatus.getStatus());
		billpayDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
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
				billpaybackendtrxDto = billpayBackendDelegate.createTransactionWithoutApproval(billpayBackendDTO, request);					
				if(billpaybackendtrxDto == null) {	
					billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,  TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12600.setErrorCode(result);
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
			
			if(billpaybackendtrxDto.getDbpErrCode() != null || billpaybackendtrxDto.getDbpErrMsg() != null) {
				billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,  TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
                return ErrorCodeEnum.ERR_00000.setErrorCode(result, billpaybackendtrxDto.getDbpErrMsg());
			}
			
			String refId = billpaybackendtrxDto.getReferenceId();
			if(refId == null || "".equals(refId)) {
				billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			billpayTransactionDelegate.updateStatusUsingTransactionId(referenceId,TransactionStatusEnum.EXECUTED.getStatus(), billpaybackendtrxDto.getReferenceId());
			result.addParam(new Param("referenceId", refId));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
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
		BillPayTransactionDTO billPayTransactionDTO = null;
		
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
			BillPayTransactionBusinessDelegate billPayTransactionDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
			billPayTransactionDTO = billPayTransactionDelegate.updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
			if(billPayTransactionDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			JSONObject billPayTransactionJSON = new JSONObject(billPayTransactionDTO);
			result = JSONToResult.convert(billPayTransactionJSON.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking billpaytransaction business delegate",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	}

    public Result createBulkBillPay(String methodID, Object[] inputArray, DataControllerRequest request,
                           DataControllerResponse response) {
        Result result = new Result();

        //Initialization of business Delegate Class
        /*BillPayTransactionBusinessDelegate billPayBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
		*/
        
        @SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		List<Map<String, Object>> inputs = getFormattedInput(inputParams);
        Map<String, Object> parsedRequest = new HashMap<String, Object>();
        
        //Authorization Checks for feature
		String featureActionlist = CustomerSession.getPermittedActionIds(request, Arrays.asList("BILL_PAY_BULK"));
		if (StringUtils.isEmpty(featureActionlist)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
        if(inputs.size() > 1){
            parseRequestInput(inputs ,parsedRequest);
        }
        else{
        	parsedRequest = inputs.get(0);
        	parsedRequest.put("transactionType", "BillPay");
        }
        parsedRequest.put("loop_count", String.valueOf(inputs.size()));

        try {
            List<BillPayTransactionDTO> billpayDTOs = _createBulkBillPay(parsedRequest, request);
            
            JSONArray bulkbillpayJSONArr = new JSONArray(billpayDTOs);
			JSONObject responseObj = new JSONObject();
			responseObj.put("transaction", bulkbillpayJSONArr);
			result = JSONToResult.convert(responseObj.toString());
			
        }catch (Exception e) {
            alert.prepareError("Caught exception at createBulkBillPay method: ", e).log();
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
    	
    	BillPayTransactionBusinessDelegate billPayBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
    	
    	if(referenceId != null) {
    		status = TransactionStatusEnum.EXECUTED;
    		confirmationNumber = referenceId.getValue();
    	}
    	else {
    		status = TransactionStatusEnum.FAILED;
    	}
    	inputParams.put("status", status.getStatus());
    	billPayBusinessDelegate.updateStatusUsingTransactionId(transactionId, status.getStatus(), confirmationNumber);
    	
    	result.addParam(new Param("initiationId", request.getParameter("initiationId")));
    	result.addParam(new Param("dbxtransactionId", transactionId));
    	*/
    	
    	return result;
    }


    private void parseRequestInput(List<Map<String, Object>> records, Map<String, Object> parsedRequest) {
    	StringBuffer amount = new StringBuffer();
		StringBuffer deliverBy = new StringBuffer();
		StringBuffer fromAccountNumber = new StringBuffer();
		StringBuffer scheduledDate = new StringBuffer();
		StringBuffer transactionsNotes = new StringBuffer();
		StringBuffer transactionType = new StringBuffer();
		StringBuffer Bill_id = new StringBuffer();
		StringBuffer payeeId = new StringBuffer();
		StringBuffer payeeName = new StringBuffer();
		StringBuffer transactionCurrency = new StringBuffer();
		StringBuffer initiationId = new StringBuffer();
		int id =0;
		
        for (Map<String, Object> input : records) {
            amount = amount.append(input.get("amount")+ ",");
			deliverBy = deliverBy.append(input.get("deliverBy")+ ",");
			fromAccountNumber = fromAccountNumber.append(input.get("fromAccountNumber")+ ",");
			scheduledDate = scheduledDate.append(input.get("scheduledDate")+ ",");
			Object notes = input.get("transactionsNotes");
			if(notes != null && StringUtils.isBlank(notes.toString())) {
				notes = null;
			}
			transactionsNotes = transactionsNotes.append(notes+ ",");
			transactionType = transactionType.append("BillPay"+ ",");
			Bill_id = Bill_id.append(input.get("Bill_id")+ ",");
			payeeId = payeeId.append(input.get("payeeId")+ ",");
			payeeName = payeeName.append(input.get("payeeName")+ ",");
			transactionCurrency = transactionCurrency.append(input.get("transactionCurrency")+ ",");
			initiationId = initiationId.append(id+ ",");
			id++;
        }
		
        parsedRequest.put("amount",amount.toString());
        parsedRequest.put("deliverBy",deliverBy.toString());
        parsedRequest.put("fromAccountNumber",fromAccountNumber.toString());
        parsedRequest.put("scheduledDate",scheduledDate.toString());
        parsedRequest.put("transactionsNotes",transactionsNotes.toString());
        parsedRequest.put("transactionType",transactionType.toString());
        parsedRequest.put("Bill_id",Bill_id.toString());
        parsedRequest.put("payeeId",payeeId.toString());
        parsedRequest.put("payeeName", payeeName.toString());
        parsedRequest.put("transactionCurrency",transactionCurrency.toString());
        parsedRequest.put("initiationId", initiationId.toString());
    }

    private List<Map<String, Object>> getFormattedInput(Map<String, String> inputParams) {
        List<Map<String, Object>> inputs = new ArrayList<>();
        String bulkPayString = (String) inputParams.get("bulkPayString");
        JsonParser jsonParser = new JsonParser();
        JsonArray jArray = (JsonArray) jsonParser.parse(bulkPayString);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Gson gson = new Gson();
        if (jArray.isJsonArray()) {
            for (int i = 0; i < jArray.size(); i++) {
                @SuppressWarnings("unchecked")
				Map<String, Object> inputObject = (Map<String, Object>) gson.fromJson(jArray.get(i), type);
                inputs.add(inputObject);
            }
        }
        return inputs;
    }
    
    //Added this method to pass DCRequest. Will be moved to BusinessDelegate after suitable architecture changes
    private List<BillPayTransactionDTO> _createBulkBillPay(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest) {

    	List<BillPayTransactionDTO> billpayDTOs;

        String serviceName = ServiceId.BULK_BILL_PAY_SERVICE;
        String operationName = OperationName.CREATE_BULK_BILL_PAY;

        String bulkBillPayResponse = null;
        try {
        	bulkBillPayResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(dataControllerRequest).
					build().getResponse();
        	
            JSONObject bulkBillPayJSON = new JSONObject(bulkBillPayResponse);
			JSONArray records = bulkBillPayJSON.getJSONArray("LoopDataset");
			billpayDTOs = JSONUtils.parseAsList(records.toString(), BillPayTransactionDTO.class);
        }
        catch(JSONException e) {
            alert.prepareError("Failed to create billpay transaction: ", e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at create billpay transaction: ", e).log();
            return null;
        }

        return billpayDTOs;
    }

	@Override
	public Result editTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
		BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
		BillPayTransactionBackendDelegate billpayBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BillPayTransactionBackendDelegate.class);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		
		String featureActionId = FeatureAction.BILL_PAY_CREATE;
		BillPayTransactionDTO billpayDTO = null;
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
		
		if (confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
			confirmationNumber = confirmationNumberParam.toString();
			billpayDTO = billpayTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber + "", Arrays.asList(companyId), createdby, legalEntityId);
			if (billpayDTO == null) {
				billpayDTO = billpayBackendDelegate.fetchTransactionById(confirmationNumber, request);
				if(billpayDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
				billpayDTO = billpayTransactionDelegate.createTransactionAtDBX(billpayDTO);
			}
		} else {
			alert.prepareError("confirmationNumber is missing in the payload which is mandatory for edit").log();
			return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
		}
		
		String transactionId = billpayDTO.getTransactionId();
		String oldRequestId = billpayDTO.getRequestId();
		
		//Authorization checks on transcationId
		/*
		if(billpayDTO.getCompanyId() != null && ! billpayDTO.getCompanyId().equals(companyId)) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isBusinessUser && ! CustomerSession.IsCombinedUser(customer) && ! billpayDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		*/
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = billpayDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		BillPayTransactionDTO requestDTO = null;
		inputParams.put("createdby", createdby);
		inputParams.put("companyId", companyId);
		inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		
		try {
			requestDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), BillPayTransactionDTO.class);
			requestDTO.setConfirmationNumber(null);
			requestDTO.setTransactionId(null);
			billpayDTO = billpayDTO.updateValues(requestDTO);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(billpayDTO == null) {
			alert.prepareError("Error occured while fetching the input params: " ).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		//Authorization checks on newfromAccountNumber and featureactionId
		String newfromAccountNumber = billpayDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, newfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		String date = billpayDTO.getScheduledDate() == null ? 
				(billpayDTO.getProcessingDate() == null ? 
						(billpayDTO.getFrequencystartdate() == null ? 
								application.getServerTimeStamp()
								: billpayDTO.getFrequencystartdate())
						: billpayDTO.getProcessingDate()) 
				: billpayDTO.getScheduledDate();
		
		//Transaction limit checks on amount and featureactionId
		Double amount = billpayDTO.getAmount();
		String transactionAmount = inputParams.get("transactionAmount") != null ? inputParams.get("transactionAmount").toString() : null;
	    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
	    
	    if("0".equals(transactionAmount) || "0.0".equals(transactionAmount) || "0.00".equals(transactionAmount)) {
			transactionAmount = amount + "";
		}
	    
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		
		if("true".equalsIgnoreCase(validate)) {
			BillPayTransactionBackendDTO backendObj = new BillPayTransactionBackendDTO();
			backendObj =  backendObj.convert(billpayDTO);
			BillPayTransactionDTO validateownaccountDTO = billpayBackendDelegate.validateTransaction(backendObj,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validateownaccountDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for bill pay transfer: ", e).log();
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
		transactionStatusDTO.setTransactionCurrency(billpayDTO.getTransactionCurrency());
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
		
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();

		billpayDTO.setStatus(transactionStatus.getStatus());
		//billpayDTO.setRequestId(null);
		billpayDTO.setConfirmationNumber(confirmationNumber+"");
		billpayDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		try {
			billpayDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
		}
		catch(NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		billpayDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		
		BillPayTransactionBackendDTO billpayBackendDTO = new BillPayTransactionBackendDTO();
		billpayBackendDTO = billpayBackendDTO.convert(billpayDTO);
		
		try {
			billpayBackendDTO.setAmount(Double.parseDouble(billpayBackendDTO.getTransactionAmount()));
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
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		String referenceId = null;
		BillPayTransactionDTO resDTO = null;
		
		if (transactionStatus == TransactionStatusEnum.SENT) {
			if (confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
				billpayBackendDTO.setTransactionId(confirmationNumber);
				resDTO = billpayBackendDelegate.editTransactionWithoutApproval(billpayBackendDTO, request);
			}
			
			if (resDTO == null) {
				billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12600.setErrorCode(result);
			}

			if (resDTO.getDbpErrMsg() != null && !resDTO.getDbpErrMsg().isEmpty()) {
				billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, resDTO.getDbpErrMsg());
			}
			referenceId = resDTO.getReferenceId();

			if(StringUtils.isEmpty(referenceId)) {
				billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12602.setErrorCode(result);
			}
			
			billpayDTO = resDTO;
			billpayDTO.setConfirmationNumber(referenceId);
			billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), billpayDTO.getReferenceId());
			
			result.addParam(new Param("referenceId", resDTO.getReferenceId()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
			
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){

    		String requestId = transactionStatusDTO.getRequestId();
    		referenceId = confirmationNumber;
    		billpayBackendDTO.setTransactionId(confirmationNumber);
    		BillPayTransactionDTO pendingtransactionDTO = billpayBackendDelegate.editTransactionWithApproval(billpayBackendDTO, request);
    		if(pendingtransactionDTO == null)
    		{
    			billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			alert.prepareError("Error occured while creating entry into the backend table: ").log();
    			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
    		}
    		if(pendingtransactionDTO.getDbpErrCode() != null || pendingtransactionDTO.getDbpErrMsg() != null) {
    			billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
    			return ErrorCodeEnum.ERR_00000.setErrorCode(result, pendingtransactionDTO.getDbpErrMsg());
    		}
    		
    		String backendid = pendingtransactionDTO.getReferenceId();
            billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
            transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, backendid, transactionStatusDTO.isSelfApproved(), featureActionId, request);
    		
            if(transactionStatusDTO == null) 
			{							
            	billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
				return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
			}
			
			if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
            transactionStatus=transactionStatusDTO.getStatus();
    		billpayTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
    		billpayDTO.setConfirmationNumber(backendid);
    		billpayDTO.setStatus(transactionStatus.toString());
    		billpayDTO.setRequestId(requestId);
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
    		billpayDTO.setTransactionId(transactionId);
    		billpayTransactionDelegate.updateTransactionAtDBX(billpayDTO);
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
		
		BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
		BillPayTransactionBackendDelegate billpayBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BillPayTransactionBackendDelegate.class);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		String featureActionId = FeatureAction.BILL_PAY_CREATE;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		BillPayTransactionDTO billpayDTO = null;
		
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
			billpayDTO = billpayTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", contracts, createdby, null);
			if(billpayDTO == null) {
				billpayDTO = billpayBackendDelegate.cancelTransactionOccurrence(confirmationNumber, request);
				
				if(billpayDTO.getDbpErrMsg() != null && !billpayDTO.getDbpErrMsg().isEmpty()) {
					alert.prepareError(billpayDTO.getDbpErrMsg()).log();
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, billpayDTO.getDbpErrMsg());
				}
				
				String referenceId = billpayDTO.getReferenceId();
				transactionId = billpayDTO.getTransactionId();
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
		
		transactionId = billpayDTO.getTransactionId();
		String oldrequestId = billpayDTO.getRequestId();
		
		//Authorization checks on transcationId
		/*
		if(billpayDTO.getCompanyId() != null && ! contracts.contains(billpayDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! billpayDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}*/
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = billpayDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
			
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
			billpayDTO = billpayBackendDelegate.cancelTransactionOccurrence(confirmationNumber, request);
		}
		else {
			
			billpayTransactionDelegate.deleteTransactionAtDBX(transactionId);
			
			if(oldrequestId != null) {
				approvalQueueDelegate.deleteTransactionFromApprovalQueue(oldrequestId);
			}
		}
		
		String referenceId = billpayDTO.getReferenceId();
		
		if(billpayDTO.getDbpErrMsg() != null && !billpayDTO.getDbpErrMsg().isEmpty()) {
			alert.prepareError(billpayDTO.getDbpErrMsg()).log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, billpayDTO.getDbpErrMsg());
		}
		
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
		
		BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
		
		BillPayTransactionBackendDelegate billpayBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				   .getFactoryInstance(BackendDelegateFactory.class)
				   .getBackendDelegate(BillPayTransactionBackendDelegate.class);

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		String featureActionId = FeatureAction.BILL_PAY_CREATE;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		
		String legalEntityId = (String) customer.get("legalEntityId");
		
		BillPayTransactionDTO billpayDTO = null;
		
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
			billpayDTO = billpayTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber+"", contracts, createdby, legalEntityId);
			if(billpayDTO == null) {
				billpayDTO = billpayBackendDelegate.deleteTransaction(confirmationNumber+"", transactionType, request);
				
				if(billpayDTO.getDbpErrMsg() != null && !billpayDTO.getDbpErrMsg().isEmpty()) {
					alert.prepareError(billpayDTO.getDbpErrMsg()).log();
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, billpayDTO.getDbpErrMsg());
				}
				
				String referenceId = billpayDTO.getReferenceId();
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
		
		transactionId = billpayDTO.getTransactionId();
		transactionType = billpayDTO.getTransactionType();
		String oldrequestId = billpayDTO.getRequestId();
		
		//Authorization checks on transcationId
		if(billpayDTO.getCompanyId() != null && ! contracts.contains(billpayDTO.getCompanyId())) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		else if(!isSMEUser && ! CustomerSession.IsCombinedUser(customer) && ! billpayDTO.getCreatedby().equals(createdby)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		//Authorization checks on prevfromAccountNumber and featureactionId
		String prevfromAccountNumber = billpayDTO.getFromAccountNumber();
		
		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
			
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
			billpayDTO = billpayBackendDelegate.deleteTransaction(confirmationNumber+"", transactionType, request);
		}
		
		if(billpayDTO.getDbpErrMsg() != null && !billpayDTO.getDbpErrMsg().isEmpty()) {
			alert.prepareError(billpayDTO.getDbpErrMsg()).log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, billpayDTO.getDbpErrMsg());
		}
		
		String referenceId = billpayDTO.getReferenceId();
		
		if(confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)
				&& (referenceId == null || "".equals(referenceId))) {
			alert.prepareError("Failed to delete the transaction at backend").log();
			return ErrorCodeEnum.ERR_12603.setErrorCode(result);
		}
		
			billpayTransactionDelegate.deleteTransactionAtDBX(transactionId);
		
		if(oldrequestId != null) {
			approvalQueueDelegate.deleteTransactionFromApprovalQueue(oldrequestId);
		}
		 
		result.addParam(new Param("transactionId", confirmationNumber+""));
		
		return result;
	}
}
