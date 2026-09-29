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

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InterBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InternationalFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferDTO;

/**
 * 
 * @author KH2624
 * @version 1.0
 * implements {@link InterBankFundTransferBackendDelegate}
 */

public class InternationalFundTransferBackendDelegateImpl implements InternationalFundTransferBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	@Override
	public InternationalFundTransferDTO createTransactionWithoutApproval(
			InternationalFundTransferBackendDTO internationalfundtransferbackenddto, DataControllerRequest request) {

		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_WITHOUT_APPROVER;

		String createResponse = null;
		InternationalFundTransferDTO internationalfundtransferdto = null;
		
		Map<String, Object> requestParameters;
		try {
			internationalfundtransferbackenddto.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(internationalfundtransferbackenddto).toString(), String.class, Object.class);
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
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			internationalfundtransferdto = JSONUtils.parse(createResponse, InternationalFundTransferDTO.class);
			if(internationalfundtransferdto.getTransactionId() != null && !"".equals(internationalfundtransferdto.getTransactionId())) {
				internationalfundtransferdto.setReferenceId(internationalfundtransferdto.getTransactionId());
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create internationalfund transaction: " , e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create internationalfund transaction: " , e).log();
			return null;
		}
		
		return internationalfundtransferdto;
	}

	@Override
	public InternationalFundTransferDTO editTransactionWithoutApproval(InternationalFundTransferBackendDTO internationalTransactionBackendDTO,
			DataControllerRequest request) {

		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_EDIT_WITHOUT_APPROVER;

		String createResponse = null;
		InternationalFundTransferDTO internationalTransactionDTO = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(internationalTransactionBackendDTO).toString(), String.class, Object.class);
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
				internationalTransactionDTO = JSONUtils.parse(editResponse.toString(), InternationalFundTransferDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit international transaction: " , e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at edit international transaction: " , e).log();
			return null;
		}
		
		return internationalTransactionDTO;
	}

	@Override
	public InternationalFundTransferDTO deleteTransactionWithoutApproval(String transactionId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest) {
		
		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_DELETE_WITHOUT_APPROVER;

		String deleteResponse = null;
		InternationalFundTransferDTO internationalDTO = new InternationalFundTransferDTO();
		internationalDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("transactionId", transactionId);
		requestParameters.put("transactionType", transactionType);
		requestParameters.put("frequencyType", frequencyType);
		
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
				internationalDTO = JSONUtils.parse(deleteResponseObj.toString(), InternationalFundTransferDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete international fund transaction: " , e).log();
			return internationalDTO;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at delete international fund transaction: " , e).log();
			return internationalDTO;
		}
		
		return internationalDTO;
	}

	@Override
	public InternationalFundTransferDTO cancelTransactionWithoutApproval(String transactionId, DataControllerRequest dataControllerRequest) {
	
		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITHOUT_APPROVER;

		String cancelResponse = null;
		InternationalFundTransferDTO internationalDTO = new InternationalFundTransferDTO();
		internationalDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND);
		
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
				internationalDTO = JSONUtils.parse(cancelResponseObj.toString(), InternationalFundTransferDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to cancel international fund transaction occurrence: " , e).log();
			return internationalDTO;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at cancel international fund transaction occurrence: " , e).log();
			return null;
		}
		
		return internationalDTO;
	}
	

	@Override
	public InternationalFundTransferDTO createPendingTransaction(InternationalFundTransferBackendDTO internationalBackendDTO, DataControllerRequest request) {

		InternationalFundTransferDTO internationalTransactionDTO = null;

		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_WITH_APPROVER;

		String createResponse = null;
		Map<String, Object> requestParameters;
		try {
			internationalBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(internationalBackendDTO).toString(), String.class, Object.class);
			requestParameters.put("isPending", "1");
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
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
			internationalTransactionDTO = JSONUtils.parse(createResponse, InternationalFundTransferDTO.class);

			if (StringUtils.isNotEmpty(internationalTransactionDTO.getReferenceId())) {
				internationalTransactionDTO.setTransactionId(internationalTransactionDTO.getReferenceId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create international transaction with pending status: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create international transaction with pending status: " , e).log();
			return null;
		}

		return internationalTransactionDTO;
	}

	@Override
	public InternationalFundTransferDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			InternationalFundTransferDTO internationalTransactionDTO = null;
			InternationalFundTransferBackendDTO backendObj = new InternationalFundTransferBackendDTO();
	
			backendObj.setTransactionId(referenceId);
			backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
	
			internationalTransactionDTO = updateTransactionStatus(backendObj, request);
	
			return internationalTransactionDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.APPROVE_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequency);
			try {
				String cancelresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(cancelresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;
	}

	@Override
	public InternationalFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			return deleteTransactionWithoutApproval(referenceId, transactionType, frequency, request);
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.REJECT_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("transactionType", transactionType);
			requestParameters.put("frequencyType", frequency);
			try {
				String cancelresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(cancelresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;
		
	}

	@Override
	public InternationalFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest, String frequency) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			return deleteTransactionWithoutApproval(referenceId, transactionType, frequency, dataControllerRequest);
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.WITHDRAW_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("transactionType", transactionType);
			requestParameters.put("frequencyType", frequency);
			try {
				String cancelresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(dataControllerRequest.getHeaderMap())
						.withDataControllerRequest(dataControllerRequest)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(cancelresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;
	}
	
	@Override
	public InternationalFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest request) {

		List<InternationalFundTransferDTO> internationalTransactionDTO = null;
		
		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_GET;

		String response = null;
		Map<String, Object> requestParameters;
		try {
			InternationalFundTransferBackendDTO backendObj = new InternationalFundTransferBackendDTO();
			backendObj.setTransactionId(referenceId);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(backendObj).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
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
			internationalTransactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), InternationalFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create international transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create international transaction: " , e).log();
			return null;
		}

		if(internationalTransactionDTO != null && internationalTransactionDTO.size() != 0)
			return internationalTransactionDTO.get(0);
		
		return null;
	}
	
	private InternationalFundTransferDTO updateTransactionStatus(InternationalFundTransferBackendDTO input, DataControllerRequest request) {

		String serviceName = ServiceId.INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_UPDATE_STATUS;

		String updateStatus = null;
		InternationalFundTransferDTO internationalTransactionDTO = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(input).toString(),
					String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
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
				internationalTransactionDTO = JSONUtils.parse(editResponse.toString(), InternationalFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of international transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of international transaction: " , e).log();
			return null;
		}

		return internationalTransactionDTO;
	}
	
	@Override
	public InternationalFundTransferDTO validateTransaction(InternationalFundTransferBackendDTO input, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			InternationalFundTransferDTO output = new InternationalFundTransferDTO();
			try {
				output = JSONUtils.parse(new JSONObject(input).toString(), InternationalFundTransferDTO.class);
			} catch (IOException e) {
				alert.prepareError("Caught exception at converting backenddto to dbxdto. ", e).log();
			}
			return output;
		}
		else {
			Map<String, Object> requestParameters;
			try {
				requestParameters = JSONUtils.parseAsMap(new JSONObject(input).toString(), String.class, Object.class);
			} catch (IOException e) {
				alert.prepareError("Error occured while fetching the input params: ", e).log();
				return null;
			}
			try {
				String serviceName = ServiceId.VALIDATE_TRANSACTION_SERVICE;
				String operationName = OperationName.VALIDATE_TRANSACTION;
				requestParameters.put("validate", "true");
				String response = DBPServiceExecutorBuilder.builder()
									.withServiceId(serviceName)
									.withObjectId(null)
									.withOperationId(operationName)
									.withRequestParameters(requestParameters)
									.withRequestHeaders(request.getHeaderMap())
									.withDataControllerRequest(request)
									.build().getResponse();
						
						//TransactionBackendDelegateimpl.validateTransaction(requestParameters, request);
				return JSONUtils.parse(response, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException | DBPApplicationException e) {
				alert.prepareError("Failed to validate own account fund transfer: ", e).log();
				return null;
			}
		}
		
	}

	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> internationalTransIds,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERNATIONALFUNDTRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();
		
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, internationalTransIds);
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String internationalTransactionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(internationalTransactionResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			transactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
			transactions.forEach((transaction) ->{
				transaction.setTransactionId(transaction.getConfirmationNumber());
			});
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch International Transactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching International Transactions: ", e).log();
			return null;
		}
		
		return transactions;
	}
	
	@Override
	public InternationalFundTransferDTO editTransactionWithApproval(InternationalFundTransferBackendDTO backendObj, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			internationalFundTransferDTO = null;
			backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			internationalFundTransferDTO = updateTransactionStatus(backendObj, request);
			return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_EDIT_WITH_APPROVER; //operationName to be changed
			Map<String, Object> requestParameters;
			try {
				requestParameters = JSONUtils.parseAsMap(new JSONObject(backendObj).toString(), String.class, Object.class);
			} catch (IOException e) {
				alert.prepareError("Error occured while fetching the input params: ", e).log();
				return null;
			} 
			try {
				String editresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(editresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to edit international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to edit international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;			
	}

	@Override
	public InternationalFundTransferDTO cancelTransactionWithApproval(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			internationalFundTransferDTO = new InternationalFundTransferDTO();
			internationalFundTransferDTO.setReferenceId(referenceId);
			return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE; //serviceName to be changed
			String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITH_APPROVER; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			try {
				String cancelresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(cancelresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;	
	}

	@Override
	public InternationalFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			return cancelTransactionWithoutApproval(referenceId, request);
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.APPROVE_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", "Once");
			try {
				String approveresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(approveresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to approve international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to approve international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;	
	}

	@Override
	public InternationalFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			internationalFundTransferDTO = new InternationalFundTransferDTO();
			internationalFundTransferDTO.setReferenceId(referenceId);
			return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.REJECT_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", "Once");
			try {
				String rejectresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(rejectresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to reject international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to reject international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;	
	}

	@Override
	public InternationalFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			internationalFundTransferDTO = new InternationalFundTransferDTO();
			internationalFundTransferDTO.setReferenceId(referenceId);
			return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.WITHDRAW_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", "Once");
			try {
				String withdrawresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(withdrawresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to withdraw international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to withdraw international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;		
	}

	@Override
	public InternationalFundTransferDTO deleteTransactionWithApproval(String referenceId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			internationalFundTransferDTO = new InternationalFundTransferDTO();
			internationalFundTransferDTO.setReferenceId(referenceId);
			return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_DELETE_WITH_APPROVER; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequencyType);
			requestParameters.put("transactionType", transactionType);
			try {
				String deleteresponse =DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(dataControllerRequest.getHeaderMap())
						.withDataControllerRequest(dataControllerRequest)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(deleteresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to delete international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to delete international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;		
	}

	@Override
	public InternationalFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			return deleteTransactionWithoutApproval(referenceId, transactionType, frequencyType, request);
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.APPROVE_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequencyType);
			requestParameters.put("transactionType", transactionType);
			try {
				String approveresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(approveresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to approve international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to approve international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;		
	}

	@Override
	public InternationalFundTransferDTO rejectDeletion(String referenceId, String frequencyType, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
		internationalFundTransferDTO = new InternationalFundTransferDTO();
		internationalFundTransferDTO.setReferenceId(referenceId);
		return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.REJECT_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequencyType);
			try {
				String rejectresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(rejectresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to reject international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to reject international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;		
	}

	@Override
	public InternationalFundTransferDTO withdrawDeletion(String referenceId, String frequencyType, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		InternationalFundTransferDTO internationalFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			internationalFundTransferDTO = new InternationalFundTransferDTO();
			internationalFundTransferDTO.setReferenceId(referenceId);
			return internationalFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.WITHDRAW_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequencyType);
			try {
				String getresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				internationalFundTransferDTO =  JSONUtils.parse(getresponse, InternationalFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to withdraw international account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to withdraw international account fund transfer: ", e).log();
				return null;
			}
		}
		return internationalFundTransferDTO;		
	}
	
	@Override
	public List<ApprovalRequestDTO> getBeneBankAddress(List<ApprovalRequestDTO> transactions,
			DataControllerRequest request) {
		String serviceName = ServiceId.T24_IS_PAYMENTS_VIEW;
		String operationName = OperationName.GET_BANK_DETAILS_BY_SWIFTCODE;
		if (StringUtils.isBlank(operationName)) {
			return transactions;
		}
		String swiftCode;
		Map<String, String> bankDetailsMap = new HashMap<String, String>();
		for (ApprovalRequestDTO dto : transactions) {
			swiftCode = dto.getSwiftCode();
			if (StringUtils.isNotBlank(swiftCode)) {
				if (bankDetailsMap.get(swiftCode) != null) {
					dto.setBeneficiaryBankAddress(bankDetailsMap.get(swiftCode));
				} else {
					String bank;
					try {
						Map<String, Object> requestParameters = new HashMap<String, Object>();
						requestParameters.put("SwiftCode", swiftCode);
						alert.prepareError("INPUT to BACKEND: " + new JSONObject(requestParameters).toString()).log();
						bank = DBPServiceExecutorBuilder.builder().
								withServiceId(serviceName).
								withObjectId(null).
								withOperationId(operationName).
								withRequestParameters(requestParameters).
								withRequestHeaders(request.getHeaderMap()).
								withDataControllerRequest(request).build().
								getResponse();
					} catch (Exception e) {
						alert.prepareError("Caught exception in fetching bank details: ", e).log();
						return null;
					}
					JSONObject bankObj = new JSONObject(bank);
					String address = bankObj.getString("bankName") + ", " + bankObj.getString("addrLine1") + ", "
							+ bankObj.getString("addrLine2");
					dto.setBeneficiaryBankAddress(address);
					bankDetailsMap.put(swiftCode, address);
				}
			}
		}
		return transactions;
	}
}