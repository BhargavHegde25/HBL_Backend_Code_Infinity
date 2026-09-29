package com.temenos.dbx.product.achservices.businessdelegate.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHFileBackendDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileRecordBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileFormatTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileRecordDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileSubrecordDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public class ACHFileBusinessDelegateImpl implements ACHFileBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	ACHFileBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ACHFileBackendDelegate.class);
	
	private final String BATCH_HEADER_REACOR = "Batch Header Record";
	private final String PDP_ENTRY_DETAIL_RECORD = "PDP Entry Detail Record";
	private final String BATCH_CONTROL_RECORD = "Batch Control Record";
	private final String FILE_CONTROL_RECORD = "File Control Record";
	private final String FILE_HEADER_RECORD = "File Header Record";
	private final String OFFSET ="OFFSET";

	private final String TRANSACTION_TYPE = "Transaction Type";
	private final String REQUEST_TYPE = "Request Type";
	private final String EFFECTIVE_DATE = "Effective Date";
	private final String AMOUNT = "Amount";
	private final String ACCOUNT_TYPE = "Account Type";
	private final String ACCOUNT_NUMBER = "Account Number";
	private final String RECEIVER_NAME = "Receiver Name";

	private final String CREDIT = "Credit";
	private final String DEBIT = "Debit";

	private final String CREDIT_CODE = "220";
	private final String DEBIT_CODE = "225";

	private final String COLLECTION_CHECKINGS = "Collection,Checkings";
	private final String COLLECTION_SAVINGS = "Collection,Savings";
	private final String PAYMENT_CHECKINGS = "Payment,Checkings";
	private final String PAYMENT_SAVINGS = "Payment,Savings";

	private final String COLLECTION_CHECKINGS_CODE = "22";
	private final String COLLECTION_SAVINGS_CODE = "32";
	private final String PAYMENT_CHECKINGS_CODE = "27";
	private final String PAYMENT_SAVINGS_CODE = "37";

	/* NACHA Standard Record identifiers */
	private final Character FILE_RECORD_HEADER_CODE = '1';
	private final Character BATCH_HEADER_RECORD_CODE = '5';
	private final Character PDP_ENTITY_DETAIL_RECORD_CODE = '6';
	private final Character BATCH_CONTROL_RECORD_CODE = '8';
	private final Character FILE_CONTROL_RECORD_CODE = '9';

	private final HashMap<String, String> service_code_map = new HashMap<String, String>() {
		private static final long serialVersionUID = -5024873910875898303L;
		{
			put(CREDIT_CODE, CREDIT);
			put(DEBIT_CODE, DEBIT);
		}
	};

	private final HashMap<Character, String> recordTypeMap = new HashMap<Character, String>() {
		private static final long serialVersionUID = 6134905877243097924L;
		{
			put(FILE_RECORD_HEADER_CODE, FILE_HEADER_RECORD);
			put(BATCH_HEADER_RECORD_CODE, BATCH_HEADER_REACOR);
			put(PDP_ENTITY_DETAIL_RECORD_CODE, PDP_ENTRY_DETAIL_RECORD);
			put(BATCH_CONTROL_RECORD_CODE, BATCH_CONTROL_RECORD);
			put(FILE_CONTROL_RECORD_CODE, FILE_CONTROL_RECORD);
		}
	};

	private final HashMap<String, String> transaction_code_map = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(COLLECTION_CHECKINGS_CODE, COLLECTION_CHECKINGS);
			put(COLLECTION_SAVINGS_CODE, COLLECTION_SAVINGS);
			put(PAYMENT_CHECKINGS_CODE, PAYMENT_CHECKINGS);
			put(PAYMENT_SAVINGS_CODE, PAYMENT_SAVINGS);
		}
	};

	private final List<ACHFileParsingRule> batchRecordRule = new ArrayList<ACHFileParsingRule>() {
		private static final long serialVersionUID = 2099500089841534413L;
		{
			add(new ACHFileParsingRule(2, 4, TRANSACTION_TYPE));
			add(new ACHFileParsingRule(51, 53, REQUEST_TYPE));
			add(new ACHFileParsingRule(70, 75, EFFECTIVE_DATE));
		}

	};

	private final List<ACHFileParsingRule> pdpEntityRule = new ArrayList<ACHFileParsingRule>() {
		private static final long serialVersionUID = 8066106976006257121L;

		{
			add(new ACHFileParsingRule(30, 39, AMOUNT));
			add(new ACHFileParsingRule(2, 3, ACCOUNT_TYPE));
			add(new ACHFileParsingRule(13, 29, ACCOUNT_NUMBER));
			add(new ACHFileParsingRule(55, 76, RECEIVER_NAME));
		}

	};

	@Override
	public ACHFileDTO uploadACHFileAtDBX(ACHFileDTO achfileDTO) {
		
		ACHFileDTO achFileDTO;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_CREATE;

		Map<String, Object> requestParameters;
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(achfileDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}
		
		String uploadResponse = null;
		try {
	        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
	        sdf.setTimeZone(TimeZone.getDefault());
			requestParameters.put("createdts", sdf.format(new Date()));
			uploadResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
		} catch (JSONException e) {
			alert.prepareError("Unable to Create ACH File: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at CreateACHFileAtDBX method: " , e).log();
			return null;
		}

		try {
			JSONObject fileformatJSON = new JSONObject(uploadResponse);
			JSONArray records = CommonUtils.getFirstOccuringArray(fileformatJSON);
			
			if(records != null) {
				achFileDTO = JSONUtils.parse(records.getJSONObject(0).toString(), ACHFileDTO.class);
				
				if(achFileDTO != null) {
					achfileDTO.setAchFile_id(achFileDTO.getAchFile_id());
					List<ACHFileRecordDTO> fileRecords = _getACHFileRecordsRequestDTO(achfileDTO);
					
			        if(fileRecords == null || !createFileRecordAndSubRecords(fileRecords)) {
			        	deleteACHFileAtDBX(achfileDTO.getAchFile_id());
			 			return null;
			        }
				}
			}
			else {
				return null;
			}
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " , e).log();
			return null;
		}
		
		return achFileDTO;
	}

	private List<ACHFileRecordDTO> _getACHFileRecordsRequestDTO(ACHFileDTO achfileDTO) {
		List<ACHFileRecordDTO> fileRecordsDTO = achfileDTO.getFileRecords();
		String achFileId = achfileDTO.getAchFile_id();
        
        if((achFileId != null && !achFileId.isEmpty()) || fileRecordsDTO != null){
        	for(int i=0; i < fileRecordsDTO.size(); i++) {
    			fileRecordsDTO.get(i).setAchFileId(achFileId);
    		}
        	return fileRecordsDTO;
        }
        
		return null;
	}

	@Override
	public List<ACHFileFormatTypeDTO> getACHFileFormats() {
		
		List<ACHFileFormatTypeDTO> fileFormatsDTOs;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.ACH_FILE_FORMAT_TYPES_GET;

		HashMap<String, Object> requestParameters = new HashMap<>();

		String fileFormats = null;
		try {
			fileFormats = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
		} catch (JSONException e) {
			alert.prepareError("Unable to fetch file formats: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getACHFileFormats method: " , e).log();
			return null;
		}

		try {
			JSONObject fileformatJSON = new JSONObject(fileFormats);
			JSONArray records = CommonUtils.getFirstOccuringArray(fileformatJSON);
			if(records != null)
				fileFormatsDTOs = JSONUtils.parseAsList(records.toString(), ACHFileFormatTypeDTO.class);
			else
				return null;
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " , e).log();
			return null;
		}
		
		return fileFormatsDTOs;
	}
	
	@Override
	public boolean updateRequestId(String achFileId, String requestId) {

		List<ACHFileDTO> achFileDTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("achFile_id", achFileId);
		requestParams.put("requestId", requestId);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(updateResponse);
			JSONArray achfileJsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			achFileDTOs = JSONUtils.parseAsList(achfileJsonArray.toString(), ACHFileDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured while updating the achfile table",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while updating the achfile table",exp).log();
			return false;
		}
		
		if(achFileDTOs != null && achFileDTOs.size() != 0)
			return true;
		
		return false;
	}
	
	@Override
	public ACHFileDTO updateStatus(String achFileId, String status, String confirmationNumber) {

		if(application.getIsStateManagementAvailableFromCache()) {
			return updateStatusUsingConfirmationNumber(achFileId, status);
		}
		else {
			return updateStatusUsingTransactionId(achFileId, status, confirmationNumber);
		}
	}
	
	@Override
	public ACHFileDTO updateStatusUsingTransactionId(String achFileId, String status, String confirmationNumber) {

		List<ACHFileDTO> achFileDTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("achFile_id", achFileId);
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
			JSONArray achfileJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			achFileDTOs = JSONUtils.parseAsList(achfileJsonArray.toString(), ACHFileDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the billpaytransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the billpaytransaction",exp).log();
			return null;
		}
		
		if(achFileDTOs != null && achFileDTOs.size() != 0)
			return achFileDTOs.get(0);
		
		return null;
	}
	
	private ACHFileDTO updateStatusUsingConfirmationNumber(String confirmationNumber, String status) {
		ACHFileDTO achFileDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_UPDATE_TRANSACTION_STATUS_PROC;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_featureId", Constants.FEATURE_ACH_FILES);
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
					achFileDTO = new ACHFileDTO();
					achFileDTO.setConfirmationNumber(confirmationNumber);
					achFileDTO.setStatus(status);
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
		
		return achFileDTO;
	}
	
	@Override
	public List<ACHFileDTO> fetchACHFiles(String customerId, String achFileId, FilterDTO filters, DataControllerRequest dcr) {
		
		List<ACHFileDTO> resultDTO = null;
		List<ACHFileDTO> statusDTOs = new ArrayList<ACHFileDTO>();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_ACHFILES_PROC;
		
		Map<String, Object> requestMap;
		String fetchResponse = null;
		List<String> fileIds = new ArrayList<String>();
		Map<String, String> statusMap = new HashMap<String, String>();
		
		ACHFileBackendDelegate achFileBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				   .getFactoryInstance(BackendDelegateFactory.class)
				   .getBackendDelegate(ACHFileBackendDelegate.class);
		
		try {
			requestMap = JSONUtils.parseAsMap(JSONUtils.stringify(filters), String.class, Object.class);
			requestMap.put(ACHConstants._ACH_FILE_ID, achFileId);
			requestMap.put(Constants._CUSTOMER_ID, customerId);
			
			fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestMap).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray records = CommonUtils.getFirstOccuringArray(responseObj);
			resultDTO = JSONUtils.parseAsList(records.toString(), ACHFileDTO.class);
			boolean stateManagementAvailable = application.getIsStateManagementAvailableFromCache();
			for(int i=0; i < resultDTO.size(); i++) {
				ACHFileDTO transcation = resultDTO.get(i);
				String confirmationNumber = transcation.getConfirmationNumber();
				
				if (stateManagementAvailable && StringUtils.isNotEmpty(confirmationNumber)) {
					transcation.setAchFile_id(confirmationNumber);
				}
				
				if(confirmationNumber != null && !confirmationNumber.isEmpty() && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
			        	fileIds.add(confirmationNumber);
				}
			}
			
			if(fileIds.size() > 0) {
				statusDTOs = achFileBackendDelegate.fetchFileStatus(fileIds, dcr);
			}
			
			for(int i=0; i< statusDTOs.size(); i++) {
				ACHFileDTO statusDTO = statusDTOs.get(i);
				statusMap.put(statusDTO.getConfirmationNumber(), statusDTO.getStatus());
			}
			
			for(int i=0; i < resultDTO.size() && statusMap.size() > 0; i++) {
				ACHFileDTO transaction = resultDTO.get(i);
				String status = transaction.getStatus();
				String confirmationNumber = transaction.getConfirmationNumber();
				
				if(confirmationNumber != null && !confirmationNumber.isEmpty()) {
					
					status = (statusMap.get(confirmationNumber) == null || statusMap.get(confirmationNumber).isEmpty())
							? status : statusMap.get(confirmationNumber);
					transaction.setStatus(status);
					resultDTO.set(i, transaction);
					statusMap.remove(confirmationNumber);
				}
			}
		}
		catch(Exception exp) {
			alert.prepareError("Error Occurred while fetching files",exp).log();
			return null;
		}
		return resultDTO;
	}
	
	@Override
	public ACHFileDTO fetchACHFileEntry(String achFileId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_GET;
		
		List<ACHFileDTO> achFiles = null;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		String filter = "achFile_id" + DBPUtilitiesConstants.EQUAL + achFileId;
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			achFiles = JSONUtils.parseAsList(jsonArray.toString(), ACHFileDTO.class);
			
			if(achFiles != null && achFiles.size() != 0)
				return achFiles.get(0);
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured in delegate while fetching ach file entry", exp).log();
			return null;
		}
		return null;
	}
	
	@Override
	public void executeACHFileAfterApproval(String achFileId, String featureActionId, DataControllerRequest dcr) {
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcr);			
		String customerId = CustomerSession.getCustomerId(customer);
		
		ApprovalQueueBusinessDelegate approvalDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		
		ACHFileDTO achfileDTO = fetchTransactionById(achFileId, dcr);
		if(achfileDTO == null) {
			alert.prepareError("Failed to fetch the wire transaction entry from table: ").log();
			updateStatus(achFileId, TransactionStatusEnum.FAILED.getStatus(), null);
			return;
		}
		/*if(! achfileDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {
			alert.prepareError("Transaction is not Approved ").log();
			return;
		}*/
		String confirmationNumber = achfileDTO.getConfirmationNumber();
		String companyId = achfileDTO.getCompanyId();
		String requestId = achfileDTO.getRequestId();
		
		/*if(! achfileDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {
			alert.prepareError("File is not Approved ").log();
			return;
		}*/
		
		ACHFileRecordBusinessDelegate achFileRecordBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileRecordBusinessDelegate.class);
		List<ACHFileRecordDTO> achFileRecordListDTO = achFileRecordBusinessDelegate.fetchACHFileRecords(achFileId);
		if(achFileRecordListDTO == null) {
			alert.prepareError("Failed to fetch the file records from table: ").log();
			updateStatus(achFileId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.logActedRequest(requestId, achfileDTO.getCompanyId(), TransactionStatusEnum.FAILED.getStatus(), "Failed to fetch the file records from table before executing", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		achfileDTO.setFileRecords(achFileRecordListDTO);
		
		//Summarizing Offset Details
		Map<String, Double> offsetDetails = new HashMap<String, Double>();
        for(int i = 0; i < achFileRecordListDTO.size() ; i++){    
            ACHFileRecordDTO fileRecord = achFileRecordListDTO.get(i);
            String accountNumber = fileRecord.getOffsetAccountNumber();
            String effectiveDate = fileRecord.getEffectiveDate();
            double debitAmount = ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT.equals(fileRecord.getOffsetTransactionType()) ? fileRecord.getOffsetAmount() : 0.0;
            String key = accountNumber + "_" + effectiveDate;
            if(offsetDetails.containsKey(key)){
                debitAmount = offsetDetails.get(key) + debitAmount;
            }
            offsetDetails.put(key , debitAmount);
        }
        
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(achfileDTO.getCreatedby());
        transactionStatusDTO.setCompanyId(companyId);
        transactionStatusDTO.setStatus(TransactionStatusEnum.APPROVED);
        transactionStatusDTO.setFeatureActionID(featureActionId);
        transactionStatusDTO.setOffsetDetails(offsetDetails);
        transactionStatusDTO.setConfirmationNumber(confirmationNumber);
        
        transactionStatusDTO = approvalDelegate.validateForApprovals(transactionStatusDTO, dcr); 
		if(transactionStatusDTO == null) {			
			alert.prepareError("Failed to validate limits ").log();
			updateStatus(achFileId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Failed to validate limits Before executing at core", customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			alert.prepareError("Error occured while validating limits").log();
			updateStatus(achFileId, TransactionStatusEnum.FAILED.getStatus(), null);
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
			updateStatus(achFileId, TransactionStatusEnum.FAILED.getStatus(), null);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Not a valid status: " + transactionStatusDTO.getStatus(), customerId,
					TransactionStatusEnum.FAILED.getStatus());
			}
			return;
		}
		
		achfileDTO = approveTransaction(achFileId, dcr);
		
		String referenceId = null;
		
		String reason = null;
		if(achfileDTO == null) {
			reason = "Failed at backend";
		}
		else if(achfileDTO.getDbpErrMsg() != null && !achfileDTO.getDbpErrMsg().isEmpty()) {
			reason = achfileDTO.getDbpErrMsg();
		}
		else {
			referenceId = achfileDTO.getReferenceID();
		}
		
		if(referenceId == null || referenceId.isEmpty()) {
			alert.prepareError("create or edit transaction failed ").log();
			updateStatus(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber == null ? reason : confirmationNumber);
			if(requestId != null) {
				approvalDelegate.updateBBRequestStatus(requestId, TransactionStatusEnum.FAILED.getStatus(), dcr);
				approvalDelegate.logActedRequest(requestId, companyId, TransactionStatusEnum.FAILED.getStatus(), "Create transaction failed at backend", customerId,
						TransactionStatusEnum.FAILED.getStatus());
			}
		}
		else {
			updateStatus(achFileId, TransactionStatusEnum.EXECUTED.getStatus(), referenceId);
		}
		// ADP-7058 update additional meta data
		try{
			approvalDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), dcr);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
	}
	
	@Override
	public boolean createFileRecordAndSubRecords(List<ACHFileRecordDTO> records) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_FILE_RECORD_SUBRECORD_CREATE;
		String createResponse = null;
		Map<String, Object> requestParams = _getMapForFileRecordsAndSubRecords(records);
		
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt("opstatus") == 0 && responseObj.getInt("httpStatusCode") == 0) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while creating an ACH template",exp).log();
			return false;
		}
		
		return false;
	}

	/*
	 *@author KH2638
	 *@version 1.0
	 *@param listof record to create input string map for create procedure
	***/
	private Map<String, Object> _getMapForFileRecordsAndSubRecords(List<ACHFileRecordDTO> records) {

		Map<String,Object> requestParam = new HashMap<String, Object>();

		requestParam.put("_recordvalues", _getRecordsValue(records));
		requestParam.put("_subRecordvalues", _getSubRecordsValue(records));
		
		return requestParam;
	}

	/*
	 *@author KH2638
	 *@version 1.0
	 *@param list of file records to create input string for create procedure
	***/
	private String _getRecordsValue(List<ACHFileRecordDTO> records) {
		
		StringBuilder recordValues = new StringBuilder("");
		int recordSize = records.size();
		
		for(int i=0; i<recordSize; i++) {
			ACHFileRecordDTO record = records.get(i);		
			
			String offsetAccountNumber = record.getOffsetAccountNumber();
			offsetAccountNumber = (offsetAccountNumber == null) ? offsetAccountNumber : "\""+offsetAccountNumber+"\"";
			
			String offsetTransactionType = record.getOffsetTransactionType();
			offsetTransactionType = (offsetTransactionType == null) ? offsetTransactionType : "\""+offsetTransactionType+"\"";
			
			String transactionType = record.getTransactionType();
			transactionType = (transactionType == null) ? transactionType : "\""+transactionType+"\"";
			
			String effectiveDate = record.getEffectiveDate();
			effectiveDate = (effectiveDate == null) ? effectiveDate : "\""+effectiveDate+"\"";

			String requestType = record.getRequestType();
			requestType = (requestType == null) ? requestType : "\""+requestType+"\"";
			
			recordValues.append(record.getAchFileId()+",");
			recordValues.append(offsetAccountNumber+",");
			recordValues.append(record.getOffsetAmount()+",");
			recordValues.append(offsetTransactionType+",");
			recordValues.append(effectiveDate+",");
			recordValues.append(requestType+",");
			recordValues.append(record.getTotalCreditAmount()+",");
			recordValues.append(record.getTotalDebitAmount()+",");
			recordValues.append(transactionType);
			
			if( i < recordSize-1) {
				recordValues.append("|");
			}
		}
		return recordValues.toString();
	}

	/*
	 *@author KH2638
	 *@version 1.0
	 *@param list of file sub records to create input string for create procedure
	***/
	private String _getSubRecordsValue(List<ACHFileRecordDTO> records) {

		StringBuilder subRecordValues = new StringBuilder("");
		int recordSize = records.size();
		
		for(int i=0; i<recordSize; i++) {
			List<ACHFileSubrecordDTO> subRecords = records.get(i).getSubRecords();
			
			if(subRecords == null) {
				subRecordValues.append(";");
			}
			else {
				int subRecordSize = subRecords.size();
				
				for(int j=0; j<subRecordSize; j++) {
					
					ACHFileSubrecordDTO subRecord = subRecords.get(j);
					
					String receiverAccountNumber = subRecord.getReceiverAccountNumber();
					receiverAccountNumber = (receiverAccountNumber == null) ? receiverAccountNumber : "\""+receiverAccountNumber+"\"";
					
					String receiverAccountType = subRecord.getReceiverAccountType();
					receiverAccountType = (receiverAccountType == null) ? receiverAccountType : "\""+receiverAccountType+"\"";
					
					String receiverTransactionType = subRecord.getReceiverTransactionType();
					receiverTransactionType = (receiverTransactionType == null) ? receiverTransactionType : "\""+receiverTransactionType+"\"";

					String receiverName = subRecord.getReceiverName();
					receiverName = (receiverName == null) ? receiverName : "\""+receiverName+"\"";
			
					subRecordValues.append(subRecord.getAmount()+",");
					subRecordValues.append(receiverAccountNumber+",");
					subRecordValues.append(receiverAccountType+",");
					subRecordValues.append(receiverName+",");
					subRecordValues.append(receiverTransactionType);
					
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
	public boolean deleteACHFileAtDBX(String achFileId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_DELETE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("achFile_id", achFileId);
		
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
			alert.prepareError("JSONExcpetion occured while deleting the ach file",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while deleting the ach file",exp).log();
			return false;
		}
		
		return false;	
	}
	
	@Override
	public ACHFileDTO validateACHFile(File achFile) {

		ACHFileDTO achFileDTO = new ACHFileDTO();

		if (!_isFileNameValid(achFile) && !achFile.exists() && !achFile.isFile() && !(achFile.length() > 0) && !(achFile.length() < 1000000)) {
			achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12308);
			return achFileDTO;
		}

		try (BufferedReader achFileBufferReader = new BufferedReader(new FileReader(achFile));) {

			String achRecord = achFileBufferReader.readLine();

			if (achRecord.charAt(0) == FILE_RECORD_HEADER_CODE) { // proceed further only if there is a header
				List<ACHFileRecordDTO> fileRecords = new ArrayList<>();
				achRecord = achFileBufferReader.readLine();
				int noOfCredits = 0;
				int noOfDebits = 0;				
				
				while (achRecord != null & achRecord.charAt(0) != FILE_CONTROL_RECORD_CODE) { // Read the next line until end of file and end of file record is reached

					if (achRecord.charAt(0) == BATCH_HEADER_RECORD_CODE) {
						String rule = recordTypeMap.get(achRecord.charAt(0));
						ACHFileRecordDTO achFileRecord = _fetchFileRecordDTO(_getRuleSet(rule), achRecord);

						if(achFileRecord.getEffectiveDate() == null) {
							alert.prepareError("Effective Date provided in ACH File is not valid.").log();
							achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12309);
							return achFileDTO;
						}
						
						achRecord = achFileBufferReader.readLine(); // Reading the next line for batch entry (subrecords) details
						
						if(achFileRecord.getRequestType() == null) {
							alert.prepareError("Request type provided in ACH File is not valid.").log();
							achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12310);
							return achFileDTO;
						} 						
						else if(achFileDTO.getRequestType() == null) {
							achFileDTO.setRequestType(achFileRecord.getRequestType());
						}
						else {
							achFileDTO.setRequestType(String.join(",", achFileDTO.getRequestType(), achFileRecord.getRequestType()));
						}
							
						List<ACHFileSubrecordDTO> achFileSubRecords = new ArrayList<>();
						
						while (achRecord.charAt(0) != BATCH_CONTROL_RECORD_CODE) { // until we have reached batch control record 

							rule = recordTypeMap.get(achRecord.charAt(0));
							ACHFileSubrecordDTO achFileSubRecord = _fetchFileSubrecordDTO(_getRuleSet(rule), achRecord);

							//To check whether it is offset record or not
							if(OFFSET.equalsIgnoreCase(achFileSubRecord.getReceiverName())) {
								achFileRecord.setOffsetAccountNumber(achFileSubRecord.getReceiverAccountNumber());
								achFileRecord.setOffsetAmount(achFileSubRecord.getAmount());
								achFileRecord.setOffsetTransactionType(achFileSubRecord.getReceiverTransactionType());
							}
							else {

								if (ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION.equalsIgnoreCase(achFileSubRecord.getReceiverTransactionType())) {
									achFileRecord.setTotalCreditAmount(
											Double.sum(achFileRecord.getTotalCreditAmount(), achFileSubRecord.getAmount())
											);
									noOfCredits++;
								}
								else if (ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT.equalsIgnoreCase(achFileSubRecord.getReceiverTransactionType())) {
									achFileRecord.setTotalDebitAmount(
											Double.sum(achFileRecord.getTotalDebitAmount(), achFileSubRecord.getAmount())
											);
									noOfDebits++;
								}
								achFileSubRecords.add(achFileSubRecord);
							}
							achRecord = achFileBufferReader.readLine();
						}

						if( achFileRecord.getOffsetAccountNumber() == null || achFileRecord.getOffsetAccountNumber().isEmpty() ) {
							alert.prepareError("File doesn't contain offset record. It is not a balanced file. ").log();
							achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12046);
							return achFileDTO;
						}

						if(!_isOffsetAmountValid(achFileRecord)){
							alert.prepareError("Offset Amount is not balanced.").log();
							achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12051);
							return achFileDTO;
						}

						achFileRecord.setSubRecords(achFileSubRecords);
						Double recieverCreditAmount = achFileRecord.getTotalCreditAmount();
						achFileRecord.setTotalCreditAmount(achFileRecord.getTotalDebitAmount());
						achFileRecord.setTotalDebitAmount(recieverCreditAmount);
						
						fileRecords.add(achFileRecord);

						achFileDTO.setCreditAmount(Double.sum(achFileDTO.getCreditAmount(), achFileRecord.getTotalCreditAmount()));
						achFileDTO.setDebitAmount(Double.sum(achFileDTO.getDebitAmount(), achFileRecord.getTotalDebitAmount()));
					}

					achRecord = achFileBufferReader.readLine();
				} 
				
				if(achFileDTO.getDebitAmount() + achFileDTO.getCreditAmount() == 0) {
					alert.prepareError("No debit / credit is associated with the offset account. Please re-check your file.").log();
					achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12112);
					return achFileDTO;
				}
				
				achFileDTO.setFileRecords(fileRecords);
				achFileDTO.setNumberOfDebits(Integer.toString(noOfDebits));
				achFileDTO.setNumberOfCredits(Integer.toString(noOfCredits));
				achFileDTO.setNumberOfRecords(Integer.toString(noOfCredits + noOfDebits));
				
				achFileBufferReader.close();
			} 
			else {
				alert.prepareError("Invalid ACH File").log();
				achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12308);
				return achFileDTO;
			}
		} 
		catch (IOException f) {
			alert.prepareError("IOException in ACHUtil: " + f).log();
			achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12308);
			return achFileDTO;
		}

		return achFileDTO;
	}

	/**
	 * This method is used to validate file name
	 * @param File f - ACH File object
	 * @return boolean - true if valid and false if not valid
	 */
	private static boolean _isFileNameValid(File f) {
		try {
			f.getCanonicalPath();
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	/**
	 * This method will fetch the ACHFileRecordDTO object out of given charStream
	 * @param ruleList
	 * @param charStream
	 * @return
	 */
	private ACHFileRecordDTO _fetchFileRecordDTO(List<ACHFileParsingRule> ruleList, String charStream) {

		ACHFileRecordDTO achfileRecordDTO = new ACHFileRecordDTO();

		for (ACHFileParsingRule rule : ruleList) {

			String ruleName = rule.getRuleName();
			String ruleValue = charStream.substring(rule.getOrigin() - 1, rule.getDest()).trim();

			if (TRANSACTION_TYPE.equals(ruleName)) {
				achfileRecordDTO.setTransactionType(service_code_map.get(ruleValue));
			}
			else if (REQUEST_TYPE.equals(ruleName)) {
				achfileRecordDTO.setRequestType(ruleValue);
			}
			else if (EFFECTIVE_DATE.equals(ruleName)) {
				achfileRecordDTO.setEffectiveDate(ruleValue);
			}
		}

		return achfileRecordDTO;
	}

	/**
	 * This method will fetch the ACHFileSubrecordDTO object out of given charStream
	 * @param ruleList
	 * @param charStream
	 * @return
	 */
	private ACHFileSubrecordDTO _fetchFileSubrecordDTO(List<ACHFileParsingRule> ruleList, String charStream) {

		ACHFileSubrecordDTO achFileSubRecordDTO = new ACHFileSubrecordDTO();

		for (ACHFileParsingRule rule : ruleList) {

			String ruleName = rule.getRuleName();
			String ruleValue = charStream.substring(rule.getOrigin() - 1, rule.getDest()).trim();

			if (ACCOUNT_TYPE.equals(ruleName)) {
				String[] transactionAndAccountType = transaction_code_map.get(ruleValue).split(",");
				achFileSubRecordDTO.setReceiverTransactionType(transactionAndAccountType[0]);
				achFileSubRecordDTO.setReceiverAccountType(transactionAndAccountType[1]);
			} 
			else if (AMOUNT.equals(ruleName)) {
				Double amount = Double.parseDouble(ruleValue.replaceFirst("^0+(?!$)", ""))/100D;
				achFileSubRecordDTO.setAmount(amount);
			}
			else if (ACCOUNT_NUMBER.equals(ruleName)) {
				achFileSubRecordDTO.setReceiverAccountNumber(ruleValue);
			} 
			else if (RECEIVER_NAME.equals(ruleName)) {
				achFileSubRecordDTO.setReceiverName(ruleValue);
			}
		}

		return achFileSubRecordDTO;
	}

	/***
	 * This method validates whether the offset amount is valid with respect to it's subrecords
	 * @param ACHFileRecordDTO fileRecord - contains file record details
	 * @return boolean - true if valid and false if invalid
	 */
	private boolean _isOffsetAmountValid(ACHFileRecordDTO fileRecord) {

		double diffAmount = Math.round((fileRecord.getTotalCreditAmount() - fileRecord.getTotalDebitAmount()) * 100.0) / 100.0;
        String offsetTransactionType = fileRecord.getOffsetTransactionType();
        int isOffsetAmountValid = Double.compare(Math.abs(diffAmount), fileRecord.getOffsetAmount());
        
        //Validating transaction type of offset account 
        if(isOffsetAmountValid == 0) {
        	if( diffAmount == 0 || (diffAmount > 0 && ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT.equalsIgnoreCase(offsetTransactionType)) 
            		|| (diffAmount < 0 && ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION.equalsIgnoreCase(offsetTransactionType)) ){
        		
        		return true;
        	}
        }
        
    	return false;
	}

	/**
	 * fetches the rule set based on the rule
	 * @param rule
	 * @return
	 */
	private List<ACHFileParsingRule> _getRuleSet(String rule) {

		List<ACHFileParsingRule> ruleSet = new ArrayList<ACHFileParsingRule>();

		if (BATCH_HEADER_REACOR.equalsIgnoreCase(rule)) {
			ruleSet = batchRecordRule;
		} 
		else if (PDP_ENTRY_DETAIL_RECORD.equalsIgnoreCase(rule)) {
			return pdpEntityRule;
		} 
		return ruleSet;
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchACHFilesWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr) {
		Set<String> achFileIds = new HashSet<String>();
		List<ApprovalRequestDTO> achFiles = new ArrayList<ApprovalRequestDTO>();
		if(CollectionUtils.isEmpty(requests))
			return achFiles;
		
		for(BBRequestDTO bBRequestDTO : requests) {
			if(StringUtils.isNotBlank(bBRequestDTO.getTransactionId()))
				achFileIds.add(bBRequestDTO.getTransactionId());
		}
		
		String filter = "";
		
		if (!application.getIsStateManagementAvailableFromCache()) {
			filter = "achFile_id" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "achFile_id" + DBPUtilitiesConstants.EQUAL, achFileIds);
			achFiles = fetchACHFilesForApprovalInfo(filter, dcr);
			List<ACHFileFormatTypeDTO> types = getACHFileFormats();
			achFiles = (new FilterDTO()).merge(achFiles, types, "fileTypeId=id", "fileType");
			
			achFiles = (new FilterDTO()).merge(achFiles, requests, "transactionId=transactionId", "requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		else {
			filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, achFileIds);
			achFiles = fetchACHFilesForApprovalInfo(filter,dcr);
			List<ApprovalRequestDTO> backendData = backendDelegate.fetchBackendTransactionsForApproval(achFileIds, dcr);
			if(CollectionUtils.isNotEmpty(backendData)) {
				achFiles = (new FilterDTO()).merge(achFiles, backendData,"confirmationNumber=transactionId", "");
			}
			List<ACHFileFormatTypeDTO> types = getACHFileFormats();
			achFiles = (new FilterDTO()).merge(achFiles, types, "fileTypeId=id", "fileType");
			
			achFiles = (new FilterDTO()).merge(achFiles, requests, "confirmationNumber=transactionId", "transactionId,requestId,status,amIApprover,amICreator,requiredApprovals,receivedApprovals,actedByMeAlready,featureActionId");
		}
		

		return achFiles;
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchACHFilesForApprovalInfo(String filter, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> achFiles = new ArrayList<ApprovalRequestDTO>();
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String achFileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			
			JSONObject responseObj = new JSONObject(achFileResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			achFiles = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch ACH files : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching ACH files: ", e).log();
		}

		return achFiles;
	}

	@Override
	public ACHFileDTO createPendingTransaction(ACHFileDTO input, DataControllerRequest request) {
		if (!application.getIsStateManagementAvailableFromCache()) {
			input.setReferenceID(Constants.REFERENCE_KEY + input.getAchFile_id());
			return input;
		}
		input.setAchFile_id(null);
		return backendDelegate.createPendingTransaction(input, request);
	
	}

	@Override
	public ACHFileDTO approveTransaction(String referenceId, DataControllerRequest request) {

		if (!application.getIsStateManagementAvailableFromCache()) {
			ACHFileDTO backendObj = new ACHFileDTO();
			ACHFileDTO dbxObj = fetchTransactionById(referenceId, request);
			backendObj = dbxObj;
			String confirmationNumber = dbxObj.getConfirmationNumber();
			
			if(!StringUtils.isEmpty(confirmationNumber) && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
				backendObj.setAchFile_id(dbxObj.getConfirmationNumber());
				return backendDelegate.editTransaction(backendObj, request);
			}
			else {
				return backendDelegate.createTransactionWithoutApproval(backendObj, request);
			}
		}
		updateStatus(referenceId, TransactionStatusEnum.APPROVED.getStatus(), "Transaction Approved");
		return backendDelegate.approveTransaction(referenceId, request);
	}

	@Override
	public ACHFileDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request) {
		
		if(!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), "Transaction Rejected");
		}
		
		updateStatus(referenceId, TransactionStatusEnum.REJECTED.getStatus(), "Transaction Rejected");
		return backendDelegate.rejectTransaction(referenceId, transactionType, request);
	}

	@Override
	public ACHFileDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request) {
		if (!application.getIsStateManagementAvailableFromCache()) {
			return updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), "Transaction Withdrawn");
		}
		updateStatus(referenceId, TransactionStatusEnum.WITHDRAWN.getStatus(), "Transaction Withdrawn");
		return backendDelegate.withdrawTransaction(referenceId, transactionType, request);
	}

	@Override
	public ACHFileDTO fetchTransactionById(String referenceId, DataControllerRequest request) {
		if (!application.getIsStateManagementAvailableFromCache()) {
			return fetchACHFileEntry(referenceId);
		}
		
		ACHFileDTO backendData = backendDelegate.fetchTransactionById(referenceId, request);
		ACHFileDTO dbxData = fetchExecutedTranscationEntry(referenceId, null, null);
		
		return (new FilterDTO()).merge(Arrays.asList(dbxData),Arrays.asList(backendData), "confirmationNumber=achFile_id", "").get(0);
	}
	
	@Override
	public ACHFileDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby) {
		
		List<ACHFileDTO> transactionDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_GET;
		
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
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), ACHFileDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the ach file",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the ach file",exp).log();
			return null;
		}
		
		if(transactionDTO != null && transactionDTO.size() != 0)
			return transactionDTO.get(0);
		
		return null;
	}
}

/** class to maintain parsing Rule parameters
 * 
 */
class ACHFileParsingRule {

	private int origin;
	private int dest;
	private String ruleName;

	public ACHFileParsingRule(int origin, int dest, String ruleName) {
		this.dest = dest;
		this.origin = origin;
		this.ruleName = ruleName;
	}

	public int getOrigin() {
		return origin;
	}

	public int getDest() {
		return dest;
	}

	public String getRuleName() {
		return ruleName;
	}
}
