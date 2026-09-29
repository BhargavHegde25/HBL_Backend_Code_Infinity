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
import com.temenos.dbx.product.transactionservices.backenddelegate.api.OwnAccountFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferDTO;

public class OwnAccountFundTransferBackendDelegateImpl implements OwnAccountFundTransferBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	@Override
	public OwnAccountFundTransferDTO createTransactionWithoutApproval(OwnAccountFundTransferBackendDTO ownaccountfundtransferbackenddto, DataControllerRequest request) {

		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;//"T24PaymentsJavaServices2";//
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_WITHOUT_APPROVER;//"T24TransferToOwnAccountsWithoutApprover";
		//T24CreateTransferWithoutApprover
		String createResponse = null;
		OwnAccountFundTransferDTO ownaccountfundtransferdto = null;

		Map<String, Object> requestParameters;
		try {
			ownaccountfundtransferbackenddto.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(ownaccountfundtransferbackenddto).toString(),
					String.class, Object.class);
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
			ownaccountfundtransferdto = JSONUtils.parse(createResponse, OwnAccountFundTransferDTO.class);
			if (ownaccountfundtransferdto.getTransactionId() != null
					&& !"".equals(ownaccountfundtransferdto.getTransactionId())) {
				ownaccountfundtransferdto.setReferenceId(ownaccountfundtransferdto.getTransactionId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create ownaccount transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create ownaccount transaction: " , e).log();
			return null;
		}

		return ownaccountfundtransferdto;
	}

	@Override
	public OwnAccountFundTransferDTO editTransactionWithoutApproval(OwnAccountFundTransferBackendDTO ownaccountTransactionBackendDTO, DataControllerRequest request) {

		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_EDIT_WITHOUT_APPROVER;

		String createResponse = null;
		OwnAccountFundTransferDTO ownaccountTransactionDTO = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(ownaccountTransactionBackendDTO).toString(),
					String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}
		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request).build().getResponse();
			JSONObject editResponse = new JSONObject(createResponse);
			if (editResponse.has(Constants.OPSTATUS) && editResponse.getInt(Constants.OPSTATUS) == 0
					&& (editResponse.has("transactionId") || editResponse.has("referenceId"))) {
				if (editResponse.has("transactionId") && !"".equals(editResponse.getString("transactionId"))) {
					editResponse.put("referenceId", editResponse.getString("transactionId"));
				}
			}
			if (editResponse != null)
				ownaccountTransactionDTO = JSONUtils.parse(editResponse.toString(), OwnAccountFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to edit ownaccount transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at edit ownaccount transaction: " , e).log();
			return null;
		}

		return ownaccountTransactionDTO;
	}

	@Override
	public OwnAccountFundTransferDTO deleteTransactionWithoutApproval(String transactionId, String transactionType,	String frequencyType, DataControllerRequest dataControllerRequest) {

		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_DELETE_WITHOUT_APPROVER;

		String deleteResponse = null;
		OwnAccountFundTransferDTO ownaccountDTO = new OwnAccountFundTransferDTO();
		ownaccountDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("transactionId", transactionId);
		requestParameters.put("transactionType", transactionType);
		requestParameters.put("frequencyType", frequencyType);

		try {

			deleteResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(dataControllerRequest.getHeaderMap())
					.withDataControllerRequest(dataControllerRequest).build().getResponse();

			JSONObject deleteResponseObj = new JSONObject(deleteResponse);
			if (deleteResponseObj.has(Constants.OPSTATUS) && deleteResponseObj.getInt(Constants.OPSTATUS) == 0
					&& (deleteResponseObj.has("transactionId") || deleteResponseObj.has("referenceId"))) {
				if (deleteResponseObj.has("transactionId")
						&& !"".equals(deleteResponseObj.getString("transactionId"))) {
					deleteResponseObj.put("referenceId", deleteResponseObj.getString("transactionId"));
				}
			}
			if (deleteResponseObj != null)
				ownaccountDTO = JSONUtils.parse(deleteResponseObj.toString(), OwnAccountFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to delete own account transaction: " , e).log();
			return ownaccountDTO;
		} catch (Exception e) {
			alert.prepareError("Caught exception at delete own account transaction: " , e).log();
			return ownaccountDTO;
		}

		return ownaccountDTO;
	}

	@Override
	public OwnAccountFundTransferDTO cancelTransactionWithoutApproval(String transactionId,	DataControllerRequest dataControllerRequest) {

		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_CANCEL_OCCURRENCE_WITHOUT_APPROVER;

		String cancelResponse = null;
		OwnAccountFundTransferDTO ownaccountDTO = new OwnAccountFundTransferDTO();
		ownaccountDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND);

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("transactionId", transactionId);

		try {

			cancelResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(dataControllerRequest.getHeaderMap())
					.withDataControllerRequest(dataControllerRequest).build().getResponse();

			JSONObject cancelResponseObj = new JSONObject(cancelResponse);
			if (cancelResponseObj.has(Constants.OPSTATUS) && cancelResponseObj.getInt(Constants.OPSTATUS) == 0
					&& (cancelResponseObj.has("transactionId") || cancelResponseObj.has("referenceId"))) {
				if (cancelResponseObj.has("transactionId")
						&& !"".equals(cancelResponseObj.getString("transactionId"))) {
					cancelResponseObj.put("referenceId", cancelResponseObj.getString("transactionId"));
				}
			}
			if (cancelResponseObj != null)
				ownaccountDTO = JSONUtils.parse(cancelResponseObj.toString(), OwnAccountFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to cancel own account fund transaction occurrence: " , e).log();
			return ownaccountDTO;
		} catch (Exception e) {
			alert.prepareError("Caught exception at cancel own account fund transaction occurrence: " , e).log();
			return ownaccountDTO;
		}

		return ownaccountDTO;
	}

	@Override
	public OwnAccountFundTransferDTO createPendingTransaction(OwnAccountFundTransferBackendDTO ownaccountBackendDTO, DataControllerRequest request) {

		OwnAccountFundTransferDTO ownaccountfundtransferdto = null;
		
		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_WITH_APPROVER;

		String createResponse = null;
		Map<String, Object> requestParameters;
		try {
			ownaccountBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(ownaccountBackendDTO).toString(), String.class, Object.class);
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
			ownaccountfundtransferdto = JSONUtils.parse(createResponse, OwnAccountFundTransferDTO.class);

			if (StringUtils.isNotEmpty(ownaccountfundtransferdto.getReferenceId())) {
				ownaccountfundtransferdto.setTransactionId(ownaccountfundtransferdto.getReferenceId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create ownaccount transaction with pending status: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create ownaccount transaction with pending status: " , e).log();
			return null;
		}

		return ownaccountfundtransferdto;
	}

	@Override
	public OwnAccountFundTransferDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			OwnAccountFundTransferDTO ownaccountTransactionDTO = null;
			OwnAccountFundTransferBackendDTO backendObj = new OwnAccountFundTransferBackendDTO();
	
			backendObj.setTransactionId(referenceId);
			backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
	
			ownaccountTransactionDTO = updateTransactionStatus(backendObj, request);
	
			return ownaccountTransactionDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE;
			String operationName = OperationName.APPROVE_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequency);
			try {
				String approveresponse = DBPServiceExecutorBuilder.builder()
											.withServiceId(serviceName)
											.withObjectId(null)
											.withOperationId(operationName)
											.withRequestParameters(requestParameters)
											.withRequestHeaders(request.getHeaderMap())
											.withDataControllerRequest(request)
											.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(approveresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
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
				String rejectresponse = DBPServiceExecutorBuilder.builder()
											.withServiceId(serviceName)
											.withObjectId(null)
											.withOperationId(operationName)
											.withRequestParameters(requestParameters)
											.withRequestHeaders(request.getHeaderMap())
											.withDataControllerRequest(request)
											.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(rejectresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
		
	}

	@Override
	public OwnAccountFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			return deleteTransactionWithoutApproval(referenceId, transactionType, frequency, request);
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.WITHDRAW_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("transactionType", transactionType);
			requestParameters.put("frequencyType", frequency);
			try {
				String approveresponse = DBPServiceExecutorBuilder.builder()
											.withServiceId(serviceName)
											.withObjectId(null)
											.withOperationId(operationName)
											.withRequestParameters(requestParameters)
											.withRequestHeaders(request.getHeaderMap())
											.withDataControllerRequest(request)
											.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(approveresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}
	
	@Override
	public OwnAccountFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest request) {

		List<OwnAccountFundTransferDTO> ownaccountTransactionDTO = null;
		
		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_GET;

		String response = null;
		Map<String, Object> requestParameters;
		try {
			OwnAccountFundTransferBackendDTO backendObj = new OwnAccountFundTransferBackendDTO();
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
			ownaccountTransactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), OwnAccountFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create ownaccount transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create ownaccount transaction: " , e).log();
			return null;
		}
		if(ownaccountTransactionDTO != null && ownaccountTransactionDTO.size() != 0)
			return ownaccountTransactionDTO.get(0);
		
		return null;
	}
	
	private OwnAccountFundTransferDTO updateTransactionStatus(OwnAccountFundTransferBackendDTO input, DataControllerRequest request) {

		String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_UPDATE_STATUS;

		String updateStatus = null;
		OwnAccountFundTransferDTO ownaccountTransactionDTO = null;

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
				ownaccountTransactionDTO = JSONUtils.parse(editResponse.toString(), OwnAccountFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of ownaccount transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of ownaccount transaction: " , e).log();
			return null;
		}

		return ownaccountTransactionDTO;
	}
	
	@Override
	public OwnAccountFundTransferDTO validateTransaction(OwnAccountFundTransferBackendDTO input, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			OwnAccountFundTransferDTO output = new OwnAccountFundTransferDTO();
			try {
				output = JSONUtils.parse(new JSONObject(input).toString(), OwnAccountFundTransferDTO.class);
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
				return JSONUtils.parse(response, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException | DBPApplicationException e) {
				alert.prepareError("Failed to validate own account fund transfer: ", e).log();
				return null;
			}
		}
	}

	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> ownAccountFundTransferIds,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_OWNACCOUNTTRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();
		
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, ownAccountFundTransferIds);
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try 
		{
			String ownAccountFundTransferResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();

			JSONObject responseObj = new JSONObject(ownAccountFundTransferResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			transactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
			transactions.forEach((transaction) ->{
				transaction.setTransactionId(transaction.getConfirmationNumber());
			});
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch OwnAccountFundsTransactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching OwnAccountFundsTransactions: ", e).log();
		}
		
		return transactions;
	}
	
	@Override
	public OwnAccountFundTransferDTO editTransactionWithApproval(OwnAccountFundTransferBackendDTO backendObj, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			OwnAccountFundTransferDTO ownaccountTransactionDTO = null;
			backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
	
			ownaccountTransactionDTO = updateTransactionStatus(backendObj, request);
	
			return ownaccountTransactionDTO;
		}
		else {
			String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
			String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_EDIT_WITH_APPROVER;
			Map<String, Object> requestParameters;
			try {
				requestParameters = JSONUtils.parseAsMap(new JSONObject(backendObj).toString(), String.class, Object.class);
			} catch (IOException e) {
				alert.prepareError("Error occured while fetching the input params: ", e).log();
				return null;
			}
			try {
				String cancelresponse = DBPServiceExecutorBuilder.builder()
											.withServiceId(serviceName)
											.withObjectId(null)
											.withOperationId(operationName)
											.withRequestParameters(requestParameters)
											.withRequestHeaders(request.getHeaderMap())
											.withDataControllerRequest(request)
											.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(cancelresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to create pending own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to create pending own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO cancelTransactionWithApproval(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			ownAccountFundTransferDTO = new OwnAccountFundTransferDTO();
			ownAccountFundTransferDTO.setReferenceId(referenceId);
			return ownAccountFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
			String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_CANCEL_OCCURRENCE_WITH_APPROVER;
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
				ownAccountFundTransferDTO =  JSONUtils.parse(cancelresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
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
				ownAccountFundTransferDTO =  JSONUtils.parse(approveresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to approve cancellation own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			ownAccountFundTransferDTO = new OwnAccountFundTransferDTO();
			ownAccountFundTransferDTO.setReferenceId(referenceId);
			return ownAccountFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.REJECT_TRANSACTION; //operationName to be changed
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
				ownAccountFundTransferDTO =  JSONUtils.parse(withdrawresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			ownAccountFundTransferDTO = new OwnAccountFundTransferDTO();
			ownAccountFundTransferDTO.setReferenceId(referenceId);
			return ownAccountFundTransferDTO;
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
				ownAccountFundTransferDTO =  JSONUtils.parse(withdrawresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO deleteTransactionWithApproval(String referenceId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			ownAccountFundTransferDTO = new OwnAccountFundTransferDTO();
			ownAccountFundTransferDTO.setReferenceId(referenceId);
			return ownAccountFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE;
			String operationName = OperationName.TRANSFER_TO_OWN_ACCOUNTS_BACKEND_DELETE_WITH_APPROVER;
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequencyType);
			requestParameters.put("transactionType", transactionType);
			try {
				String deleteresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(dataControllerRequest.getHeaderMap())
						.withDataControllerRequest(dataControllerRequest)
						.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(deleteresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
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
				String rejectresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(rejectresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO rejectDeletion(String referenceId, String frequencyType, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			ownAccountFundTransferDTO = new OwnAccountFundTransferDTO();
			ownAccountFundTransferDTO.setReferenceId(referenceId);
			return ownAccountFundTransferDTO;
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
				ownAccountFundTransferDTO =  JSONUtils.parse(rejectresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;
	}

	@Override
	public OwnAccountFundTransferDTO withdrawDeletion(String referenceId, String frequencyType, DataControllerRequest request) {
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		OwnAccountFundTransferDTO ownAccountFundTransferDTO = null;
		if(PAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			ownAccountFundTransferDTO = new OwnAccountFundTransferDTO();
			ownAccountFundTransferDTO.setReferenceId(referenceId);
			return ownAccountFundTransferDTO;
		}
		else {
			String serviceName = ServiceId.APPROVAL_TRANSACTION_SERVICE; //serviceName to be changed
			String operationName = OperationName.WITHDRAW_TRANSACTION; //operationName to be changed
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("referenceId", referenceId);
			requestParameters.put("frequencyType", frequencyType);
			try {
				String withdrawresponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParameters)
						.withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request)
						.build().getResponse();
				ownAccountFundTransferDTO =  JSONUtils.parse(withdrawresponse, OwnAccountFundTransferDTO.class);
			}
			catch (JSONException | IOException e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Failed to cancel own account fund transfer: ", e).log();
				return null;
			}
		}
		return ownAccountFundTransferDTO;		
	}
}