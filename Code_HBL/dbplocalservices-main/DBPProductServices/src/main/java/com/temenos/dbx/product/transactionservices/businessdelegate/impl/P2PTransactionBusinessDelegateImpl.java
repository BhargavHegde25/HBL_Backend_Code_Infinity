package com.temenos.dbx.product.transactionservices.businessdelegate.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
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
import com.temenos.dbx.product.transactionservices.backenddelegate.api.P2PTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;

/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link P2PTransactionBusinessDelegate}
 */

public class P2PTransactionBusinessDelegateImpl implements P2PTransactionBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	P2PTransactionBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(P2PTransactionBackendDelegate.class);
	
	@Override
	public  P2PTransactionDTO createTransactionAtDBX(P2PTransactionDTO p2pTransactionDTO) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_CREATE;
		String createResponse = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pTransactionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}
		
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);
			
			p2pTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), P2PTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create p2p transaction entry into p2ptransfers table: " , e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create p2p transaction entry: " , e).log();
			return null;
		}
		
		return p2pTransactionDTO;
	}
	
	@Override
	public  P2PTransactionDTO updateTransactionAtDBX(P2PTransactionDTO p2pTransactionDTO) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_UPDATE;

		String createResponse = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pTransactionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}
		
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);
			
			p2pTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), P2PTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create p2p transaction entry into p2ptransfers table: " , e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create p2p transaction entry: " , e).log();
			return null;
		}
		
		return p2pTransactionDTO;
	}
	
	@Override
	public P2PTransactionDTO updateStatus(String transactionId, String status, String confirmationNumber) {

		if(application.getIsStateManagementAvailableFromCache()) {
			return updateStatusUsingConfirmationNumber(transactionId, status);
		}
		else {
			return updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
		}
	}
	
	@Override
	public P2PTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber) {

		List<P2PTransactionDTO> p2pTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_UPDATE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		requestParams.put("status", status);
		requestParams.put("confirmationNumber", confirmationNumber);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray p2pJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			p2pTransactionDTO = JSONUtils.parseAsList(p2pJsonArray.toString(), P2PTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the p2ptransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the p2ptransaction",exp).log();
			return null;
		}
		
		if(p2pTransactionDTO != null && p2pTransactionDTO.size() != 0)
			return p2pTransactionDTO.get(0);
		
		return null;
	}
	
	private P2PTransactionDTO updateStatusUsingConfirmationNumber(String confirmationNumber, String status) {
		P2PTransactionDTO transactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_UPDATE_TRANSACTION_STATUS_PROC;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_featureId", Constants.FEATURE_P2P);
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
					transactionDTO = new P2PTransactionDTO();
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

		List<P2PTransactionDTO> p2pTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("transactionId", transactionId);
		requestParams.put("requestId", requestId);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray p2pJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			p2pTransactionDTO = JSONUtils.parseAsList(p2pJsonArray.toString(), P2PTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the p2ptransaction",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the p2ptransaction",exp).log();
			return false;
		}
		
		if(p2pTransactionDTO != null && p2pTransactionDTO.size() != 0)
			return true;
		
		return false;
	}
	
	@Override
	public P2PTransactionDTO fetchTranscationEntry(String transactionId) {
		
		List<P2PTransactionDTO> p2pTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_GET;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "transactionId" + DBPUtilitiesConstants.EQUAL + transactionId;
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray p2pJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			p2pTransactionDTO = JSONUtils.parseAsList(p2pJsonArray.toString(), P2PTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the p2ptransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the p2ptransaction",exp).log();
			return null;
		}
		
		if(p2pTransactionDTO != null && p2pTransactionDTO.size() != 0)
			return p2pTransactionDTO.get(0);
		
		return null;
	}
	
	@Override
	public boolean deleteTransactionAtDBX(String transactionId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_DELETE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		
		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while deleting the p2ptransaction",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while deleting the p2ptransaction",exp).log();
			return false;
		}
		
		return false;	
	}

	@Override
	public P2PTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby, String legalEntityId) {
		
		List<P2PTransactionDTO> transactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_GET;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + confirmationNumber;
		
		String innerFilter = "";

		if (CollectionUtils.isNotEmpty(companyIds))
			innerFilter = innerFilter + DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;

		if (StringUtils.isNotBlank(innerFilter) && StringUtils.isNotBlank(createdby))
			innerFilter = innerFilter + DBPUtilitiesConstants.OR;

		if (StringUtils.isNotBlank(createdby))
			innerFilter = innerFilter + "createdby" + DBPUtilitiesConstants.EQUAL + createdby;
        
		if (StringUtils.isNotBlank(legalEntityId))
			innerFilter = innerFilter + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.OPEN_BRACE + "legalEntityId" 
		    + DBPUtilitiesConstants.EQUAL + legalEntityId + DBPUtilitiesConstants.CLOSE_BRACE;
		
		if (StringUtils.isNotBlank(innerFilter))
			filter = filter + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.OPEN_BRACE + innerFilter
					+ DBPUtilitiesConstants.CLOSE_BRACE;

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
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), P2PTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the p2ptransfers",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the p2ptransfers",exp).log();
			return null;
		}
		
		if(transactionDTO != null && transactionDTO.size() != 0)
			return transactionDTO.get(0);
		
		return null;
		
	}
	
	@Override
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request) {
		
		ApprovalQueueBusinessDelegate approvalDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);			
		String customerId = CustomerSession.getCustomerId(customer);
		
		P2PTransactionDTO transactionDTO = fetchTransactionById(transactionId, request);
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
		String date = transactionDTO.getScheduledDate() == null ? 
				(transactionDTO.getProcessingDate() == null ? 
						(transactionDTO.getFrequencyStartDate() == null ? 
								application.getServerTimeStamp()
								: transactionDTO.getFrequencyStartDate())
						: transactionDTO.getProcessingDate()) 
				: transactionDTO.getScheduledDate();
		
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
		
		transactionDTO = approveTransaction(transactionId, request, transactionDTO.getFrequencyTypeId());
		
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
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
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

	@Override
	public List<ApprovalRequestDTO> fetchP2PTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr) {

		Set<String> P2PTransIds = new HashSet<String>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();

		if (CollectionUtils.isEmpty(requests))
			return transactions;

		for (BBRequestDTO bBRequestDTO : requests) {
			if(StringUtils.isNotBlank(bBRequestDTO.getTransactionId()))
				P2PTransIds.add(bBRequestDTO.getTransactionId());
		}

		String filter = "";
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			filter = "transactionId" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "transactionId" + DBPUtilitiesConstants.EQUAL, P2PTransIds);
			transactions = fetchP2PTransactionsForApprovalInfo(filter, dcr);
			transactions = (new FilterDTO()).merge(transactions, requests, "transactionId=transactionId", "requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		else {
			filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, P2PTransIds);
			transactions = fetchP2PTransactionsForApprovalInfo(filter,dcr);
			List<ApprovalRequestDTO> backendData = backendDelegate.fetchBackendTransactionsForApproval(P2PTransIds, dcr);
			if(CollectionUtils.isNotEmpty(backendData)) {
				transactions = (new FilterDTO()).merge(transactions, backendData,"confirmationNumber=transactionId", "");
			}
			transactions = (new FilterDTO()).merge(transactions, requests, "confirmationNumber=transactionId", "transactionId,requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		
		
		return transactions;
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchP2PTransactionsForApprovalInfo(String filter, DataControllerRequest dcr) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();

		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			String P2PResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(P2PResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			transactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch P2PTransactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching P2PTransactions: ", e).log();
		}
		
		return transactions;
	}

	@Override
	public P2PTransactionDTO createPendingTransaction(P2PTransactionDTO p2ptransferdto, DataControllerRequest request) {
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			p2ptransferdto.setReferenceId(Constants.REFERENCE_KEY + p2ptransferdto.getTransactionId());
			return p2ptransferdto;
		}
		p2ptransferdto.setTransactionId(null);
		P2PTransactionBackendDTO backendObj = new P2PTransactionBackendDTO();
		backendObj =  backendObj.convert(p2ptransferdto);
		return backendDelegate.createPendingTransaction(backendObj, request);
	}

	@Override
	public P2PTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			P2PTransactionBackendDTO backendObj = new P2PTransactionBackendDTO();
			P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
			P2PTransactionDTO dbxObj = p2pTransactionDelegate.fetchTransactionById(referenceId, request);
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
				return backendDelegate.editTransactionWithoutApproval(backendObj, request);
			}
			else {
				return backendDelegate.createTransactionWithoutApproval(backendObj, request);
			}
		}
		updateStatus(referenceId, TransactionStatusEnum.APPROVED.getStatus(), null);
		return backendDelegate.approveTransaction(referenceId, request, frequency);
	}

	@Override
	public P2PTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), null);
		}
		
		P2PTransactionDTO transactionObj = fetchTransactionById(referenceId, request);
		
		if(transactionObj == null) {
			alert.prepareError("Failed to fetch the transaction entry from table: ").log();
			updateStatus(referenceId, TransactionStatusEnum.FAILED.getStatus(), null);
			return null;
		}
		
		updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), null);
		return backendDelegate.rejectTransaction(referenceId, transactionObj.getTransactionType(), request, transactionObj.getFrequencyTypeId());
	}

	@Override
	public P2PTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), null);
		}
		P2PTransactionDTO transactionObj = fetchTransactionById(referenceId, request);
		
		if(transactionObj == null) {
			alert.prepareError("Failed to fetch the transaction entry from table: ").log();
			updateStatus(referenceId, TransactionStatusEnum.FAILED.getStatus(), null);
			return null;
		}
		
		updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), null);
		return backendDelegate.withdrawTransaction(referenceId, transactionObj.getTransactionType(), request, transactionObj.getFrequencyTypeId());
	}

	@Override
	public P2PTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest) {
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			return fetchTranscationEntry(referenceId);
		}
		
		P2PTransactionDTO backendData = backendDelegate.fetchTransactionById(referenceId, dataControllerRequest);
		P2PTransactionDTO dbxData = fetchExecutedTranscationEntry(referenceId, null, null, null);
		
		if(backendData == null || StringUtils.isEmpty(backendData.getDbpErrMsg())) {
			return dbxData;
		}
		
		return (new FilterDTO()).merge( Arrays.asList(dbxData),Arrays.asList(backendData),"confirmationNumber=transactionId", "").get(0);
	}
	
	@Override
	public P2PTransactionDTO editPendingTransaction(P2PTransactionDTO input, DataControllerRequest request) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			input.setReferenceId(input.getTransactionId());
			return input;
		}
		input.setTransactionId(null);
		P2PTransactionBackendDTO backenObj = new P2PTransactionBackendDTO();
		backenObj = backenObj.convert(input);
		return backendDelegate.editTransactionWithApproval(backenObj, request);
	}
}
