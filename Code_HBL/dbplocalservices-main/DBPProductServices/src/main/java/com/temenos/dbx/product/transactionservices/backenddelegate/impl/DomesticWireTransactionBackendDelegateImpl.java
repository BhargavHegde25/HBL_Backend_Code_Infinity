package com.temenos.dbx.product.transactionservices.backenddelegate.impl;

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

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.DomesticWireTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.DomesticWireTransactionBusinessDelegateImpl;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

/**
 * 
 * @author KH2624
 * @version 1.0
 * implements {@link DomesticWireTransactionBackendDelegate}
 */

public class DomesticWireTransactionBackendDelegateImpl implements DomesticWireTransactionBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public WireTransactionDTO createTransactionWithoutApproval(WireTransactionBackendDTO wireTransactionBackendDTO,
			DataControllerRequest request) {
		
		String serviceName = ServiceId.DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.DOMESTIC_WIRE_TRANSFER_BACKEND;

		String createResponse = null;
		WireTransactionDTO wireTransactionDTO = null;
		
		Map<String, Object> requestParameters;
		try {
			wireTransactionBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(wireTransactionBackendDTO).toString(), String.class, Object.class);
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
			wireTransactionDTO = JSONUtils.parse(createResponse, WireTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create wire transaction: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create wire transaction: ", e).log();
			return null;
		}
		
		return wireTransactionDTO;
	}

	@Override
	public WireTransactionDTO createPendingTransaction(WireTransactionBackendDTO input, DataControllerRequest request) {

		WireTransactionDTO wireTransferdto = null;
		
		String serviceName = ServiceId.DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.DOMESTIC_WIRE_TRANSFER_BACKEND;

		String createResponse = null;
		Map<String, Object> requestParameters;
		try {			
			input.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(input).toString(), String.class, Object.class);

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
			wireTransferdto = JSONUtils.parse(createResponse, WireTransactionDTO.class);

			if (StringUtils.isNotEmpty(wireTransferdto.getReferenceId())) {
				wireTransferdto.setTransactionId(wireTransferdto.getReferenceId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create wire transaction with pending status: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create wire transaction with pending status: ", e).log();
			return null;
		}

		return wireTransferdto;
	}

	@Override
	public WireTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {

		WireTransactionDTO wireTransactionDTO = null;
		WireTransactionBackendDTO backendObj = new WireTransactionBackendDTO();

		backendObj.setTransactionId(referenceId);
		backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);

		wireTransactionDTO = moveTheTransactionFromPendingToLive(backendObj, request);

		return wireTransactionDTO;
	}

	@Override
	public WireTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency) {
		return deleteTransaction(referenceId, transactionType, request);
	}

	@Override
	public WireTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest, String frequency) {
		return deleteTransaction(referenceId, transactionType, dataControllerRequest);
	}
	
	
	@Override
	public WireTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest request) {

		List<WireTransactionDTO> wireTransactionDTO = null;
		
		String serviceName = ServiceId.DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.DOMESTIC_WIRE_TRANSFER_BACKEND_GET;

		String response = null;
		Map<String, Object> requestParameters;
		try {
			WireTransactionBackendDTO backendObj = new WireTransactionBackendDTO();
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
			wireTransactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), WireTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create wire transaction: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create wire transaction: ", e).log();
			return null;
		}

		if(wireTransactionDTO != null && wireTransactionDTO.size() != 0)
			return wireTransactionDTO.get(0);
		
		return null;
	}
	
	private WireTransactionDTO moveTheTransactionFromPendingToLive(WireTransactionBackendDTO input, DataControllerRequest request) {

		String serviceName = ServiceId.DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.DOMESTIC_WIRE_TRANSFER_BACKEND_UPDATE_STATUS;
		
		String updateStatus = null;
		WireTransactionDTO wireTransactionDTO = null;

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
				wireTransactionDTO = JSONUtils.parse(editResponse.toString(), WireTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of wire transaction: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of wire transaction: ", e).log();
			return null;
		}

		return wireTransactionDTO;
	}
	
	@Override
	public WireTransactionDTO validateTransaction(WireTransactionBackendDTO input, DataControllerRequest request) {
		WireTransactionDTO output = new WireTransactionDTO();
		try {
			output = JSONUtils.parse(new JSONObject(input).toString(), WireTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception at converting backenddto to dbxdto. ", e).log();
		}
		return output;
	}
	
	@Override
	public WireTransactionDTO deleteTransaction(String transactionId, String transactionType,
			DataControllerRequest dataControllerRequest) {

			String serviceName = ServiceId.DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE;
			String operationName = OperationName.DOMESTIC_WIRE_TRANSFER_BACKEND_DELETE;

			String deleteResponse = null;
			WireTransactionDTO wireTransactionDTO = new WireTransactionDTO();
			wireTransactionDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);
			
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
					wireTransactionDTO = JSONUtils.parse(deleteResponseObj.toString(), WireTransactionDTO.class);
			}
			catch (JSONException e) {
				alert.prepareError("Failed to delete domestic wire transaction: ", e).log();
				return wireTransactionDTO;
			}
			catch (Exception e) {
				alert.prepareError("Caught exception at delete domestic wire transaction: ", e).log();
				return wireTransactionDTO;
			}
			
			return wireTransactionDTO;
	}
	
	@Override
	public WireTransactionDTO editTransaction(WireTransactionBackendDTO wireTransactionBackendDTO,
			DataControllerRequest request) {

		String serviceName = ServiceId.DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.DOMESTIC_WIRE_TRANSFER_BACKEND_EDIT;

		String createResponse = null;
		WireTransactionDTO wireTransactionDTO = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(wireTransactionBackendDTO).toString(), String.class, Object.class);
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
				wireTransactionDTO = JSONUtils.parse(editResponse.toString(), WireTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit domestic wire transaction: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at edit domestic wire transaction: ", e).log();
			return null;
		}
		
		return wireTransactionDTO;
	}

	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> wireTransIds,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_WIRETRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();

		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, wireTransIds);
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
			
			transactions.forEach((transaction) ->{
				transaction.setTransactionId(transaction.getConfirmationNumber());
			});
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch wireTransactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching wireTransactions: ", e).log();
		}
		
		return transactions;
	}

}