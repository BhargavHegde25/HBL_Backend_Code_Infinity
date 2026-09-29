package com.temenos.dbx.product.transactionservices.businessdelegate.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.DomesticWireTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.OneTimePayeeDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DomesticWireTransactionBusinessDelegate}
 */

public class DomesticWireTransactionBusinessDelegateImpl implements DomesticWireTransactionBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	DomesticWireTransactionBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(DomesticWireTransactionBackendDelegate.class);
	
	@Override
	public  WireTransactionDTO createTransactionAtDBX(WireTransactionDTO wireTransactionDTO) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_CREATE;
		String oneTimePayeeOperationName = OperationName.DB_ONETIMEPAYEE_CREATE;
		String createResponse = null;
		OneTimePayeeDTO payee = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(wireTransactionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		
		String payeeId = wireTransactionDTO.getPayeeId();
		
		if(payeeId == null || "".equals(payeeId)) {
			try {
				requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(oneTimePayeeOperationName).withRequestParameters(requestParameters).build().getResponse();
				JSONObject response = new JSONObject(createResponse);
				JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);
				
				payee = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), OneTimePayeeDTO.class);
				
				requestParameters.put("onetime_id", payee.getOnetime_id());
				
			} catch (Exception e) {
				alert.prepareError("Failed to create wire transaction payee entry into onetime payee table: ", e).log();
				return null;
			}
		}
		else {
			requestParameters.remove("onetime_id");
		}
		
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);
			
			wireTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), WireTransactionDTO.class);
			if(payee != null)
				wireTransactionDTO.addAll(payee);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create wire transaction entry into wiretransfers table: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create wire transaction entry: ", e).log();
			return null;
		}
		
		return wireTransactionDTO;
	}
	
	@Override
	public WireTransactionDTO updateStatus(String transactionId, String status, String confirmationNumber) {

		if(application.getIsStateManagementAvailableFromCache()) {
			return updateStatusUsingConfirmationNumber(transactionId, status);
		}
		else {
			return updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
		}
	}
	
	@Override
	public WireTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber) {

		List<WireTransactionDTO> wireTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_UPDATE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		requestParams.put("status", status);
		requestParams.put("confirmationNumber", confirmationNumber);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray wireJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			wireTransactionDTO = JSONUtils.parseAsList(wireJsonArray.toString(), WireTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the wiretransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the wiretransaction",exp).log();
			return null;
		}
		
		if(wireTransactionDTO != null && wireTransactionDTO.size() != 0)
			return wireTransactionDTO.get(0);
		
		return null;
	}
	
	private WireTransactionDTO updateStatusUsingConfirmationNumber(String confirmationNumber, String status) {
		WireTransactionDTO transactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_UPDATE_TRANSACTION_STATUS_PROC;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_featureId", Constants.FEATURE_DOMESTIC_WIRE_TRANSFER);
		requestParams.put("_status", status);
		requestParams.put("_confirmationNumber", confirmationNumber);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray jsonArr = CommonUtils.getFirstOccuringArray(jsonRsponse);
			if(jsonArr != null && jsonArr.length() > 0) {
				JSONObject record = jsonArr.getJSONObject(0);
				if(record != null && record.has("isSuccess") && "true".equals(record.get("isSuccess"))) {
					transactionDTO = new WireTransactionDTO();
					transactionDTO.setConfirmationNumber(confirmationNumber);
					transactionDTO.setStatus(status);
				}
			}
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured in updateStatusUsingConfirmationNumber" + jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured in updateStatusUsingConfirmationNumber" + exp).log();
			return null;
		}
		
		return transactionDTO;
	}
	
	@Override
	public boolean updateRequestId(String transactionId, String requestId) {

		List<WireTransactionDTO> wireTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("transactionId", transactionId);
		requestParams.put("requestId", requestId);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray wireJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			wireTransactionDTO = JSONUtils.parseAsList(wireJsonArray.toString(), WireTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the wiretransaction",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the wiretransaction",exp).log();
			return false;
		}
		
		if(wireTransactionDTO != null && wireTransactionDTO.size() != 0)
			return true;
		
		return false;
	}
	
	@Override
	public boolean updateCreatedts(String transactionId, String createdts) {

		List<WireTransactionDTO> wireTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("transactionId", transactionId);
		requestParams.put("createdts", createdts);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray wireJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			wireTransactionDTO = JSONUtils.parseAsList(wireJsonArray.toString(), WireTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the wiretransaction",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the wiretransaction",exp).log();
			return false;
		}
		
		if(wireTransactionDTO != null && wireTransactionDTO.size() != 0)
			return true;
		
		return false;
	}
	
	@Override
	public WireTransactionDTO fetchTranscationById(String transactionId, String customerId) {
		
		List<WireTransactionDTO> wireTransactionDTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_FETCH;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_transactionId", transactionId);
		requestParams.put("_wireFileExecution_id", "");
		requestParams.put("_customerId", customerId);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray wireJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			wireTransactionDTOs = JSONUtils.parseAsList(wireJsonArray.toString(), WireTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the wiretransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the wiretransaction",exp).log();
			return null;
		}
		
		if(wireTransactionDTOs != null && wireTransactionDTOs.size() != 0) {
			return wireTransactionDTOs.get(0);
		}
		
		return null;
	}

	@Override
	public WireTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber) {
		
		List<WireTransactionDTO> transactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_GET;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + confirmationNumber;
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray trJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), WireTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the internationalfundtransfers",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the internationalfundtransfers",exp).log();
			return null;
		}
		
		if(transactionDTO != null && transactionDTO.size() != 0)
			return transactionDTO.get(0);
		
		return null;
	}
	
	@Override
	public boolean deleteTransactionAtDBX(String transactionId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_DELETE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		
		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while deleting the wiretransaction",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while deleting the wiretransaction",exp).log();
			return false;
		}
		
		return false;	
	}
	
	@Override
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request) {
		ApprovalQueueBusinessDelegate approvalDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);			
		String customerId = CustomerSession.getCustomerId(customer);
		
		WireTransactionDTO transactionDTO = fetchTransactionById(transactionId, request);
		if(transactionDTO == null) {
			alert.prepareError("Failed to fetch the wire transaction entry from table: ").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			return;
		}
		/*if(! transactionDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {
			alert.prepareError("Transaction is not Approved ").log();
			return;
		}*/
		
		String confirmationNumber = transactionDTO.getConfirmationNumber();
		String companyId = transactionDTO.getCompanyId();
		String requestId = transactionDTO.getRequestId();
		String date = application.getServerTimeStamp();
		
		try {
			transactionDTO.setAmount(Double.parseDouble(transactionDTO.getTransactionAmount()));
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return;
		}
		
		//Transaction limit checks on amount and featureactionId
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(transactionDTO.getCreatedby());
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(transactionDTO.getFromAccountNumber());
		transactionStatusDTO.setAmount(transactionDTO.getAmount());
		transactionStatusDTO.setStatus(TransactionStatusEnum.APPROVED);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(confirmationNumber);
		transactionStatusDTO.setServiceCharge(null);
		transactionStatusDTO.setTransactionCurrency(null);
		
		transactionStatusDTO = approvalDelegate.validateForApprovals(transactionStatusDTO, request);
		if(transactionStatusDTO == null) {			
			alert.prepareError("Failed to validate limits ").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), request);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Failed to validate limits Before executing at core", customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			alert.prepareError("Error occured while validating limits").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), request);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), transactionStatusDTO.getDbpErrMsg(), customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		if(!TransactionStatusEnum.SENT.getStatus().equals(transactionStatus.getStatus())) {
			alert.prepareError("Not a valid status").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), request);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Not a valid status: " + transactionStatusDTO.getStatus(), customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		transactionDTO = approveTransaction(transactionId, request,transactionDTO.getFrequencyType());
		
		String referenceId = null;
		
		String reason = null;
		if(transactionDTO == null) {
			reason = "Failed at backend";
		}
		else if(transactionDTO.getDbpErrMsg() != null && !transactionDTO.getDbpErrMsg().isEmpty()) {
			reason = transactionDTO.getDbpErrMsg();
		}
		else {
			referenceId = transactionDTO.getReferenceId();
		}
		
		if(referenceId == null || referenceId.isEmpty()) {
			alert.prepareError("create or edit transaction failed ").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), request);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Create transaction failed at backend", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
		}
		else {
			updateStatus(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), referenceId);
		}

		// ADP-7058 update additional meta data
		try{
			approvalDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
	}
	
	private String deriveSubTypeForWireTransfer(JsonObject requestPayload, String serviceName) {

		String wireAccountType = "";
		if(requestPayload.has("payeeId")) {
			if (serviceName.equals("DOMESTIC_WIRE_TRANSFER_CREATE")) {
				return "REG_DOM_WIRE_TRANSFER";
			} else {
				return "REG_INTERNATIONAL_WIRE_TRANSFER";
			}
		} else {
			if(requestPayload.has("wireAccountType")) {	
				wireAccountType = requestPayload.get("wireAccountType").getAsString();
			}
			if (wireAccountType.equalsIgnoreCase("Domestic")) {
				return "NON_REG_DOM_WIRE_TRANSFER";
			} else {
				return "NON_REG_INTERNATIONAL_WIRE_TRANSFER";
			}
		}
	}

	@Override
	public void auditloggingforWireTransfers(DataControllerRequest request, DataControllerResponse response,
			Result result, Object[] inputArray, JSONObject bulkWireTransactionJSON,
			TransactionStatusEnum transactionStatus, Double totalAmount, String requestId, String referenceId,
			JsonObject requestPayload, String serviceName) {
		
		try {
			String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
			if (enableEvents == null || enableEvents.equalsIgnoreCase("false")) return;
			
			String eventSubType = deriveSubTypeForWireTransfer(requestPayload,serviceName);

			ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);

			@SuppressWarnings("unchecked")
			Map<String, String> inputParam = (HashMap<String, String>) inputArray[1];
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
			String fromAccountNumber = inputParam.get("fromAccountNumber");
			String status = "SID_EVENT_FAILURE";

			//Adding common custom params for every log
			JsonObject customparams = new JsonObject();
			customparams.addProperty("fromAccountNumber",fromAccountNumber);
			customparams.addProperty("createdBy",CustomerSession.getCustomerName(customer));//12601

			String dtFormat = "yyyy-MM-dd'T'hh:mm:ss";
			SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
			Date currdate = new Date();
			String date = formatter.format(currdate);
			customparams.addProperty("transactionDate",date);
			
			JsonObject resultObject = null;
			if(bulkWireTransactionJSON == null)
			{
				resultObject = new JsonObject();
			}
			else{
				resultObject = new JsonParser().parse(bulkWireTransactionJSON.toString()).getAsJsonObject();
				
				String wireFileExecution_id = resultObject.get("wireFileExecution_id").toString();
				String wireTemplateExecution_id = resultObject.get("wireTemplateExecution_id").toString();
				
				if((Integer.parseInt(wireFileExecution_id) == 0) && (Integer.parseInt(wireTemplateExecution_id) == 0)) {
					resultObject.remove("wireFileExecution_id");
					resultObject.remove("wireTemplateExecution_id");
				}
				else if((Integer.parseInt(wireFileExecution_id) != 0) && (Integer.parseInt(wireTemplateExecution_id) == 0)){
					resultObject.remove("wireTemplateExecution_id");
				}
				else {
					resultObject.remove("wireFileExecution_id");
				}
				
			}

			List<String> approvers ;

			switch (transactionStatus) {
			case SENT:        
				if(referenceId == null || "".equals(referenceId)) {
					resultObject = ErrorCodeEnum.ERR_12601.setErrorCode(resultObject);
					customparams.add("Response",resultObject);
					status = "SID_EVENT_FAILURE" ;
					break;
				}
				else 
				{
					resultObject.addProperty("referenceId", referenceId);
					customparams.add("Response",resultObject);
					customparams.addProperty("approvers","pre-approved");
					customparams.addProperty("approvedBy","pre-approved");
					customparams.addProperty("status",transactionStatus.getStatus());
					customparams.addProperty("message",transactionStatus.getMessage());
					status = "SID_EVENT_SUCCESS";
					break;
				}
			case PENDING:

				resultObject.addProperty("referenceId", referenceId);
				resultObject.addProperty("status",transactionStatus.getStatus());
				resultObject.addProperty("message",transactionStatus.getMessage());
				customparams.add("Response",resultObject);
				approvers = approversBusinessDelegate.getRequestApproversList(requestId);
				customparams.addProperty("approvers",approvers.toString());
				status = "SID_EVENT_SUCCESS";
				eventSubType = Constants.PENDING_APPROVAL_ + eventSubType;
				break;

			case DENIED_AD_MAX_TRANSACTION:
				resultObject = ErrorCodeEnum.ERR_12501.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;
				
			case DENIED_AD_DAILY:
				resultObject = ErrorCodeEnum.ERR_12502.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;	
				
			case DENIED_AD_WEEKLY:
				resultObject = ErrorCodeEnum.ERR_12503.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;

			case DENIED_MAX_TRANSACTION:
				resultObject = ErrorCodeEnum.ERR_12504.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;

			case DENIED_DAILY:
				resultObject = ErrorCodeEnum.ERR_12505.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;

			case DENIED_WEEKLY:
				resultObject = ErrorCodeEnum.ERR_12506.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;

			case DENIED_INVALID_APPROVAL_MATRIX:
				resultObject = ErrorCodeEnum.ERR_12507.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;
			
			case DENIED_MIN_TRANSACTION:
				resultObject = ErrorCodeEnum.ERR_12512.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;	

			default:
				break;

			}
			if(isSMEUser) {
				customparams.addProperty("approvedBy", "N/A");
				customparams.addProperty("rejectedBy", "N/A");
			}

			AdminUtil.addAdminUserNameRoleIfAvailable(customparams, request);
			
			EventsDispatcher.dispatch(request, response, "MAKE_TRANSFER", eventSubType,
					"Transactions/POST(createTransfer)",status, null, CustomerSession.getCustomerName(customer), customparams);
		}
		catch(Exception err) {
			alert.prepareError("error while logging the wire transfer: ",err).log(); 
			return;
		}
		
	}

	@Override
	public List<ApprovalRequestDTO> fetchWireTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr) {
		
		Set<String> wireTransIds = new HashSet<String>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();

		if (CollectionUtils.isEmpty(requests))
			return transactions;

		for (BBRequestDTO bBRequestDTO : requests) {
			if(StringUtils.isNotBlank(bBRequestDTO.getTransactionId()))
				wireTransIds.add(bBRequestDTO.getTransactionId());
		}

		String filter = "";
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			filter = "transactionId" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "transactionId" + DBPUtilitiesConstants.EQUAL, wireTransIds);
			transactions = fetchWireTransactionsForApprovalInfo(filter, dcr);
			transactions = (new FilterDTO()).merge(transactions, requests, "transactionId=transactionId", "requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		else {
			filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, wireTransIds);
			transactions = fetchWireTransactionsForApprovalInfo(filter,dcr);
			List<ApprovalRequestDTO> backendData = backendDelegate.fetchBackendTransactionsForApproval(wireTransIds, dcr);
			if(CollectionUtils.isNotEmpty(backendData)) {
				transactions = (new FilterDTO()).merge(transactions, backendData,"confirmationNumber=transactionId", "");
			}
			transactions = (new FilterDTO()).merge(transactions, requests, "confirmationNumber=transactionId", "transactionId,requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		
		
		return transactions;
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchWireTransactionsForApprovalInfo(String filter, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();

		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			String wireTransactionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(wireTransactionResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			transactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch wireTransactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching wireTransactions: ", e).log();
		}
		
		return transactions;
	}
	
	@Override
	public WireTransactionDTO createPendingTransaction(WireTransactionDTO input, DataControllerRequest request) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			input.setReferenceId(Constants.REFERENCE_KEY + input.getTransactionId());
			return input;
		}
		input.setTransactionId(null);
		
		WireTransactionBackendDTO wireBackendDTO = new WireTransactionBackendDTO();
		wireBackendDTO = wireBackendDTO.convert(input);
		
		return backendDelegate.createPendingTransaction(wireBackendDTO, request);
	}

	@Override
	public WireTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			WireTransactionBackendDTO backendObj = new WireTransactionBackendDTO();
			WireTransactionDTO dbxObj = fetchTransactionById(referenceId, request);
			backendObj = backendObj.convert(dbxObj);
			try {
				backendObj.setAmount(Double.parseDouble(backendObj.getTransactionAmount()));
			} catch (Exception e) {
				alert.prepareError("Invalid amount value", e).log();
				return null;
			}
			String confirmationNumber = dbxObj.getConfirmationNumber();
			
			if(!StringUtils.isEmpty(confirmationNumber) && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
				backendObj.setTransactionId(dbxObj.getConfirmationNumber());
				return backendDelegate.editTransaction(backendObj, request);
			}
			else {
				return backendDelegate.createTransactionWithoutApproval(backendObj, request);
			}
		}
		updateStatus(referenceId, TransactionStatusEnum.APPROVED.getStatus(), null);
		return backendDelegate.approveTransaction(referenceId, request,frequency);
	}

	@Override
	public WireTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request) {
		if(!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), null);
		}
		
		WireTransactionDTO transactionObj = fetchTransactionById(referenceId, request);
		
		if(transactionObj == null) {
			alert.prepareError("Failed to fetch the transaction entry from table: ").log();
			updateStatus(referenceId, TransactionStatusEnum.FAILED.getStatus(), null);
			return null;
		}
		
		updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), null);
		return backendDelegate.rejectTransaction(referenceId, transactionObj.getTransactionType(), request, transactionObj.getFrequencyType());
	}

	@Override
	public WireTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request) {
		if (!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(),null);
		}
		WireTransactionDTO transactionObj = fetchTransactionById(referenceId, request);
		
		if(transactionObj == null) {
			alert.prepareError("Failed to fetch the transaction entry from table: ").log();
			updateStatus(referenceId, TransactionStatusEnum.FAILED.getStatus(), null);
			return null;
		}
		
		updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), null);
		return backendDelegate.withdrawTransaction(referenceId, transactionObj.getTransactionType(), request, transactionObj.getFrequencyType());
	}
	
	
	@Override
	public WireTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest request) {

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			return fetchTranscationById(referenceId, customerId);
		}
		
		WireTransactionDTO backendData = backendDelegate.fetchTransactionById(referenceId, request);
		WireTransactionDTO dbxData = fetchExecutedTranscationEntry(referenceId);
		
		if(backendData == null || StringUtils.isEmpty(backendData.getDbpErrMsg())) {
			return dbxData;
		}

		return (new FilterDTO()).merge( Arrays.asList(dbxData), Arrays.asList(backendData),"confirmationNumber=transactionId", "").get(0);
	}
}
