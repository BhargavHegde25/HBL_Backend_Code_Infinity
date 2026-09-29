package com.temenos.dbx.product.transactionservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.BillPayTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;

/**
 * 
 * @author KH2624
 * @version 1.0
 * implements {@link BillPayTransactionBackendDelegate}
 */

public class BillPayTransactionBackendDelegateImpl implements BillPayTransactionBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	public  BillPayTransactionDTO createTransactionWithoutApproval(BillPayTransactionBackendDTO billpayTransactionBackendDTO, DataControllerRequest request) {

		String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAYTRANSFER_BACKEND;

		String createResponse = null;
		BillPayTransactionDTO billpayTransactionDTO = null;
		
		Map<String, Object> requestParameters;
		try {
			billpayTransactionBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			//Modifying the status if it is a post dated scheduled transaction
			if(StringUtils.isNotBlank(billpayTransactionBackendDTO.getIsScheduled()) && (billpayTransactionBackendDTO.getIsScheduled().equals("true")||billpayTransactionBackendDTO.getIsScheduled().equals("1"))){
				String scheduledDt = billpayTransactionBackendDTO.getScheduledDate();
		        Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
		        if (scheduledDate.after(new Date())) {
		        	billpayTransactionBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
		        }		        
			}							                 
			
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billpayTransactionBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			billpayTransactionDTO = JSONUtils.parse(createResponse, BillPayTransactionDTO.class);
			if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
				billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId());
			}									
			
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create billpay transaction: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create billpay transaction: ", e).log();
			return null;
		}
		
		return billpayTransactionDTO;
	}

	@Override
	public BillPayTransactionDTO editTransactionWithoutApproval(BillPayTransactionBackendDTO billpayTransactionBackendDTO,
			DataControllerRequest request) {

		String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAYTRANSFER_BACKEND_EDIT;

		String createResponse = null;
		BillPayTransactionDTO billpayTransactionDTO = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billpayTransactionBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			JSONObject editResponse = new JSONObject(createResponse);
			if(editResponse.has(Constants.OPSTATUS) && editResponse.getInt(Constants.OPSTATUS) ==0 && (editResponse.has("transactionId") || editResponse.has("referenceId"))) {
				if(editResponse.has("transactionId") && !"".equals(editResponse.getString("transactionId"))) {
					editResponse.put("referenceId", editResponse.getString("transactionId"));
				}
			}
			if(editResponse != null) 
				billpayTransactionDTO = JSONUtils.parse(editResponse.toString(), BillPayTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit billpay transaction: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at edit billpay transaction: ", e).log();
			return null;
		}
		
		return billpayTransactionDTO;
	}

	@Override
	public BillPayTransactionDTO deleteTransaction(String transactionId, String transactionType,
			DataControllerRequest dataControllerRequest) {

			String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
			String operationName = OperationName.BILLPAYTRANSFER_BACKEND_DELETE;

			String deleteResponse = null;
			BillPayTransactionDTO billpayTransactionDTO = new BillPayTransactionDTO();
			billpayTransactionDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);
			
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId", transactionId);
			requestParameters.put("transactionType", transactionType);
			
			try {
				
				deleteResponse = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(requestParameters).
						withRequestHeaders(dataControllerRequest.getHeaderMap()).
						withDataControllerRequest(dataControllerRequest).
						build().getResponse();
				
				JSONObject deleteResponseObj = new JSONObject(deleteResponse);
				if(deleteResponseObj.has(Constants.OPSTATUS) && deleteResponseObj.getInt(Constants.OPSTATUS) ==0 && (deleteResponseObj.has("transactionId") || deleteResponseObj.has("referenceId"))) {
					if(deleteResponseObj.has("transactionId") && !"".equals(deleteResponseObj.getString("transactionId"))) {
						deleteResponseObj.put("referenceId", deleteResponseObj.getString("transactionId"));
					}
				}
				if(deleteResponseObj != null) 
					billpayTransactionDTO = JSONUtils.parse(deleteResponseObj.toString(), BillPayTransactionDTO.class);
			}
			catch (JSONException e) {
				alert.prepareError("Failed to delete billpay transaction: ", e).log();
				return billpayTransactionDTO;
			}
			catch (Exception e) {
				alert.prepareError("Caught exception at delete billpay transaction: ", e).log();
				return billpayTransactionDTO;
			}
			
			return billpayTransactionDTO;
		
	}

	@Override
	public BillPayTransactionDTO cancelTransactionOccurrence(String transactionId,
			DataControllerRequest dataControllerRequest) {

			String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
			String operationName = OperationName.BILLPAYTRANSFER_BACKEND_CANCEL_OCCURRENCE;

			String cancelResponse = null;
			BillPayTransactionDTO billpayTransactionDTO = new BillPayTransactionDTO();
			billpayTransactionDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND);
			
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId", transactionId);
			
			try {
				
				cancelResponse = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(requestParameters).
						withRequestHeaders(dataControllerRequest.getHeaderMap()).
						withDataControllerRequest(dataControllerRequest).
						build().getResponse();
				
				JSONObject cancelResponseObj = new JSONObject(cancelResponse);
				if(cancelResponseObj.has(Constants.OPSTATUS) && cancelResponseObj.getInt(Constants.OPSTATUS) ==0 && (cancelResponseObj.has("transactionId") || cancelResponseObj.has("referenceId"))) {
					if(cancelResponseObj.has("transactionId") && !"".equals(cancelResponseObj.getString("transactionId"))) {
						cancelResponseObj.put("referenceId", cancelResponseObj.getString("transactionId"));
					}
				}
				if(cancelResponseObj != null)
					billpayTransactionDTO = JSONUtils.parse(cancelResponseObj.toString(), BillPayTransactionDTO.class);
			}
			catch (JSONException e) {
				alert.prepareError("Failed to cancel billpay transaction occurrence: ", e).log();
				return billpayTransactionDTO;
			}
			catch (Exception e) {
				alert.prepareError("Caught exception at cancel billpay transaction occurrence: ", e).log();
				return billpayTransactionDTO;
			}
			
			return billpayTransactionDTO;
	}
	
	@Override
	public BillPayTransactionDTO createPendingTransaction(BillPayTransactionBackendDTO billpayBackendDTO, DataControllerRequest request) {

		BillPayTransactionDTO billpayTransactionDTO = null;
		
		String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAYTRANSFER_BACKEND;

		String createResponse = null;
		Map<String, Object> requestParameters;
		try {
			billpayBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billpayBackendDTO).toString(), String.class, Object.class);

		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}

		try {
			createResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request)
					.build().getResponse();
			billpayTransactionDTO = JSONUtils.parse(createResponse, BillPayTransactionDTO.class);

			if (StringUtils.isNotEmpty(billpayTransactionDTO.getReferenceId())) {
				billpayTransactionDTO.setTransactionId(billpayTransactionDTO.getReferenceId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create billpay transaction with pending status: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create billpay transaction with pending status: ", e).log();
			return null;
		}

		return billpayTransactionDTO;
	}

	@Override
	public BillPayTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {

		BillPayTransactionDTO billpayTransactionDTO = null;
		BillPayTransactionBackendDTO backendObj = new BillPayTransactionBackendDTO();

		backendObj.setTransactionId(referenceId);
		
		BillPayTransactionDTO transactionDTO = fetchTransactionById(referenceId, request);
		if(StringUtils.isNotBlank(transactionDTO.getIsScheduled()) && (transactionDTO.getIsScheduled().equals("true")||transactionDTO.getIsScheduled().equals("1"))){
			String scheduledDt = transactionDTO.getScheduledDate();
	        Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
	        if (scheduledDate.after(new Date())) {
	        	backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
	        }
		}
		else {
			backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
		}
		billpayTransactionDTO = updateTransactionStatus(backendObj, request);

		return billpayTransactionDTO;
	}

	@Override
	public BillPayTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency) {
		return deleteTransaction(referenceId, transactionType, request);
	}

	@Override
	public BillPayTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest, String frequency) {
		return deleteTransaction(referenceId, transactionType, dataControllerRequest);
	}
	
	@Override
	public BillPayTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest request) {

		List<BillPayTransactionDTO> billpayTransactionDTO = null;
				
		String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAY_TRANSFER_BACKEND_GET;

		String response = null;
		Map<String, Object> requestParameters;
		try {
			BillPayTransactionBackendDTO backendObj = new BillPayTransactionBackendDTO();
			backendObj.setTransactionId(referenceId);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(backendObj).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}

		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request)
					.build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			JSONArray trJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			billpayTransactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), BillPayTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create billpay transaction: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create billpay transaction: ", e).log();
			return null;
		}

		if(billpayTransactionDTO != null && billpayTransactionDTO.size() != 0)
			return billpayTransactionDTO.get(0);
		
		return null;
	}
	
	private BillPayTransactionDTO updateTransactionStatus(BillPayTransactionBackendDTO input, DataControllerRequest request) {

		String serviceName = ServiceId.BILL_PAY_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAY_TRANSFER_BACKEND_UPDATE_STATUS;

		String updateStatus = null;
		BillPayTransactionDTO billpayTransactionDTO = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(input).toString(),
					String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		try {
			updateStatus = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request)
					.build().getResponse();
			JSONObject editResponse = new JSONObject(updateStatus);

			if (editResponse != null)
				billpayTransactionDTO = JSONUtils.parse(editResponse.toString(), BillPayTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of billpay transaction: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of bilpay transaction: ", e).log();
			return null;
		}

		return billpayTransactionDTO;
	}
	
	@Override
	public BillPayTransactionDTO validateTransaction(BillPayTransactionBackendDTO input, DataControllerRequest request) {
		BillPayTransactionDTO output = new BillPayTransactionDTO();
		try {
			output = JSONUtils.parse(new JSONObject(input).toString(), BillPayTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception at converting backenddto to dbxdto. ", e).log();
		}
		return output;
	}

	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> billPayTransIds,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BILLPAYTRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> billPayTransactions = new ArrayList<ApprovalRequestDTO>();

		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, billPayTransIds);
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			String billPayResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(billPayResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			billPayTransactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
			billPayTransactions.forEach((transaction) ->{
				transaction.setTransactionId(transaction.getConfirmationNumber());
			});
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch BillPayTransactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching BillPayTransactions: ", e).log();
		}
		
		return billPayTransactions;
	}
	
	@Override
	public BillPayTransactionDTO editTransactionWithApproval(BillPayTransactionBackendDTO backendObj, DataControllerRequest request) {

		BillPayTransactionDTO billPayTransactionDTO = null;
		backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);

		billPayTransactionDTO = updateTransactionStatus(backendObj, request);

		return billPayTransactionDTO;
	}
	
}