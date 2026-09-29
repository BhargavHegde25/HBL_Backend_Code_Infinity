package com.temenos.dbx.product.achservices.businessdelegate.impl;

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

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHTransactionBackendDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionRecordDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionSubRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRequestTypeDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.BillPayTransactionBusinessDelegateImpl;

public class ACHTransactionBusinessDelegateImpl implements ACHTransactionBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	ACHCommonsBusinessDelegate achCommons = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
	ACHTransactionBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ACHTransactionBackendDelegate.class);
	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);

	@Override
	public ACHTransactionDTO createTransactionAtDBX(ACHTransactionDTO achTransactionDTO) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_CREATE;

		String createResponse = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(achTransactionDTO).toString(), String.class,
					Object.class);
			requestParameters.remove("transaction_id");
			if( requestParameters.get("template_id").equals(ACHConstants.TEMPLATE_ID_DEFAULT ) ) {
				requestParameters.remove("template_id");
			}
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			achTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), ACHTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create ach transaction entry into achtransaction table: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create ach transaction entry: " + e).log();
			return null;
		}

		return achTransactionDTO;
	}
	
	@Override
	public boolean updateRequestId(String achTransaction_id, String requestId) {

		ACHTransactionDTO achTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("transaction_id", achTransaction_id);
		requestParams.put("requestId", requestId);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject response = new JSONObject(updateResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			achTransactionDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ACHTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured while updating the achtransaction table",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while updating the achtransaction table",exp).log();
			return false;
		}
		
		if(achTransactionDTO == null )
			return false;
		
		return true;
	}
	
	@Override
	public boolean updateConfirmationNumber(long achTransaction_id, String confirmationNumber) {

		ACHTransactionDTO achTransactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("transaction_id", achTransaction_id);
		requestParams.put("confirmationNumber", confirmationNumber);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject response = new JSONObject(updateResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			achTransactionDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ACHTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured while updating the achtransaction table",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while updating the achtransaction table",exp).log();
			return false;
		}
		
		if(achTransactionDTO == null )
			return false;
		
		return true;
	}	

	@Override
	public ACHTransactionDTO updateStatus(String transactionId, String status, String confirmationNumber) {

		if(application.getIsStateManagementAvailableFromCache()) {
			return updateStatusUsingConfirmationNumber(transactionId, status);
		}
		else {
			return updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
		}
	}

	@Override
	public ACHTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_UPDATE;

		String createResponse = null;
		ACHTransactionDTO achTransactionDTO = null;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		requestParameters.put("transaction_id", transactionId);
		requestParameters.put("status", status);
		requestParameters.put("confirmationNumber", confirmationNumber);
		
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			achTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), ACHTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update ach transaction entry into achtransaction table: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update ach transaction entry: " + e).log();
			return null;
		}

		return achTransactionDTO;
	}
	
	private ACHTransactionDTO updateStatusUsingConfirmationNumber(String confirmationNumber, String status) {
		ACHTransactionDTO transactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_UPDATE_TRANSACTION_STATUS_PROC;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_featureId", Constants.FEATURE_ACH_PAYMENT);
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
					transactionDTO = new ACHTransactionDTO();
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
	public ACHTransactionRecordDTO createTransactionRecord(ACHTransactionRecordDTO ACHTransactionRecordDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_RECORD_CREATE;

		String createResponse = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(ACHTransactionRecordDTO).toString(), String.class,
					Object.class);
			requestParameters.remove("transactionRecord_id");
			if(ACHTransactionRecordDTO.getTemplateRequestType_id() != Long.parseLong(ACHConstants.TEMPLATE_REQUEST_TYPE_FEDERAL_TAX)) {
				requestParameters.remove("taxType_id");
			}			
			
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			ACHTransactionRecordDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), ACHTransactionRecordDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create ach transaction entry into achtransactionrecord table: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create ach transaction record entry: " + e).log();
			return null;
		}

		return ACHTransactionRecordDTO;
	}

	@Override
	public ACHTransactionSubRecordDTO createTransactionSubRecord(
			ACHTransactionSubRecordDTO achTransactionSubRecordDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_SUBRECORD_CREATE;

		String createResponse = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(achTransactionSubRecordDTO).toString(), String.class,
					Object.class);
			requestParameters.remove("transactionSubRecord_id");
			
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			achTransactionSubRecordDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), ACHTransactionSubRecordDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create ach transaction entry into achtransactionsubrecord table: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create ach transaction sub record entry: " + e).log();
			return null;
		}

		return achTransactionSubRecordDTO;
	}

	
	@Override
	public Result createTransactionRecordAndSubRecords(List<ACHTransactionRecordDTO> records) {
		
		Result result = new Result();
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_RECORD_SUBRECORD_CREATE;
		
		String createResponse = null;
		
		Map<String, Object> requestParams = _getMapForTransactionRecordsAndSubRecords(records);
		
		try {
			
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(createResponse);
			
			result = JSONToResult.convert(responseObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while creating an ACH template",exp).log();
			return null;
		}
		return result;
	}


	@Override
	public List<ACHTransactionRecordDTO> fetchTransactionRecords(String transaction_id) {
		List<ACHTransactionRecordDTO> resultDTO = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_ACH_TRANSACTION_RECORDS_PROC;
		HashMap<String, Object> paramsToFetchRecords = new HashMap<>();
		paramsToFetchRecords.put(ACHConstants.TRANSACTION_ID,transaction_id);
		try {

			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(paramsToFetchRecords).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray transactionJsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			resultDTO = JSONUtils.parseAsList(transactionJsonArray.toString(), ACHTransactionRecordDTO.class);
			
			for(int i =0; i<resultDTO.size(); i++) {
				ACHTransactionRecordDTO record = resultDTO.get(i);
				record.setSubRecords(fetchTransactionSubRecords(record.getTransactionRecord_id()+""));
				resultDTO.set(i, record);
			}
		}
		catch(Exception e){
			alert.prepareError("Exception occurred while fetching ACH Transaction records",e).log();
			return null;
		}

		return resultDTO;

	}

	@Override
	public List<ACHTransactionSubRecordDTO> fetchTransactionSubRecords(String transactionRecord_id) {

		List<ACHTransactionSubRecordDTO> resultDTO = null;
		HashMap<String, Object> paramsToFetchSubRecords = new HashMap<>();
		paramsToFetchSubRecords.put(ACHConstants.TRANSACTION_RECORD_ID,transactionRecord_id);
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_ACH_TRANSACTION_SUBRECORDS_PROC;
		try {

			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(paramsToFetchSubRecords).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray transactionJsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			resultDTO = JSONUtils.parseAsList(transactionJsonArray.toString(), ACHTransactionSubRecordDTO.class);
		}
		catch (Exception e){
			alert.prepareError("Exception occurred while fetching ACH Transaction sub-records",e).log();
			return null;
		}

		if(resultDTO != null && resultDTO.size() != 0)
			return resultDTO;
		return null;
	}

	/*
	 *@author KH2626
	 *@version 1.0
	 *@param listof record to create input string map for create procedure
	***/
	private Map<String, Object> _getMapForTransactionRecordsAndSubRecords(List<ACHTransactionRecordDTO> records) {

		Map<String,Object> requestParam = new HashMap<String, Object>();
		
		String recordValues = _getRecordsValue(records);
		String subRecordValues = _getSubRecordsValue(records);
		
		requestParam.put("_recordvalues", recordValues);
		requestParam.put("_subRecordvalues", subRecordValues);
		
		return requestParam;
	}

	/*
	 *@author KH2626
	 *@version 1.0
	 *@param list of record to create input string for create procedure
	***/
	private String _getRecordsValue(List<ACHTransactionRecordDTO> records) {
		
		StringBuilder recordValues = new StringBuilder("");
		int recordSize = records.size();
		
		for(int i=0; i<recordSize; i++) {
			
			ACHTransactionRecordDTO record = records.get(i);		
			
			String recordName = record.getRecord_Name();
			recordName = (recordName == null) ? recordName : "\""+recordName+"\"";
			
			String additionalInfo = record.getAdditionalInfo();
			additionalInfo = (additionalInfo == null) ? additionalInfo : "\""+additionalInfo+"\"";
			
			String detailId = record.getDetail_id();
			detailId = (detailId == null) ? detailId : "\""+detailId+"\"";
			
			String abatrcNumber = record.getAbstractNumber() == null ? record.getAbatrcNumber() : record.getAbstractNumber();
			abatrcNumber = (abatrcNumber == null) ? abatrcNumber : "\""+ abatrcNumber + "\""; 
			
			Long taxTypeId = record.getTaxType_id();
			taxTypeId = (taxTypeId == 0)?null:taxTypeId;
			
			recordValues.append(recordName+",");
			recordValues.append(record.getToAccountNumber()+",");
			recordValues.append(abatrcNumber+",");
			recordValues.append(detailId+",");
			recordValues.append(record.getAmount()+",");
			recordValues.append(additionalInfo+",");
			recordValues.append(record.getEin()+",");
			recordValues.append(record.getIsZeroTaxDue()+",");
			recordValues.append(record.getTransaction_id()+",");
			recordValues.append(taxTypeId+",");
			recordValues.append(record.getTemplateRequestType_id()+",");
			recordValues.append(record.getToAccountType());
			
			if( i < recordSize-1) {
				recordValues.append("|");
			}
		}
		return recordValues.toString();
	}

	/*
	 *@author KH2317
	 *@version 1.0
	 *@param list of sub record to create input string for create procedure
	***/
	private String _getSubRecordsValue(List<ACHTransactionRecordDTO> records) {

		StringBuilder subRecordValues = new StringBuilder("");
		int recordSize = records.size();
		
		for(int i=0; i<recordSize; i++) {
			
			List<ACHTransactionSubRecordDTO> subRecords = records.get(i).getSubRecords();
			if(subRecords == null) {
				subRecordValues.append(";");
			}
			else {
				int subRecordSize = subRecords.size();
				
				for(int j=0; j<subRecordSize; j++) {
					
					ACHTransactionSubRecordDTO subRecord = subRecords.get(j);
					
					subRecordValues.append(subRecord.getAmount()+",");
					subRecordValues.append(subRecord.getTaxSubCategory_id());
					
					if( j < subRecordSize-1) {
						subRecordValues.append(";");
					}
				}			
			}
			if( i < recordSize-1) {
				subRecordValues.append("|");
			}				
		}
		return subRecordValues.toString();
	}

	@Override
	public List<ACHTransactionDTO> getACHTransactions(FilterDTO filters, String customerId,
			Object transactionId, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_ACH_TRANSACTION_PROC;
		
		ACHTransactionBackendDelegate achTransactionBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				   .getFactoryInstance(BackendDelegateFactory.class)
				   .getBackendDelegate(ACHTransactionBackendDelegate.class);
		
		List<ACHTransactionDTO> achTransactions = new ArrayList<ACHTransactionDTO>();
		List<ACHTransactionDTO> statusDTOs = new ArrayList<ACHTransactionDTO>();
		Map<String, Object> requestParams = null;
		Map<String, String> statusMap = new HashMap<String, String>();
		
		List<String> paymentIds = new ArrayList<String>();
		List<String> collectionIds = new ArrayList<String>();
		
		String fetchResponse = "";
		try {
			requestParams = JSONUtils.parseAsMap(JSONUtils.stringify(filters), String.class, Object.class);
			requestParams.put(Constants._CUSTOMER_ID, customerId);
			requestParams.put(Constants._TRANSACTION_ID, transactionId);
			
			fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray records = CommonUtils.getFirstOccuringArray(responseObj);
			achTransactions = JSONUtils.parseAsList(records.toString(), ACHTransactionDTO.class);
			boolean stateManagementAvailable = application.getIsStateManagementAvailableFromCache();
			for(int i=0; i < achTransactions.size(); i++) {
				ACHTransactionDTO transcation = achTransactions.get(i);
				String transactionType = transcation.getTransactionTypeName();
				String confirmationNumber = transcation.getConfirmationNumber();
				if (stateManagementAvailable && StringUtils.isNotEmpty(confirmationNumber)) {
					transcation.setTransaction_id(confirmationNumber);
				}
				
				if(confirmationNumber != null && !confirmationNumber.isEmpty() && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
					if(ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT.equals(transactionType)) {
			        	paymentIds.add(confirmationNumber);
			        } 
			        else if(ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION.equals(transactionType)) {
			        	collectionIds.add(confirmationNumber);
			        }
				}
			}
			
			if(paymentIds.size() > 0) {
				statusDTOs = achTransactionBackendDelegate.fetchPaymentStatus(paymentIds, dcr);
			}
			
			if(collectionIds.size() > 0) {
				statusDTOs.addAll(achTransactionBackendDelegate.fetchCollectionStatus(collectionIds, dcr));
			}
			
			for(int i=0; i< statusDTOs.size(); i++) {
				ACHTransactionDTO statusDTO = statusDTOs.get(i);
				statusMap.put(statusDTO.getConfirmationNumber(), statusDTO.getStatus());
			}
			
			for(int i=0; i < achTransactions.size() && statusMap.size() > 0; i++) {
				ACHTransactionDTO transaction = achTransactions.get(i);
				String status = transaction.getStatus();
				String confirmationNumber = transaction.getConfirmationNumber();
				
				if(confirmationNumber != null && !confirmationNumber.isEmpty()) {
					status = (statusMap.get(confirmationNumber) == null || statusMap.get(confirmationNumber).isEmpty())
							? status : statusMap.get(confirmationNumber);
					transaction.setStatus(status);
					achTransactions.set(i, transaction);
					statusMap.remove(confirmationNumber);
				}
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured in delegate while fetching ach transactions", exp).log();
			return null;
		}
		return achTransactions;
	}
	
	@Override
	public ACHTransactionDTO fetchTranscationEntry(String transactionId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_GET;
		
		List<ACHTransactionDTO> achTransactions = null;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		String filter = "transaction_id" + DBPUtilitiesConstants.EQUAL + transactionId;
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();;
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			achTransactions = JSONUtils.parseAsList(jsonArray.toString(), ACHTransactionDTO.class);
			
			if(achTransactions != null && achTransactions.size() != 0) {
				ACHTransactionDTO transaction = achTransactions.get(0);
				transaction.setTransactionRecords(fetchTransactionRecords(transaction.getTransaction_id()));
				return transaction;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured in delegate while fetching ach transactions", exp).log();
			return null;
		}
		return null;
	}
	
	@Override
	public boolean deleteTransactionAtDBX(String transactionId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_DELETE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transaction_id", transactionId);
		
		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while deleting the billpaytransaction",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while deleting the billpaytransaction",exp).log();
			return false;
		}
		
		return false;	
	}
	
	@Override
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest dcr) {
		
		ApprovalQueueBusinessDelegate approvalDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcr);			
		String customerId = CustomerSession.getCustomerId(customer);
		
		ACHTransactionDTO transactionDTO = fetchTransactionById(transactionId, dcr);
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
		
		if(transactionDTO.getEffectiveDate() == null) {
			alert.prepareError("Transaction does not have an effective date ").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Transaction does not have an effective date to execute", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		String date = transactionDTO.getEffectiveDate();
		
		//Transaction limit checks on amount and featureactionId
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(transactionDTO.getCreatedby());
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(transactionDTO.getFromAccount());
		transactionStatusDTO.setAmount(transactionDTO.getTotalAmount());
		transactionStatusDTO.setStatus(TransactionStatusEnum.APPROVED);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(confirmationNumber);
		transactionStatusDTO.setServiceCharge(null);
		transactionStatusDTO.setTransactionCurrency(null);
		
		transactionStatusDTO = approvalDelegate.validateForApprovals(transactionStatusDTO, dcr);
		if(transactionStatusDTO == null) {			
			alert.prepareError("Failed to validate limits ").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Failed to validate limits Before executing at core", customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			alert.prepareError("Error occured while validating limits").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
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
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Not a valid status: " + transactionStatusDTO.getStatus(), customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		transactionDTO = approveTransaction(transactionId, dcr);
		
		String referenceId = null;
		
		String reason = null;
		if(transactionDTO == null) {
			reason = "Failed at backend";
		}
		else if(transactionDTO.getDbpErrMsg() != null && !transactionDTO.getDbpErrMsg().isEmpty()) {
			reason = transactionDTO.getDbpErrMsg();
		}
		else {
			referenceId = transactionDTO.getReferenceID();
		}
		
		if(referenceId == null || referenceId.isEmpty()) {
			alert.prepareError("create or edit transaction failed ").log();
			updateStatus(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber == null ? reason : confirmationNumber);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Create transaction failed at backend", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
		}
		else {
			updateStatus(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), referenceId);
		}
		// ADP-7058 update additional meta data
		try{
			approvalDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), dcr);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
	}
	
	public JSONObject fetchTranscationEntryWithRecordsAndSubRecords(String transactionId)
	{
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHTRANSACTIONS_FETCH_RECORDS_SUBRECORDS;

		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_transactionId", transactionId);
		try {
			String transactionResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject transactionResponseJson = new JSONObject(transactionResponse);
			if (transactionResponseJson.has("records")
					&& transactionResponseJson.getJSONArray("records").length() > 0) {
				diagnostic.prepareInfo("transaction fetched successfully").log();		
				return transactionResponseJson;
			} else {
				alert.prepareError("Unable to fetch transaction response").log();
				return null;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch transaction details ",e).log();
			
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchTranscationEntryWithRecordsAndSubRecords method: ",e).log();
			
			return null;
		}
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchACHTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr) {
		
		Set<String> achTransactionIds = new HashSet<String>();
		List<ApprovalRequestDTO> achTransactions = new ArrayList<ApprovalRequestDTO>();
		
		if(CollectionUtils.isEmpty(requests))
			return achTransactions;
		
		for(BBRequestDTO bBRequestDTO : requests) {
			if(StringUtils.isNotBlank(bBRequestDTO.getTransactionId()))
				achTransactionIds.add(bBRequestDTO.getTransactionId());
		}

		String filter = "";
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			filter = "transaction_id" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "transaction_id" + DBPUtilitiesConstants.EQUAL, achTransactionIds);
			achTransactions = fetchACHTransactionsForApprovalInfo(filter, dcr);
			List<BBTemplateRequestTypeDTO> types = achCommons.fetchTemplateRequestTypes();
			achTransactions = (new FilterDTO()).merge(achTransactions, types, "fileTypeId=templateRequestType_id", "templateRequestTypeName");
			
			achTransactions = (new FilterDTO()).merge(achTransactions, requests, "transactionId=transactionId", "requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		else {
			filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, achTransactionIds);
			achTransactions = fetchACHTransactionsForApprovalInfo(filter,dcr);
			List<ApprovalRequestDTO> backendData = backendDelegate.fetchBackendTransactionsForApproval(achTransactionIds, dcr);
			if(CollectionUtils.isNotEmpty(backendData)) {
				achTransactions = (new FilterDTO()).merge(achTransactions, backendData,"confirmationNumber=transactionId", "");
			}
			List<BBTemplateRequestTypeDTO> types = achCommons.fetchTemplateRequestTypes();
			achTransactions = (new FilterDTO()).merge(achTransactions, types, "fileTypeId=templateRequestType_id", "templateRequestTypeName");
			
			achTransactions = (new FilterDTO()).merge(achTransactions, requests, "confirmationNumber=transactionId", "transactionId,requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		


		return achTransactions;
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchACHTransactionsForApprovalInfo(String filter, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> achTransactions = new ArrayList<ApprovalRequestDTO>();
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			achTransactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch ACH Transactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching ACH Transactions: ", e).log();
		}

		return achTransactions;
	}

	@Override
	public ACHTransactionDTO createPendingTransaction(ACHTransactionDTO input,
			DataControllerRequest request) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			input.setReferenceID(Constants.REFERENCE_KEY + input.getTransaction_id());
			return input;
		}
		input.setTransaction_id(null);
		return backendDelegate.createPendingTransaction(input, request);
	}

	@Override
	public ACHTransactionDTO approveTransaction(String referenceId, DataControllerRequest request) {

		ACHTransactionDTO achTransactionDTO = new ACHTransactionDTO();

		if (!application.getIsStateManagementAvailableFromCache()) {
			ACHTransactionDTO dbxObj = fetchTransactionById(referenceId, request);
			achTransactionDTO = dbxObj;
			String confirmationNumber=dbxObj.getConfirmationNumber();
		
			if(!StringUtils.isEmpty(confirmationNumber) && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
				achTransactionDTO.setTransaction_id(dbxObj.getConfirmationNumber());
				return backendDelegate.editTransaction(achTransactionDTO, request);
			}
			else {
				return backendDelegate.createTransactionWithoutApproval(achTransactionDTO, request);
			}
		}
		updateStatus(referenceId, TransactionStatusEnum.APPROVED.getStatus(), "Transaction Approved");
		return backendDelegate.approveTransaction(referenceId, request);
	}

	@Override
	public ACHTransactionDTO rejectTransaction(String referenceId, String transactionType,
			DataControllerRequest request) {
		if (!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), "Transaction Rejected");
		}
		return backendDelegate.rejectTransaction(referenceId, transactionType, request);
	}

	@Override
	public ACHTransactionDTO withdrawTransaction(String referenceId, String transactionType,
			DataControllerRequest request) {
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), "Transaction Withdrawn");
		}
		updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), "Transaction Withdrawn");
		return backendDelegate.withdrawTransaction(referenceId, transactionType, request);
	}

	@Override
	public ACHTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest request) {
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			return fetchTranscationEntry(referenceId);
		}
		
		ACHTransactionDTO backendData = backendDelegate.fetchTransactionById(referenceId, request);
		ACHTransactionDTO dbxData = fetchExecutedTranscationEntry(referenceId, null, null);
		
		return (new FilterDTO()).merge(Arrays.asList(dbxData),Arrays.asList(backendData),"confirmationNumber=Transaction_id", "").get(0);
	}
	
	@Override
	public ACHTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby) {
		
		List<ACHTransactionDTO> transactionDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_GET;
		
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
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), ACHTransactionDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the ach transactions",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the ach transactions",exp).log();
			return null;
		}
		
		if(transactionDTO != null && transactionDTO.size() != 0)
			return transactionDTO.get(0);
		
		return null;
	}
	
}