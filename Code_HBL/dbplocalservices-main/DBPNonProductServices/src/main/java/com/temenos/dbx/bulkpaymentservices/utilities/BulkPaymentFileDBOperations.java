package com.temenos.dbx.bulkpaymentservices.utilities;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;


public class BulkPaymentFileDBOperations {

	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	BulkPaymentRecordDBOperations BulkPaymentRecordDBOperations = new BulkPaymentRecordDBOperations();
	BulkPaymentPODBOperations BulkPaymentPODBOperations = new BulkPaymentPODBOperations();

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");
	public static final String DB_BULKPAYMENT_FILE_CREATE = SCHEMA_NAME + "_bulkpaymentfilesmock_create";
	public static final String DB_BULKPAYMENT_FILE_GET = SCHEMA_NAME + "_bulkpaymentfilesmock_get";
	public static final String DB_BULKPAYMENT_FILE_DELETE = SCHEMA_NAME + "_bulkpaymentfilesmock_delete";
	public static final String DB_BULKPAYMENT_FILE_UPDATE = SCHEMA_NAME + "_bulkpaymentfilesmock_update";

	public BulkPaymentFileDTO createBulkPaymentFileEntry(BulkPaymentFileDTO inputFileDTO) {

		BulkPaymentFileDTO outputFileDTO = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILE_CREATE;
		Map<String, Object> requestParameters;

		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(inputFileDTO).toString(), String.class, Object.class);
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params", e).log();			
			return null;
		}

		String uploadResponse = null;
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			uploadResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject response = new JSONObject(uploadResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			outputFileDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentFileDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Upload Bulk Payment File", e).log();			
			return null;
		} catch (Exception e) {
			alert.prepareError("Unable to store the file at Infinity", e).log();			
			return null;
		}

		//BulkpaymentRecord Creation 
		BulkPaymentRecordDTO outputRecordDTO = null;
		if(outputFileDTO != null) {
			BulkPaymentRecordDTO inputRecord = inputFileDTO.getBulkPaymentRecord();
			if(inputRecord != null) {
				String recordId = String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp());
				inputRecord.setFileId(outputFileDTO.getFileId());
				inputRecord.setRecordId(recordId);
				inputRecord.setCompanyId(inputFileDTO.getCompanyId());
				inputRecord.setCreatedby(inputFileDTO.getUploadedBy());
				inputRecord.setStatus(TransactionStatusEnum.CREATED.getStatus());
				inputRecord.setFileName(inputFileDTO.getFileName());
				outputRecordDTO = BulkPaymentRecordDBOperations.createBulkPaymentRecordEntry(inputRecord);
			}
		} else{
			alert.prepareError("Error occured while uploading Bulk Payment File").log();			
			return null;
		}

		//Bulk Payment PO Creation
		if(outputRecordDTO != null) {
			List<BulkPaymentPODTO> paymentOrders = inputFileDTO.getPaymentOrders();
			if(paymentOrders != null) {
				for(int i=0; i < paymentOrders.size(); i++) {
					paymentOrders.get(i).setRecordId(outputRecordDTO.getRecordId());
					paymentOrders.get(i).setPaymentOrderId(String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp()));
					paymentOrders.get(i).setCompanyId(inputFileDTO.getCompanyId());
					paymentOrders.get(i).setCreatedby(inputFileDTO.getUploadedBy());
					paymentOrders.get(i).setStatus(TransactionStatusEnum.READY_FOR_EXECUTION.getStatus());
					BulkPaymentPODTO paymentOrder = new BulkPaymentPODTO();
					paymentOrder = BulkPaymentPODBOperations.createBulkPaymentPOEntry(paymentOrders.get(i));
					if(paymentOrder == null) {
						alert.prepareError("BulkPayment file dto is null. Error occured while uploading Bulk Payment File").log();
						updateBulkPaymentFileStatus(outputFileDTO.getFileId(),"Upload Failed");
						break;
					}
				}
			}
			
			updateBulkPaymentFileStatus(outputFileDTO.getFileId(),"Processed");
			updateBulkPaymentFileConfirmationNumber(outputFileDTO.getFileId(),outputRecordDTO.getRecordId());
			
		} else {
			alert.prepareError("Error occured while uploading Bulk Payment File").log();
			updateBulkPaymentFileStatus(outputFileDTO.getFileId(),"Upload Failed");
			return null;
		}		

		return outputFileDTO;
	}

	public boolean deleteBulkPaymentFileEntry(String fileId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILE_DELETE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("fileId", fileId);

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
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while deleting the bulk payments file",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while deleting the bulk payments file",exp).log();
			return false;
		}		
		return false;	
	}

	public BulkPaymentFileDTO updateBulkPaymentFileStatus(String fileId, String status) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILE_UPDATE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("fileId", fileId);
		requestParams.put("status", status);

		BulkPaymentFileDTO bulkPaymentFileDTO = null;		
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
			bulkPaymentFileDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentFileDTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to update bulk payment file status", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at while updating bulk payment file", e).log();
			return null;
		}
		return bulkPaymentFileDTO;
	}
	
	public BulkPaymentFileDTO updateBulkPaymentFileConfirmationNumber(String fileId, String confirmationNumber) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILE_UPDATE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("fileId", fileId);
		requestParams.put("confirmationNumber", confirmationNumber);

		BulkPaymentFileDTO bulkPaymentFileDTO = null;		
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
			bulkPaymentFileDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentFileDTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to update bulk payment file confirmation Number", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at while updating bulk payment file", e).log();
			return null;
		}
		return bulkPaymentFileDTO;
	}
	
	public Result fetchUploadedFiles(List<String> companyIds, String customerId){
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENT_FILE_GET;
		Result result;
		String filter = "";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		
		if (CollectionUtils.isNotEmpty(companyIds))
			filter = DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;

		if(customerId != null && !customerId.isEmpty()) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.OR;
			}
			filter = filter + "uploadedBy" + DBPUtilitiesConstants.EQUAL + customerId;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			result = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResult();
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch bulk payment uploaded files", je).log();
			return ErrorCodeEnum.ERR_21219.setErrorCode(new Result());
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchUploadedFiles: ", e).log();
			return ErrorCodeEnum.ERR_21219.setErrorCode(new Result());
		}

		return result;
	}
}
