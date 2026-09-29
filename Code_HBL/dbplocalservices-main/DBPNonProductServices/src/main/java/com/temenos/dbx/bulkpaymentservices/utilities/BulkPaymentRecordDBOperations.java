package com.temenos.dbx.bulkpaymentservices.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;


public class BulkPaymentRecordDBOperations {

	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");
	public static final String DB_BULKPAYMENT_FILERECORD_CREATE = SCHEMA_NAME + "_bulkpaymentrecordmock_create";
	public static final String DB_BULKPAYMENT_FILERECORD_GET = SCHEMA_NAME + "_bulkpaymentrecordmock_get";
	public static final String DB_BULKPAYMENT_FILERECORD_DELETE = SCHEMA_NAME + "_bulkpaymentrecordmock_delete";
	public static final String DB_BULKPAYMENT_FILERECORD_UPDATE = SCHEMA_NAME + "_bulkpaymentrecordmock_update";


	public BulkPaymentRecordDTO createBulkPaymentRecordEntry(BulkPaymentRecordDTO bulkpaymentrecordDTO) {

		BulkPaymentRecordDTO bulkPaymentRecordDTO = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_CREATE;
		Map<String, Object> requestParameters;

		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkpaymentrecordDTO).toString(), String.class,
					Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}

		String createResponse = null;
		try {
			createResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			bulkPaymentRecordDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentRecordDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create bulk payment record", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Failed to create bulk payment record ", e).log();
			return null;
		}
		return bulkPaymentRecordDTO;
	}	
	
	public Result fetchOnGoingBulkPaymentRecords(List<String> companyIds, String batchMode, String createdby) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_GET;
		Result result = new Result();
		String filter = "";
		String response = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		if (CollectionUtils.isNotEmpty(companyIds))
			filter = DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;
		
		if(StringUtils.isNotEmpty(createdby)) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.OR;
			} 
			filter = filter + "createdBy" + DBPUtilitiesConstants.EQUAL + createdby;
		}
		if(StringUtils.isNotEmpty(filter)) {
			filter = DBPUtilitiesConstants.OPEN_BRACE + filter + DBPUtilitiesConstants.CLOSE_BRACE;
			filter = filter + DBPUtilitiesConstants.AND;
		}
		filter = filter + DBPUtilitiesConstants.OPEN_BRACE + "status" + DBPUtilitiesConstants.NOT_EQ + TransactionStatusEnum.EXECUTED.getStatus();
		filter = filter + DBPUtilitiesConstants.AND;
		filter = filter + "status" + DBPUtilitiesConstants.NOT_EQ + TransactionStatusEnum.FAILED.getStatus() + DBPUtilitiesConstants.CLOSE_BRACE;
		filter = filter + DBPUtilitiesConstants.AND;
		if(StringUtils.isEmpty(batchMode)) {
			filter = filter + DBPUtilitiesConstants.OPEN_BRACE +"batchMode" + DBPUtilitiesConstants.EQUAL+ "SINGLE";
			filter = filter + DBPUtilitiesConstants.OR;
			filter = filter + "batchMode" + DBPUtilitiesConstants.EQUAL+ "MULTI" + DBPUtilitiesConstants.CLOSE_BRACE;
		}
		if(StringUtils.isNotEmpty(batchMode)) {
			if(batchMode.equalsIgnoreCase("SINGLE-PO MULTI-PO")) {
					filter = filter + DBPUtilitiesConstants.OPEN_BRACE +"batchMode" + DBPUtilitiesConstants.EQUAL+ "SINGLE";
					filter = filter + DBPUtilitiesConstants.OR;
					filter = filter + "batchMode" + DBPUtilitiesConstants.EQUAL+ "MULTI" + DBPUtilitiesConstants.CLOSE_BRACE;
			}
			if(batchMode.equalsIgnoreCase("SINGLE-PO")) {
					filter = filter + DBPUtilitiesConstants.OPEN_BRACE + "batchMode" + DBPUtilitiesConstants.EQUAL+ "SINGLE" + DBPUtilitiesConstants.CLOSE_BRACE;
					}
			if(batchMode.equalsIgnoreCase("MULTI-PO")) {
				filter = filter + DBPUtilitiesConstants.OPEN_BRACE + "batchMode" + DBPUtilitiesConstants.EQUAL+ "MULTI" + DBPUtilitiesConstants.CLOSE_BRACE;
				}
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			result = JSONToResult.convert(response.toString());
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch ongoing bulk payment records ", e).log();
			return ErrorCodeEnum.ERR_21223.setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchOnGoingBulkPaymentRecords: ", e).log();
			return ErrorCodeEnum.ERR_21223.setErrorCode(result);
		}

		return result;
	}
	
	public BulkPaymentRecordDTO fetchBulkPaymentRecordDetailsById(String recordId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_GET;
		BulkPaymentRecordDTO recordDTO = null;
		String fetchResponse = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String filter = "recordId" + DBPUtilitiesConstants.EQUAL + recordId;	
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			fetchResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			recordDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentRecordDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch bulk payment records", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching bulkpayment records: ", e).log();
			return null;
		}
		return recordDTO;
	}
	
	public Result fetchBulkPaymentHistory(List<String> companyIds,String createdby) {
		Result result;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
		
		if (CollectionUtils.isNotEmpty(companyIds))
			filter = DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;
		
		if(StringUtils.isNotEmpty(createdby)) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.OR;
			} else {
				
			}
			filter = filter + "createdBy" + DBPUtilitiesConstants.EQUAL + createdby;
		}
		if(StringUtils.isNotEmpty(filter)) {
			filter = DBPUtilitiesConstants.OPEN_BRACE + filter + DBPUtilitiesConstants.CLOSE_BRACE;
			filter = filter + DBPUtilitiesConstants.AND;
		}
		filter = filter + DBPUtilitiesConstants.OPEN_BRACE;
		filter = filter + "status" + DBPUtilitiesConstants.EQUAL + TransactionStatusEnum.EXECUTED.getStatus();
		filter = filter + DBPUtilitiesConstants.OR;
		filter = filter + "status" + DBPUtilitiesConstants.EQUAL + TransactionStatusEnum.FAILED.getStatus();
		filter = filter + DBPUtilitiesConstants.CLOSE_BRACE;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			result = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResult();
			
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch bulk payment history", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching bulkpayment history: ", e).log();
			return null;
		}
		return result;
	}
	
	public Result cancelBulkPaymentRecord(String recordId, String comments, String cancellationreason) {

		Result result = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_UPDATE;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("comments", comments);
		requestParameters.put("cancellationreason", cancellationreason);
		requestParameters.put("status", TransactionStatusEnum.DISCARDED.getStatus());
	
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
			result= JSONToResult.convert(responseArray.getJSONObject(0).toString());
			
		} catch (JSONException e) {
			alert.prepareError("Failed to cancel bulk payment record", e).log(); 
			return ErrorCodeEnum.ERR_21235.setErrorCode(result); 
		} catch (Exception e) {
			alert.prepareError("Caught exception while cancelling bulkpayment record: ", e).log();
			return ErrorCodeEnum.ERR_21235.setErrorCode(result);
		}
		return result;
	}

	public Result updateBulkPaymentRecord(String recordId, String description, String fromAccount) {
		Result result = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_UPDATE;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("description", description);
		requestParameters.put("fromAccount", fromAccount);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
			result= JSONToResult.convert(responseArray.getJSONObject(0).toString());
			
		} catch (JSONException e) {
			alert.prepareError("Failed to update bulk payment record", e).log(); 
			return ErrorCodeEnum.ERR_21237.setErrorCode(result); 
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating bulkpayment record: ", e).log();
			return ErrorCodeEnum.ERR_21237.setErrorCode(result);
		}
		return result;

	}
	
	public Result updateRejectedBulkPaymentRecordStatus(String recordId,  String comments, String rejectionreason, String status) {
		Result result = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_UPDATE;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("comments", comments);
		requestParameters.put("rejectionreason", rejectionreason);
		requestParameters.put("status", status);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
			result= JSONToResult.convert(responseArray.getJSONObject(0).toString());
			
		} catch (JSONException e) {
			alert.prepareError("Failed to update bulk payment record", e).log(); 
			return ErrorCodeEnum.ERR_21237.setErrorCode(result); 
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating bulkpayment record: ", e).log();
			return ErrorCodeEnum.ERR_21237.setErrorCode(result);
		}
		return result;
	}

	public Result updateBulkPaymentRecordStatus(String recordId, String status) {
		Result result = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_UPDATE;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("status", status);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
			result= JSONToResult.convert(responseArray.getJSONObject(0).toString());
			
		} catch (JSONException e) {
			alert.prepareError("Failed to update bulk payment record", e).log(); 
			return ErrorCodeEnum.ERR_21237.setErrorCode(result); 
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating bulkpayment record: ", e).log();
			return ErrorCodeEnum.ERR_21237.setErrorCode(result);
		}
		return result;
	}
	
	public boolean updateBulkPaymentRecordPaymentStatus(String recordId, String paymentStatus) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_UPDATE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", recordId);
		requestParams.put("paymentStatus", paymentStatus);

		String editResponse = null;

		try {
			editResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(editResponse);
			if(responseObj.getInt("opstatus") == 0 && responseObj.getInt("httpStatusCode") == 0) {
				return true;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to update bulk payment status", e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at while updating bulk payment Record", e).log();
			return false;
		}
		return false;	
	}
	
	public boolean updateBulkPaymentRecordAmountAndTotalTransactions(String recordId, Double amount, Double transactionCountChange) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILERECORD_UPDATE;
		
		BulkPaymentRecordDTO recordDTO = fetchBulkPaymentRecordDetailsById(recordId);
		Double recordAmount = recordDTO.getTotalAmount();
		Double totalTransactions = recordDTO.getTotalTransactions();
		recordAmount = recordAmount + amount;
		totalTransactions = totalTransactions + transactionCountChange;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("totalAmount", recordAmount);
		requestParameters.put("totalTransactions", totalTransactions.intValue());
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			if(response != null) {
				return true;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to update bulk payment record", e).log(); 
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating bulkpayment record: ", e).log();
			return false;
		}
		return false;
	}
	
}
