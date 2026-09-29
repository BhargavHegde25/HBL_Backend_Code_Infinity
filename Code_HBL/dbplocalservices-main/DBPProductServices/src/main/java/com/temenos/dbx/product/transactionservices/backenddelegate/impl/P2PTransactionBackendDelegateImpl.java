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
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.P2PTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;

public class P2PTransactionBackendDelegateImpl implements P2PTransactionBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);

	@Override
	public P2PTransactionDTO createTransactionWithoutApproval(P2PTransactionBackendDTO p2pTransactionBackendDTO, DataControllerRequest request) {

		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND;

		String createResponse = null;
		P2PTransactionDTO p2pTransactionDTO = null;

		Map<String, Object> requestParameters;
		try {
			p2pTransactionBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pTransactionBackendDTO).toString(), String.class,
					Object.class);
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
			p2pTransactionDTO = JSONUtils.parse(createResponse, P2PTransactionDTO.class);
			if (p2pTransactionDTO.getTransactionId() != null && !"".equals(p2pTransactionDTO.getTransactionId())) {
				p2pTransactionDTO.setReferenceId(p2pTransactionDTO.getTransactionId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create p2p transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create p2p transaction: " , e).log();
			return null;
		}

		return p2pTransactionDTO;
	}
	
	@Override
	public P2PTransactionDTO editTransactionWithoutApproval(P2PTransactionBackendDTO p2pTransactionBackendDTO, DataControllerRequest request) {

		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND_EDIT;

		String createResponse = null;
		P2PTransactionDTO p2pTransactionDTO = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pTransactionBackendDTO).toString(), String.class, Object.class);
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
				p2pTransactionDTO = JSONUtils.parse(editResponse.toString(), P2PTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit p2p transaction: " , e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at edit p2p transaction: " , e).log();
			return null;
		}
		
		return p2pTransactionDTO;
	}
	
	@Override
	public P2PTransactionDTO deleteTransaction( String transactionId, String transactionType, DataControllerRequest dataControllerRequest) {

		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND_DELETE;

		String deleteResponse = null;
		P2PTransactionDTO p2pDTO = new P2PTransactionDTO();
		p2pDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);
		
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
				p2pDTO = JSONUtils.parse(deleteResponseObj.toString(), P2PTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete p2p transaction: " , e).log();
			return p2pDTO;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at delete p2p transaction: " , e).log();
			return p2pDTO;
		}
		
		return p2pDTO;
	}
	
	@Override
	public P2PTransactionDTO cancelTransactionOccurrence( String transactionId, DataControllerRequest dataControllerRequest) {

		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND_CANCEL_OCCURRENCE;

		String cancelResponse = null;
		P2PTransactionDTO p2pDTO = new P2PTransactionDTO();
		p2pDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND);
		
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
				p2pDTO = JSONUtils.parse(cancelResponseObj.toString(), P2PTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to cancel p2p transaction occurrence: " , e).log();
			return p2pDTO;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at cancel p2p transaction occurrence: " , e).log();
			return p2pDTO;
		}
		
		return p2pDTO;
	}
	
	@Override
	public  P2PTransactionDTO createPendingTransaction(P2PTransactionBackendDTO backendObj, DataControllerRequest request) {

		P2PTransactionDTO p2pTransferObj = null;
	
		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND;

		String createResponse = null;
		Map<String, Object> requestParameters;
		try {
			backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(backendObj).toString(), String.class, Object.class);

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
			p2pTransferObj = JSONUtils.parse(createResponse, P2PTransactionDTO.class);

			if (StringUtils.isNotEmpty(p2pTransferObj.getReferenceId())) {
				p2pTransferObj.setTransactionId(p2pTransferObj.getReferenceId());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create p2p transaction with pending status: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create p2p transaction with pending status: " , e).log();
			return null;
		}

		return p2pTransferObj;
	}

	@Override
	public P2PTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency) {

		P2PTransactionDTO p2pTransferObj = null;
		P2PTransactionBackendDTO backendObj = new P2PTransactionBackendDTO();

		backendObj.setTransactionId(referenceId);
		backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);

		p2pTransferObj = updateTransactionStatus(backendObj, request);

		return p2pTransferObj;
	}

	@Override
	public P2PTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency) {

		return  deleteTransaction(referenceId, transactionType, request);
	}

	@Override
	public P2PTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest, String frequency) {

		return  deleteTransaction(referenceId, transactionType, dataControllerRequest);
	}
	
	@Override
	public P2PTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest request) {

		List<P2PTransactionDTO> p2pTransactionDTO = null;
			
		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND_GET;

		String response = null;
		Map<String, Object> requestParameters;
		try {
			P2PTransactionBackendDTO backendObj = new P2PTransactionBackendDTO();
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
			p2pTransactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), P2PTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create p2p transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create p2p transaction: " , e).log();
			return null;
		}
		if(p2pTransactionDTO != null && p2pTransactionDTO.size() != 0)
			return p2pTransactionDTO.get(0);
		
		return null;
	}
	
	private P2PTransactionDTO updateTransactionStatus(P2PTransactionBackendDTO input, DataControllerRequest request) {

		String serviceName = ServiceId.P2P_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2PTRANSFER_BACKEND_UPDATE_STATUS;

		String updateStatus = null;
		P2PTransactionDTO p2pTransactionDTO = null;

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
				p2pTransactionDTO = JSONUtils.parse(editResponse.toString(), P2PTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of p2p transaction: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of p2p transaction: " , e).log();
			return null;
		}

		return p2pTransactionDTO;
	}
	
	@Override
	public P2PTransactionDTO validateTransaction(P2PTransactionBackendDTO input, DataControllerRequest request) {
		P2PTransactionDTO output = new P2PTransactionDTO();
		try {
			output = JSONUtils.parse(new JSONObject(input).toString(), P2PTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception at converting backenddto to dbxdto. ", e).log();
		}
		return output;
	}

	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> p2pTransIds,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PTRANSFERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> transactions = new ArrayList<ApprovalRequestDTO>();

		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, p2pTransIds);
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			String P2PResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(P2PResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			transactions = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
			transactions.forEach((transaction) ->{
				transaction.setTransactionId(transaction.getConfirmationNumber());
			});
			
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch P2PTransactions : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching P2PTransactions: ", e).log();
		}
		
		return transactions;
	}
	
	@Override
	public P2PTransactionDTO editTransactionWithApproval(P2PTransactionBackendDTO backendObj, DataControllerRequest request) {

		P2PTransactionDTO p2pTransactionDTO = null;
		backendObj.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);

		p2pTransactionDTO = updateTransactionStatus(backendObj, request);

		return p2pTransactionDTO;
	}
	
}