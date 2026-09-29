package com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentPOBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;

public class BulkPaymentRecordBackendDelegateImpl implements BulkPaymentRecordBackendDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public BulkPaymentRecordDTO fetchBulkPaymentRecordDetailsById(String recordId, DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.FETCH_BULKPAYMENT_RECORDDETAILS_BY_ID;
		String response;
		BulkPaymentRecordDTO record;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		requestParameters.put("recordId", recordId);
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
            record = JSONUtils.parse(responseObj.toString(), BulkPaymentRecordDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Error in fetching Bulk Payment record", e).log();			
			return null;
		} 
		catch (Exception e) {
			alert.prepareError("Error occured in fetching Bulk Payment record", e).log();	
			return null;
		}
		return record;
	}
	
	@Override
	public BulkPaymentRecordDTO createBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteBulkPaymentRecord(String bulkPaymentRecordId) {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

	@Override
	public BulkPaymentRecordDTO updateBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BulkPaymentRecordDTO> fetchOnGoingPayments(Set<String> fromAccounts, String batchMode, String fromDate, String toDate, DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.GETONGOINGPAYMENTS;
		String response;
		List<BulkPaymentRecordDTO> records;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		requestParameters.put("batchMode", batchMode);
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            records = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentRecordDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Error in Downloading OnGoing Bulk Payments", e).log();			
			return null;
		} 
		catch (Exception e) {
			alert.prepareError("Error occured in fetchOnGoingBulkPayments", e).log();			
			return null;
		}
		return records;
	}

	@Override
	public BulkPaymentRecordDTO initiateBulkPayment(BulkPaymentRecordDTO bulkPaymentRecordDTO, DataControllerRequest dcr) {
		
		BulkPaymentPOBackendDelegate bulkPaymentPOBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentPOBackendDelegate.class);
		try {
			bulkPaymentPOBackendDelegate.approvePaymentOrders(bulkPaymentRecordDTO.getRecordId(),dcr);
		}catch(Exception e){
			alert.prepareError("Error in approving bulk payment orders", e).log();
		}
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.INITIATE_BULKPAYMENT;
		BulkPaymentRecordDTO recordDTO;
		String response;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", bulkPaymentRecordDTO.getConfirmationNumber());
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
			recordDTO = JSONUtils.parse(responseObj.toString(), BulkPaymentRecordDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Error in fetching Bulk Payments history", e).log();			
			return null;
		} 
		catch (Exception e) {
			alert.prepareError("Error occured in fetchBulkPaymentHistory", e).log();			
			return null;
		}
		return recordDTO;
	}

    @Override
	public List<BulkPaymentRecordDTO> fetchBulkPaymentHistory(Set<String> fromAccounts, String fromDate, String toDate, DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.FETCHHISTORY;
		String response;
		List<BulkPaymentRecordDTO> historyRecords;

		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(null).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            historyRecords = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentRecordDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Error in fetching Bulk Payments history", e).log();			
			return null;
		} 
		catch (Exception e) {
			alert.prepareError("Error occured in fetchBulkPaymentHistory", e).log();			
			return null;
		}
		return historyRecords;
	}

	@Override
	public BulkPaymentRecordDTO cancelBulkPaymentRecord(String bulkPaymentRecordId, String comments, 
			String cancellationreason,String cancellationReasonId, String statusCode, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.CANCELBULKPAYMENTRECORD;
		String response = null;
		BulkPaymentRecordDTO bulkPaymentRecordDTO = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", bulkPaymentRecordId);
		requestParameters.put("comments", comments);
		requestParameters.put("cancellationreason", cancellationreason);
		requestParameters.put("statusCode", statusCode);

		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject record = new JSONObject(response);			
			bulkPaymentRecordDTO = JSONUtils.parse(record.toString(), BulkPaymentRecordDTO.class);
			
		} catch (JSONException e) {
			alert.prepareError("Error in cancelling Bulk Payments history", e).log(); 
			return null;
		}catch (Exception e) {
			alert.prepareError("Error occured in cancelBulkPaymentRecord", e).log();			
			return null;
		}
	
		return bulkPaymentRecordDTO;
	}

	@Override
	public BulkPaymentRecordDTO updateBulkPaymentRecord(String recordId, String fromAccount, String description, DataControllerRequest request) {
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.UPDATEBULKPAYMENTRECORD;
		String response = null;
		BulkPaymentRecordDTO bulkPaymentRecordDTO = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("fromAccount", fromAccount);
		requestParameters.put("description", description);
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			JSONObject record = new JSONObject(response);			
			bulkPaymentRecordDTO = JSONUtils.parse(record.toString(), BulkPaymentRecordDTO.class);
			
		} catch (JSONException e) {
			alert.prepareError("Error in updating Bulk Payments history", e).log(); 
			return null;
		}catch (Exception e) {
			alert.prepareError("Error occured in updateBulkPaymentRecord", e).log();			
			return null;
		}
	
		return bulkPaymentRecordDTO;
	}

	@Override
	public List<BulkPaymentRecordDTO> fetchRecords(Set<String> recordIdList, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSORCH;
		String operationName = OperationName.FETCHRECORDS;

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("recordId", String.join(",", recordIdList));
		inputParams.put("loop_count", String.valueOf(recordIdList.size()));
		List<BulkPaymentRecordDTO> output = new ArrayList<BulkPaymentRecordDTO>();
		
		try {
			String createResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap())
					.withDataControllerRequest(dcr)
					.build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			output = JSONUtils.parseAsList(responseArray.toString(), BulkPaymentRecordDTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception at " + operationName + " entry: " + e).log();
			return null;
		}
		return output;
	}
	
	@Override
	public BulkPaymentRecordDTO updateBulkPaymentRecordStatus(String recordId, String status, DataControllerRequest request) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.UPDATEBULKPAYMENTRECORDSTATUS;
		String response = null;
		BulkPaymentRecordDTO bulkPaymentRecordDTO = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("status", status);		
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			JSONObject record = new JSONObject(response);			
			bulkPaymentRecordDTO = JSONUtils.parse(record.toString(), BulkPaymentRecordDTO.class);
			
		} catch (JSONException e) {
			alert.prepareError("Error while updating the status of Bulk Payment record", e).log(); 
			return null;
		}catch (Exception e) {
			alert.prepareError("Error while updating the status of Bulk Payment record", e).log();			
			return null;
		}
	
		return bulkPaymentRecordDTO;
	}

	@Override
	public BulkPaymentRecordDTO rejectBulkPaymentRecord(String recordId, String comments, String rejectionreason, DataControllerRequest request) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.REJECTBULKPAYMENTRECORD;
		String response = null;
		BulkPaymentRecordDTO bulkPaymentRecordDTO = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("recordId", recordId);
		requestParameters.put("comments", comments);
		requestParameters.put("rejectionreason", rejectionreason);
						
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			JSONObject record = new JSONObject(response);			
			bulkPaymentRecordDTO = JSONUtils.parse(record.toString(), BulkPaymentRecordDTO.class);
			
		} catch (JSONException e) {
			alert.prepareError("Error while updating the status of Bulk Payment record", e).log(); 
			return null;
		}catch (Exception e) {
			alert.prepareError("Error while updating the status of Bulk Payment record", e).log();			
			return null;
		}
	
		return bulkPaymentRecordDTO;
	}

}
