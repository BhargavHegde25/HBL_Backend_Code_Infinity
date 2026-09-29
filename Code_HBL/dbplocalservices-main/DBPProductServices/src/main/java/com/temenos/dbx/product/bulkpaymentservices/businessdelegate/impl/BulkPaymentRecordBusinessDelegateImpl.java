package com.temenos.dbx.product.bulkpaymentservices.businessdelegate.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.constants.EventSubType;
import com.temenos.dbx.constants.EventType;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentFileBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentFileBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public class BulkPaymentRecordBusinessDelegateImpl implements BulkPaymentRecordBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	BulkPaymentRecordBackendDelegate bulkPaymentRecordBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
	BulkPaymentFileBackendDelegate bulkPaymentFileBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentFileBackendDelegate.class);
	BulkPaymentFileBusinessDelegate bulkPaymentFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentFileBusinessDelegate.class);
	
	@Override
	public List<BulkPaymentRecordDTO> fetchBulkPaymentRecords(Set<String> recordIds) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_GET;

		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String filter = "recordId" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "recordId" + DBPUtilitiesConstants.EQUAL, recordIds);
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<BulkPaymentRecordDTO> bulkPaymentRecords = null;
		String payeeResponse = null;
		try {
			payeeResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			bulkPaymentRecords = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentRecordDTO.class);
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch bulk payment records by recordId: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchBulkPaymentRecord: ", e).log();
			return null;
		}

		return bulkPaymentRecords;
	}

	@Override
	public BulkPaymentRecordDTO createBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_CREATE;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentRecordDTO).toString(), String.class,
					Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}

		String createResponse = null;
		try {
			requestParameters.put("createdts",new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			createResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();

			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			bulkPaymentRecordDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentRecordDTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to create bulk payment record at bulkpaymentrecord table: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createBulkPaymentRecord: ", e).log();
			return null;
		}
		return bulkPaymentRecordDTO;
	}
	
	@Override
	public BulkPaymentRecordDTO editBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO) {

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentRecordDTO).toString(), String.class,
					Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}

	    return _updateBulkPaymentRecord(requestParameters);
	}
	
	@Override
	public boolean deleteBulkPaymentRecord(String bulkPaymentRecordId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_DELETE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", bulkPaymentRecordId);

		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.build().getResponse();
			
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		} catch (JSONException je) {
			alert.prepareError("Failed to delete the bulk payment record at bulkpaymentrecord table", je).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at deleteBulkPaymentRecord: ", e).log();
			return false;
		}

		return false;
	}

	@Override
	public BulkPaymentRecordDTO updateBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_UPDATE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", bulkPaymentRecordDTO.getRecordId());
		requestParams.put("paymentOrderId", bulkPaymentRecordDTO.getPaymentId());
		String editResponse = null;

		try {
			editResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.build().getResponse();

			JSONObject response = new JSONObject(editResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			bulkPaymentRecordDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentRecordDTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to edit bulk payment record at bulkpaymentrecord table: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updateBulkPaymentRecord: ", e).log();
			return null;
		}

		return bulkPaymentRecordDTO;
	}
	
	@Override
	public BulkPaymentRecordDTO updateRequestId(String recordId, String requestId) {
		
	    Map<String, Object> requestParams = new HashMap<>();
	    requestParams.put("recordId", recordId);
	    requestParams.put("requestId", requestId);
	    
	    return _updateBulkPaymentRecord(requestParams);
	}

	@Override
	public BulkPaymentRecordDTO fetchBulkPaymentRecordDetailsById(String recordId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_GET;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "recordId" + DBPUtilitiesConstants.EQUAL + recordId;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		BulkPaymentRecordDTO bulkPaymentRecord = null;
		String payeeResponse = null;
		try {
			payeeResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			List<BulkPaymentRecordDTO> response = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentRecordDTO.class);
			if(CollectionUtils.isNotEmpty(response)) {
				bulkPaymentRecord = response.get(0);
			}
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch bulk payment records by recordId: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchBulkPaymentRecord: ", e).log();
			return null;
		}

		return bulkPaymentRecord;
	}
	
	@Override
	public BulkPaymentRecordDTO fetchBulkPaymentRecordBybackendId(String confirmationNumber) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_GET;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + confirmationNumber;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		BulkPaymentRecordDTO bulkPaymentRecord = null;
		String payeeResponse = null;
		try {
			payeeResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			List<BulkPaymentRecordDTO> bulkPaymentRecords = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentRecordDTO.class);
			
			if(CollectionUtils.isNotEmpty(bulkPaymentRecords))
				return bulkPaymentRecords.get(0);
				
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch bulk payment records by recordId: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchBulkPaymentRecord: ", e).log();
			return null;
		}

		return bulkPaymentRecord;
	}

	@Override
	public BulkPaymentRecordDTO updateStatus(String recordId, String status, String paymentId, String rejectionreason) {

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", recordId);
		requestParams.put("status", status);
		requestParams.put("paymentId", paymentId);
		requestParams.put("rejectionreason", rejectionreason);
		return _updateBulkPaymentRecord(requestParams);
	}
	
	private static BulkPaymentRecordDTO _updateBulkPaymentRecord (Map<String, Object> requestParams) {
		
		BulkPaymentRecordDTO recordDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTRECORD_UPDATE;
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			recordDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), BulkPaymentRecordDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the bulkpayment",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the bulkpayment",exp).log();
			return null;
		}
		return recordDTO;
	}
	
	@Override
	public Object approveBulkPaymentRecord(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		BBRequestDTO bBRequestDTO = null;

		int counter = approvalQueueBusinessDelegate.callFetchRequestProc(requestId, customerId, comments, companyId);
		if (counter == -1) {
			return null;
		} else {
			
			bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestCounter(requestId, counter, dcr);
			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {
				BulkPaymentRecordDTO bulkPaymentRecordDTO = updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), null, null);
				if (bulkPaymentRecordDTO == null) {
					return null;
				} else {
					bulkPaymentRecordDTO.setFeatureActionId(bBRequestDTO.getFeatureActionId());
					return bulkPaymentRecordDTO;
				}

			} else {
				return bBRequestDTO;
			}
		}
	}
	
	@Override
	public void executeRecordAfterApproval(String recordId, DataControllerRequest dcrequest, DataControllerResponse dcresponse, Result result) {
		
		TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		BulkPaymentRecordBackendDelegate billpayBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
		BulkPaymentRecordDTO recordDTO = fetchBulkPaymentRecordDetailsById(recordId);
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcrequest);			
		String customerId = CustomerSession.getCustomerId(customer);
		
		if(recordDTO == null) {
			alert.prepareError("Failed to fetch the transcation entry from table: ").log();
			updateStatus(recordId, TransactionStatusEnum.FAILED.getStatus(), null, null);
			return;
		}
		String confirmationNumber = recordDTO.getConfirmationNumber();
		
		String companyId = recordDTO.getCompanyId();
		String requestId = recordDTO.getRequestId();
		
		if(! recordDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {
			alert.prepareError("Record is not Approved ").log();
			return;
		}
		
		if(recordDTO.getPaymentDate() == null) {
			alert.prepareError("Record does not have an payment date ").log();
			updateStatus(recordId, TransactionStatusEnum.FAILED.getStatus(), null, null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcrequest);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Record does not have an payment date to execute", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		TransactionStatusDTO transactionStatusDTO = limitsDelegate.validateForLimits(recordDTO.getCreatedby(), 
				companyId, recordDTO.getFromAccount(), recordDTO.getFeatureActionId(), recordDTO.getTotalAmount(),
				TransactionStatusEnum.APPROVED, recordDTO.getPaymentDate(), null, null, dcrequest);
		
		if(transactionStatusDTO == null 
				|| transactionStatusDTO.getDbpErrCode() != null 
				|| transactionStatusDTO.getDbpErrMsg() != null) {
			alert.prepareError("Failed to validate limits ").log();
			updateStatus(recordId, TransactionStatusEnum.FAILED.getStatus(), null, null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcrequest);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Failed to validate limits Before executing at core", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		recordDTO.setStatus(transactionStatus.getStatus());
		
		switch (transactionStatus) {
		
		case SENT:
			String referenceId = null;
			BulkPaymentRecordDTO initiateResponse = null;
			updateStatus(recordId, transactionStatus.getStatus(), confirmationNumber, null);
			
			if(confirmationNumber != null && !(confirmationNumber.equals("")) && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
				BulkPaymentRecordDTO initiateRequest = recordDTO;
				initiateRequest.setRecordId(recordDTO.getConfirmationNumber());
				initiateResponse = billpayBackendDelegate.initiateBulkPayment(initiateRequest, dcrequest);
			} 
			else {
				return;
			}
			
			String reason = null;
			if(initiateResponse == null) {
				reason = "Failed at backend";
			}
			else if(initiateResponse.getDbpErrMsg() != null && !initiateResponse.getDbpErrMsg().isEmpty()) {
				reason = initiateResponse.getDbpErrMsg();
			}
			else {
				referenceId = initiateResponse.getPaymentId();
			}
			
			if(referenceId == null || referenceId.isEmpty()) {
				alert.prepareError("create or edit transaction failed ").log();
				updateStatus(recordId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber == null ? reason : confirmationNumber,"");
				if(requestId != null) {
					approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcrequest);
					approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Create transaction failed at backend", customerId,
							TransactionStatusEnum.FAILED.getStatus());
				}
				_logTransaction(dcrequest,dcresponse,EventType.BULK_PAYMENT_REQUEST,EventSubType.BULK_PAYMENT_REQUEST_INITIATE, recordDTO, result, Constants.SID_EVENT_FAILURE);
			}
			else {

				updateStatus(recordId, TransactionStatusEnum.EXECUTED.getStatus(), referenceId,"");
				_logTransaction(dcrequest,dcresponse,EventType.BULK_PAYMENT_REQUEST,EventSubType.BULK_PAYMENT_REQUEST_INITIATE, recordDTO, result, Constants.SID_EVENT_SUCCESS);

			}
			
			break;
			
		case PENDING:
			//This case should never encounter for approved transactions
			break;
			
		case DENIED_AD_MAX_TRANSACTION:
			
		case DENIED_AD_DAILY:
			
		case DENIED_AD_WEEKLY:
			
		case DENIED_MAX_TRANSACTION:
			
		case DENIED_MIN_TRANSACTION:
			
		case DENIED_DAILY:
			
		case DENIED_WEEKLY:
			
		case DENIED_INVALID_APPROVAL_MATRIX:
			updateStatus(recordId, TransactionStatusEnum.FAILED.getStatus(), null, null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcrequest);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), transactionStatus.getMessage(), customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
			_logTransaction(dcrequest,dcresponse,EventType.BULK_PAYMENT_REQUEST,EventSubType.BULK_PAYMENT_REQUEST_INITIATE, recordDTO, result, Constants.SID_EVENT_FAILURE);
			break;
			
		default:
			break;
			
		}
	}
	
	/**
	 * Logs bulkpayment request's status in auditactivity
	 * @param request
	 * @param response
	 * @param result
	 * @param transactionStatus
	 * @param bulkPaymentRecordDTO
	 * @param referenceId
	 * @param requestId
	 */
	private void _logTransaction(DataControllerRequest request, DataControllerResponse response, String eventType,
			String eventSubType, BulkPaymentRecordDTO bulkPaymentRecordDTO, Result result, String statuId) {
		try {
			JsonObject customParams = new JsonObject();
			AuditLog auditLog = new AuditLog();
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userName = CustomerSession.getCustomerName(customer);
			customParams = auditLog.buildCustomParamsForAlertEngine(bulkPaymentRecordDTO.getFromAccount(), "", customParams);
			customParams.addProperty("fromAccountNumber", bulkPaymentRecordDTO.getFromAccount());
			customParams.addProperty("createdBy", userName);
			customParams.addProperty("executiondate", bulkPaymentRecordDTO.getPaymentDate());
			customParams.addProperty(Constants.AMOUNT, bulkPaymentRecordDTO.getTotalAmount());
			
			List<Param> params = result.getAllParams();
			for (Param param : params) {
				if (request.containsKeyInRequest(param.getName())) {
					continue;
				} else {
					customParams.addProperty(param.getName(), param.getValue());
				}
			}
			String producer = "BulkPaymentObjects/approveBulkPaymentRecord";
			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statuId, null, null, customParams);
		}
		catch (Exception e) {
			alert.prepareError("Error while triggering an event for Event Manager").log();
		}
	}

	@Override
	public List<BulkPaymentRecordDTO> fetchOnGoingPaymentsfromBackend(Set<String> fromAccounts, String batchMode, String fromDate, String toDate, DataControllerRequest request) {
		List<BulkPaymentRecordDTO> ongoingpayments;
		List<BulkPaymentRecordDTO> records = bulkPaymentRecordBackendDelegate.fetchOnGoingPayments(fromAccounts, batchMode, fromDate, toDate, request);
		if(records == null || records.size() == 0) {
			return records;
		}
		
		if(records.size() > 0 && StringUtils.isNotBlank(records.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching ongoing bulk payments from backend").log();
			return null;
		}

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
		contracts.add(CustomerSession.getCompanyId(customer));
				
		List<BulkPaymentFileDTO> files = bulkPaymentFileBusinessDelegate.fetchBulkPaymentFiles(customerId, contracts);
		if(files == null) {
			return null;
		}
		
		ongoingpayments = (new FilterDTO()).merge(records, files, "fileId=confirmationNumber", "");
		
		if(ongoingpayments.size() == 0){
			alert.prepareError("No Records Found").log();
		}
		
		return ongoingpayments;
		
	}

	@Override
	public List<BulkPaymentRecordDTO> fetchBulkPaymentHistoryfromBackend(Set<String> fromAccounts, String fromDate, String toDate, DataControllerRequest request) {
		List<BulkPaymentRecordDTO> historyRecords;
		List<BulkPaymentRecordDTO> records = bulkPaymentRecordBackendDelegate.fetchBulkPaymentHistory(fromAccounts, fromDate, toDate, request); 

		if(records == null || records.size() == 0) {
			return records;
		}
		
		if(records.size() > 0 && StringUtils.isNotBlank(records.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching ongoing bulk payments from backend").log();
			return null;
		}
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
		contracts.add(CustomerSession.getCompanyId(customer));
				
		List<BulkPaymentFileDTO> files = bulkPaymentFileBusinessDelegate.fetchBulkPaymentFiles(customerId, contracts);
		if(files == null) {
			return null;
		}
		historyRecords = (new FilterDTO()).merge(records, files, "fileId=confirmationNumber", "");
		 
		if(historyRecords.size() == 0){
			alert.prepareError("No Records Found").log();
		}
		return historyRecords;
	}

	@Override
	public List<BulkPaymentRecordDTO> fetchRecordsFromBackend(Set<String> backendrecordIdList, DataControllerRequest request) {
		List<BulkPaymentRecordDTO> fetchedRecords;
		List<BulkPaymentRecordDTO> records = bulkPaymentRecordBackendDelegate.fetchRecords(backendrecordIdList, request);

		if(records == null || records.size() == 0) {
			return records;
		}
		
		if(records.size() > 0 && StringUtils.isNotBlank(records.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching bulk payment records from backend").log();
			return null;
		}
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
		contracts.add(CustomerSession.getCompanyId(customer));
				
		List<BulkPaymentFileDTO> files = bulkPaymentFileBusinessDelegate.fetchBulkPaymentFiles(customerId, contracts);
		if(files == null) {
			return null;
		}
		fetchedRecords = (new FilterDTO()).merge(records, files, "fileId=confirmationNumber", "featureActionId");
		 
		if(fetchedRecords.size() == 0){
			alert.prepareError("No Records Found").log();
		}
		return fetchedRecords;
	}
	
	public BulkPaymentRecordDTO fetchBulkPaymentRecordByRequestId(String requestId, DataControllerRequest dcr) {
		
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		List<BBRequestDTO> requestDTOs = approvalQueueBusinessDelegate.fetchRequests("","",requestId,"");
		
		Set<String> recordIds = new HashSet<String>();
		List<BulkPaymentRecordDTO> records = null;
		
		if(requestDTOs == null)
			return null;
		for(BBRequestDTO bBRequestDTO : requestDTOs)
			recordIds.add(bBRequestDTO.getTransactionId());
		if(recordIds.size() == 0) 
			return null;
		records = fetchBulkPaymentRecords(recordIds);
		
		if(records == null) 
			return null;
		recordIds = new HashSet<String>();
		for(BulkPaymentRecordDTO dto : records)
			recordIds.add(dto.getConfirmationNumber());
		if(recordIds.size() == 0) 
			return null;
		records = fetchRecordsFromBackend(recordIds, dcr);
		
		if(CollectionUtils.isEmpty(records))
			return null;
		return records.get(0);
	}

	/**
	 * ADP-7058 - consolidated method for fetching all the records with approval info (invoked by ApprovalQueueBusinessDelegate.fetchAdditionalBackendData)
	 * @param bbRequests
	 * @param dcr
	 * @return List<BulkPaymentRecordDTO>
	 * @author sourav.raychaudhuri
	 */
	@Override
	public List<BulkPaymentRecordDTO> fetchAllRecordsFromBackendWithApprovalInfo(List<BBRequestDTO> bbRequests, DataControllerRequest dcr){
		Set<String> bulkPaymentRecordsIdList = new HashSet<>();
		List<BulkPaymentRecordDTO> bulkPaymentRecords = new ArrayList<>();
		List<BulkPaymentRecordDTO> bulkPaymentBackendRecords = new ArrayList<>();

		try{
			for(BBRequestDTO bbRequest : bbRequests){
				bulkPaymentRecordsIdList.add(bbRequest.getTransactionId());
			}

			// get bulk payment request data
			if(bulkPaymentRecordsIdList.size()!=0){
				bulkPaymentRecords = this.fetchBulkPaymentRecords(bulkPaymentRecordsIdList);
			}
			if(bulkPaymentRecords == null || bulkPaymentRecords.size() == 0){
				return bulkPaymentBackendRecords;
			}
			List<BulkPaymentRecordDTO> bulkPaymentRecordsWithApprovalInfo = (new FilterDTO()).merge(bulkPaymentRecords, bbRequests, "recordId=transactionId", "requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready");

			// get bulk payment request backend data
			bulkPaymentRecordsIdList.clear();
			for(BulkPaymentRecordDTO recordDTO: bulkPaymentRecordsWithApprovalInfo){
				bulkPaymentRecordsIdList.add(recordDTO.getConfirmationNumber());
			}

			if(bulkPaymentRecordsIdList.size()!=0) {
				bulkPaymentBackendRecords = this.fetchRecordsFromBackend(bulkPaymentRecordsIdList, dcr);
			}
			if(bulkPaymentBackendRecords == null || bulkPaymentBackendRecords.size( )== 0){
				return bulkPaymentBackendRecords;
			}
			bulkPaymentBackendRecords = (new FilterDTO()).merge(bulkPaymentBackendRecords, bulkPaymentRecords, "recordId=confirmationNumber", "requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");

			return bulkPaymentBackendRecords;
		} catch(Exception e){
			alert.prepareError(e + "").log();
			return bulkPaymentBackendRecords;
		}
	}
}
