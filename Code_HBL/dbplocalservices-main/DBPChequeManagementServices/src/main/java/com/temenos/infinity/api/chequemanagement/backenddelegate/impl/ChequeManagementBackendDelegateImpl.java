package com.temenos.infinity.api.chequemanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ChequeManagementBackendDelegate;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.ApprovalRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementUtilities;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class ChequeManagementBackendDelegateImpl implements ChequeManagementBackendDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public ChequeBookAction rejectChequeBook(DataControllerRequest request) throws ApplicationException {
		ChequeBookAction chequeBookAction = new ChequeBookAction();

        HashMap<String, Object> inputMap = new HashMap<String, Object>();
        
        if (StringUtils.isNotBlank(request.getParameter("requestId"))) {
        	inputMap.put("serviceRequestId", request.getParameter("requestId"));
		}
        if (StringUtils.isNotBlank(request.getParameter("comments"))) {
			inputMap.put("comments", request.getParameter("comments"));
		}

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));

        // SRMS Call
        String chequeBookResponse = null;
        JSONObject Response = new JSONObject();
        try {
            chequeBookResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVASERVICE_REJECT.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVASERVICE_REJECT.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to reject cheque book request order " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26019);
        }

        if (StringUtils.isNotBlank(chequeBookResponse)) {
            Response = new JSONObject(chequeBookResponse);
        }

        if (Response.has(ChequeManagementConstants.PARAM_REQUEST_ID)
                && StringUtils.isNotBlank(Response.getString(ChequeManagementConstants.PARAM_REQUEST_ID))) {
        	chequeBookAction.setRequestId(Response.getString("requestId"));
        	chequeBookAction.setComments(Response.getString("comments"));
        }
        else {
            if (Response.has("dbpErrCode"))
            	chequeBookAction.setDbpErrCode(Response.getInt("dbpErrCode"));
            if (Response.has("dbpErrMsg"))
            	chequeBookAction.setDbpErrMsg(Response.getString("dbpErrMsg"));
        }
        return chequeBookAction;
	}

	@Override
	public ChequeBookAction withdrawCheque(DataControllerRequest request) throws ApplicationException {
		ChequeBookAction chequeBookAction = new ChequeBookAction();

        // Input map
        HashMap<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("serviceRequestId", request.getParameter("requestId"));
        inputMap.put("comments", request.getParameter("comments"));

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));

        // Making a call to withdraw cheque book request SRMS API
        String response = null;
        JSONObject Response = new JSONObject();
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVASERVICE_WITHDRAW.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVASERVICE_WITHDRAW.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Error occured withdrawing cheque book request " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26016);
        }

        if (StringUtils.isNotBlank(response)) {
            alert.prepareError("Transact cheque book Response " + response).log();
            Response = new JSONObject(response);
        }

        if (Response.has("requestId") && StringUtils.isNotBlank(Response.getString("requestId"))) {

        	chequeBookAction.setRequestId(Response.getString("requestId"));
        	chequeBookAction.setComments(Response.getString("comments"));
        } else {
            if (Response.has("dbpErrCode"))
            	chequeBookAction.setDbpErrCode(Response.getInt("dbpErrCode"));
            if (Response.has("dbpErrMsg"))
            	chequeBookAction.setDbpErrMsg(Response.getString("dbpErrMsg"));
        }

        return chequeBookAction;
	}

	@Override
	public List<ApprovalRequestDTO> fetchChequeDetails(DataControllerRequest request) throws ApplicationException {
		List<ApprovalRequestDTO> chequeBooks = new ArrayList<ApprovalRequestDTO>();
		String  transactionIds= request.getParameter("transactionIds");
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("serviceRequestIds", transactionIds);
        
        try {
			String jResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_GETCHEQUEDETAILS.getServiceName()).
                    withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_GETCHEQUEDETAILS.getOperationName()).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(jResponse);
			JSONArray Orders = ChequeManagementUtilities.getFirstOccuringArray(jsonResponse);
			
			for (int i = 0; i < Orders.length(); i++) {
				ApprovalRequestDTO order = new ApprovalRequestDTO();
	            JSONObject singleOrder = Orders.getJSONObject(i);
	            if (singleOrder.has(ChequeManagementConstants.PARAM_INPUT_PAYLOAD)) {
	                JSONObject payload = singleOrder.getJSONObject(ChequeManagementConstants.PARAM_INPUT_PAYLOAD);
	                String fees = payload.has(ChequeManagementConstants.PARAM_FEES) ? payload.getString(ChequeManagementConstants.PARAM_FEES) : "";
	                String address = payload.has(ChequeManagementConstants.PARAM_ADDRESS) ? payload.getString(ChequeManagementConstants.PARAM_ADDRESS) : "";
	                
	                
	                String numberOfChequeBooks = payload.has(ChequeManagementConstants.PARAM_NUMBEROFCHEQUEBOOKS) ? payload.getString(ChequeManagementConstants.PARAM_NUMBEROFCHEQUEBOOKS) : "";
					
					
					if (StringUtils.isNotBlank(fees)) {
						order.setAmount(fees);
					}
					if (StringUtils.isNotBlank(address)) {
						order.setAddress(address);
					}
					if (StringUtils.isNotBlank(numberOfChequeBooks)) {
						order.setNoOfBooks(numberOfChequeBooks);
					}             
	            }
	            String accountId = singleOrder.has(ChequeManagementConstants.PARAM_ACCOUNTID) ? singleOrder.getString(ChequeManagementConstants.PARAM_ACCOUNTID) : "";
        		if (StringUtils.isNotBlank(accountId)) {
					order.setAccountId(accountId);
				}
        		String transactionId = singleOrder.has(ChequeManagementConstants.PARAM_TRANS_ID) ? singleOrder.getString(ChequeManagementConstants.PARAM_TRANS_ID) : "";
        		if (StringUtils.isNotBlank(transactionId)) {
					order.setTransactionId(transactionId);
				}
        		String partyId = singleOrder.has(ChequeManagementConstants.PARAM_PARTY_ID) ? singleOrder.getString(ChequeManagementConstants.PARAM_PARTY_ID) : "";
        		if (StringUtils.isNotBlank(partyId)) {
					order.setSentBy(partyId);
				}
        		String sentDate = singleOrder.has(ChequeManagementConstants.PARAM_ORDER_PROCESSED_TIME) ? singleOrder.getString(ChequeManagementConstants.PARAM_ORDER_PROCESSED_TIME) : "";
        		if (StringUtils.isNotBlank(sentDate)) {
					order.setSentDate(sentDate);
				}
        		String type = singleOrder.has(ChequeManagementConstants.PARAM_TYPE) ? singleOrder.getString(ChequeManagementConstants.PARAM_TYPE) : "";
        		if (StringUtils.isNotBlank(accountId)) {
        			if("cheque".equalsIgnoreCase(type)) {
        				order.setRequestType("New Request");
        			}else {
        				order.setRequestType(type);
        			}
				}
        		chequeBooks.add(order);
	        }
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the cheque details",exp).log();
			throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26020);
		}
        return chequeBooks;
	}
}
