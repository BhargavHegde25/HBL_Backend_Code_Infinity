package com.temenos.infinity.api.chequemanagement.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ApproveCheckBookRequestBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.CreateChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.CreateChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.Constants;
import com.temenos.infinity.api.chequemanagement.dto.BBRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookStatusDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class CreateChequeBookBusinessDelegateImpl implements CreateChequeBookBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public ChequeBook createChequeBook(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException {
	    
	    CreateChequeBookBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CreateChequeBookBackendDelegate.class);
	    ChequeBook chequeBookOrder = orderBackendDelegate.createChequeBookOrder(chequeBook, request); 
		return chequeBookOrder; 
	}
	
	@Override
    public ChequeBook validateChequeBook(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException {
        
        CreateChequeBookBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CreateChequeBookBackendDelegate.class);
        ChequeBook chequeBookOrder = orderBackendDelegate.validateChequeBookOrder(chequeBook, request); 
        return chequeBookOrder;
    }

	@Override
	public ChequeBookAction executeChequeBookRequestAfterApproval(ChequeBookAction chequeBookAction, DataControllerRequest request)
			throws ApplicationException {
		ApproveCheckBookRequestBackendDelegate approveBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ApproveCheckBookRequestBackendDelegate.class);
		ChequeBookAction chequeBookOrder = approveBackendDelegate.approveChequeBookRequest(chequeBookAction, request); 
        return chequeBookOrder;
	}
	
	@Override
	public ChequeBookStatusDTO validateForApprovals(ChequeBookStatusDTO chequeBookDTO, DataControllerRequest request) {
		ChequeBookStatusDTO statusDTO = null;
		
		String serviceName = Constants.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = Constants.VALIDATE_FOR_APPROVALS;
        
        String status = chequeBookDTO.getStatus() == null? null : chequeBookDTO.getStatus().toString();
        chequeBookDTO.setStatus(null);
        

        String offsetDetails = chequeBookDTO.getOffsetDetails() == null? null : new JSONObject(chequeBookDTO.getOffsetDetails()).toString();
        chequeBookDTO.setOffsetDetails(null);
        
        Map<String, Object> requestParameters = null;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(chequeBookDTO).toString(), String.class, Object.class);
			requestParameters.put("status", status);
			requestParameters.put("offsetDetails", offsetDetails);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			statusDTO = JSONUtils.parse(jsonRsponse.toString(), ChequeBookStatusDTO.class);
			if(jsonRsponse.has("isSelfApproved") && jsonRsponse.get("isSelfApproved") != null && StringUtils.isNotBlank(jsonRsponse.get("isSelfApproved").toString())) {
				statusDTO.setSelfApproved(Boolean.parseBoolean(jsonRsponse.get("isSelfApproved").toString()));
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}

		return statusDTO;
	}
	
	@Override
	public ChequeBookStatusDTO updateBackendIdInApprovalQueue(String requestId, String transactionId,  boolean isSelfApproved,  String featureActionId, DataControllerRequest request) {
		ChequeBookStatusDTO statusDTO = null;
		
		String serviceName = Constants.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = Constants.UPDATE_BACKENDID_IN_APPROVALQUEUE;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("transactionId", transactionId);
        requestParameters.put("isSelfApproved", isSelfApproved);
        requestParameters.put("featureActionId", featureActionId);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			statusDTO = JSONUtils.parse(jsonRsponse.toString(), ChequeBookStatusDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}

		return statusDTO;
	}
	
	@Override
	public boolean deleteChequeFromApprovalQueue(String requestId) {

		String serviceName = Constants.DBPRBLOCALSERVICEDB;
		String operationName = Constants.DB_BBREQUEST_DELETE;

		String deleteResponse = "";
		Map<String, Object> requestParams = new HashMap<String, Object>();
		try {
			requestParams.put("requestId", requestId);

			deleteResponse = DBPServiceExecutorBuilder.builder().
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
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching actions of a request", exp).log();
			return false;
		}
		return false;
	}

	@Override
	public BBRequestDTO getAccountId(String requestId) {
		BBRequestDTO statusDTO = null;
		
		String serviceName = Constants.DBPRBLOCALSERVICEDB;
        String operationName = Constants.DB_BBREQUEST_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			statusDTO = JSONUtils.parse(jsonRsponse.toString(), BBRequestDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}

		return statusDTO;
	}

}