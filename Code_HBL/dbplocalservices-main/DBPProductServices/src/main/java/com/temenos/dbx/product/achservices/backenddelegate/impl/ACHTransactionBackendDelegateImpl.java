package com.temenos.dbx.product.achservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
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

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHTransactionBackendDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class ACHTransactionBackendDelegateImpl implements ACHTransactionBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	@Override
	public ACHTransactionDTO createTransactionWithoutApproval(ACHTransactionDTO transactionDTO, DataControllerRequest dcr) {

		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = null;

		Map<String, Object> inputParams = null;
		JSONObject transactionObj = new JSONObject(transactionDTO);
		try {
			transactionDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			inputParams = JSONUtils.parseAsMap(new JSONObject(transactionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Failed to extract inputParams from transactionDTO", e).log();
			return null;
		}
		ACHCommonsBusinessDelegate bbTransactionTypesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
		String transactionType = bbTransactionTypesBusinessDelegate
				.getTransactionTypeById((int) transactionDTO.getTransactionType_id());
		;

		if (transactionType.equals(ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT)) {
			operationName = OperationName.CREATE_ACH_PAYMENT_WITH_VENDOR;
		} else if (transactionType.equals(ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION)) {
			operationName = OperationName.CREATE_ACH_COLLECTION_WITH_VENDOR;
		} else {
			alert.prepareError("Failed to fetch transactionType").log();
			return null;
		}

		String createResponse = null;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONObject responseObj = new JSONObject(transactionObj, JSONObject.getNames(transactionObj));
			for (String key : JSONObject.getNames(response)) {
				responseObj.put(key, response.get(key));
			}
			transactionDTO = JSONUtils.parse(responseObj.toString(), ACHTransactionDTO.class);
			if (StringUtils.isNotEmpty(transactionDTO.getTransaction_id())) {
				transactionDTO.setReferenceID(transactionDTO.getTransaction_id());
			}
			return transactionDTO;
		} catch (JSONException e) {
			alert.prepareError("Failed to create entry into vendor service: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAchVendorServiceResponse entry: " , e).log();
			return null;
		}

	}

	@Override
	public ACHTransactionDTO editTransaction(ACHTransactionDTO transactionDTO, DataControllerRequest dcr) {

		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = null;

		Map<String, Object> inputParams = null;

		try {
			inputParams = JSONUtils.parseAsMap(new JSONObject(transactionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Failed to extract inputParams from transactionDTO", e).log();
			return null;
		}

		Long transactionType = transactionDTO.getTransactionType_id();

		if (transactionType==2) {
			operationName = OperationName.CREATE_ACH_PAYMENT_WITH_VENDOR;
		} else if (transactionType==1) {
			operationName = OperationName.CREATE_ACH_COLLECTION_WITH_VENDOR;
		} else {
			alert.prepareError("Failed to fetch transactionType").log();
			return null;
		}

		String createResponse = null;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			transactionDTO = JSONUtils.parse(response.toString(), ACHTransactionDTO.class);
			return transactionDTO;
		} catch (JSONException e) {
			alert.prepareError("Failed to create entry into vendor service: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAchVendorServiceResponse entry: " , e).log();
			return null;
		}
	}

	@Override
	public List<ACHTransactionDTO> fetchCollectionStatus(List<String> confirmationNumbers, DataControllerRequest dcr) {
		String serviceName = ServiceId.ACH_ORCH_LOB;
		String operationName = OperationName.FETCH_ACH_ORCH_COLLECTION_STATUS;

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("confirmationNumber", String.join(",", confirmationNumbers));
		inputParams.put("loop_count", String.valueOf(confirmationNumbers.size()));
		List<ACHTransactionDTO> output = new ArrayList<ACHTransactionDTO>();

		try {
			String createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			output = JSONUtils.parseAsList(responseArray.toString(), ACHTransactionDTO.class);

		} catch (Exception e) {
			alert.prepareError("Caught exception at " + OperationName.FETCH_ACH_ORCH_COLLECTION_STATUS + " entry: " , e).log();
		}
		return output;
	}

	public List<ACHTransactionDTO> fetchPaymentStatus(List<String> confirmationNumbers, DataControllerRequest dcr) {
		String serviceName = ServiceId.ACH_ORCH_LOB;
		String operationName = OperationName.FETCH_ACH_ORCH_PAYMENT_STATUS;

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("confirmationNumber", String.join(",", confirmationNumbers));
		inputParams.put("loop_count", String.valueOf(confirmationNumbers.size()));
		List<ACHTransactionDTO> output = new ArrayList<ACHTransactionDTO>();

		try {
			String createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			output = JSONUtils.parseAsList(responseArray.toString(), ACHTransactionDTO.class);

		} catch (Exception e) {
			alert.prepareError("Caught exception at " + OperationName.FETCH_ACH_ORCH_PAYMENT_STATUS + " entry: " , e).log();
		}
		return output;
	}
	
	@Override
	public ACHTransactionDTO createPendingTransaction(ACHTransactionDTO input, DataControllerRequest request) {

		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = null;

		Map<String, Object> inputParams = null;
		JSONObject transactionObj = new JSONObject(input);
		try {
			input.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			inputParams = JSONUtils.parseAsMap(new JSONObject(input).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Failed to extract inputParams from transactionDTO", e).log();
			return null;
		}
		ACHCommonsBusinessDelegate bbTransactionTypesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
		String transactionType = bbTransactionTypesBusinessDelegate
				.getTransactionTypeById((int) input.getTransactionType_id());
		;

		if (transactionType.equals(ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT)) {
			operationName = OperationName.CREATE_ACH_PAYMENT_WITH_VENDOR;
		} else if (transactionType.equals(ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION)) {
			operationName = OperationName.CREATE_ACH_COLLECTION_WITH_VENDOR;
		} else {
			alert.prepareError("Failed to fetch transactionType").log();
			return null;
		}

		String createResponse = null;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONObject responseObj = new JSONObject(transactionObj, JSONObject.getNames(transactionObj));
			for (String key : JSONObject.getNames(response)) {
				responseObj.put(key, response.get(key));
			}
			input = JSONUtils.parse(responseObj.toString(), ACHTransactionDTO.class);
			if (StringUtils.isNotEmpty(input.getReferenceID())) {
				input.setTransaction_id(input.getReferenceID());
			}
			return input;
		} catch (JSONException e) {
			alert.prepareError("Failed to create entry into vendor service: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAchVendorServiceResponse entry: " , e).log();
			return null;
		}
	}

	@Override
	public ACHTransactionDTO approveTransaction(String referenceId, DataControllerRequest request) {

		ACHTransactionDTO achTransactionDTO = new ACHTransactionDTO();
		achTransactionDTO.setTransaction_id(referenceId);
		achTransactionDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);

		achTransactionDTO = moveTheTransactionFromPendingToLive(achTransactionDTO, request);

		return achTransactionDTO;
	}

	@Override
	public ACHTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request) {
		
		return deleteTransaction(referenceId, transactionType, request);
	}

	@Override
	public ACHTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest) {

		return  deleteTransaction(referenceId, transactionType, dataControllerRequest);
	}
	
	@Override
	public ACHTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest request) {
		
		List<ACHTransactionDTO> transactionDTO = null;
		ACHTransactionDTO transaction = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_GET;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + referenceId;
		
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray trJsonArray = jsonRsponse.getJSONArray(ACHConstants.ACH_TRANSACTION);
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), ACHTransactionDTO.class);
			if(transactionDTO != null && transactionDTO.size() != 0) {
				transaction = transactionDTO.get(0);
				transaction.setTransaction_id(transaction.getConfirmationNumber());
				return transaction;
			}
			
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the ach transactions",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the ach transactions",exp).log();
			return null;
		}
		
		return null;
	}
	
	private ACHTransactionDTO moveTheTransactionFromPendingToLive(ACHTransactionDTO input, DataControllerRequest request) {
		/*
		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.ACH_TRANSFER_BACKEND_UPDATE_STATUS;

		String updateStatus = null;
		ACHTransactionDTO achTransactionDTO = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(input).toString(),
					String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
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
				achTransactionDTO = JSONUtils.parse(editResponse.toString(), ACHTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of ach transaction: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of ach transaction: " + e).log();
			return null;
		}
		
		return achTransactionDTO;
		*/
		input.setReferenceID(input.getTransaction_id());
		return input;
	}
	
	@Override
	public ACHTransactionDTO validateTransaction(ACHTransactionDTO input, DataControllerRequest request) {
		
		return input;
	}
	
	@Override
	public ACHTransactionDTO deleteTransaction(String transactionId, String transactionType,
			DataControllerRequest dataControllerRequest) {

		/*
		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.ACH_TRANSFER_BACKEND_DELETE;
		
		String deleteResponse = null;
		ACHTransactionDTO achTransactionDTO = new ACHTransactionDTO();
		achTransactionDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);
		
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
				achTransactionDTO = JSONUtils.parse(deleteResponseObj.toString(), ACHTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete ach transaction: " + e).log();
			return achTransactionDTO;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at delete ach transaction: " + e).log();
			return achTransactionDTO;
		}
		
		return achTransactionDTO;
		*/
		return new ACHTransactionDTO();
	}

	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> achTransactionIds,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TRANSACTION_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> achTransactions = new ArrayList<ApprovalRequestDTO>();
		
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, achTransactionIds);
		
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
			
			achTransactions.forEach((transaction) ->{
				transaction.setTransactionId(transaction.getConfirmationNumber());
			});
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch ACH Transactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching ACH Transactions: ", e).log();
		}

		return achTransactions;
	}
}
