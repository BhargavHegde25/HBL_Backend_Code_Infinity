package com.temenos.infinity.api.chequemanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.CreateChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class CreateChequeBookBackendDelegateImpl implements CreateChequeBookBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public ChequeBook createChequeBookOrder(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException {

        ChequeBook chequeBookOrder = new ChequeBook();

        // Load Check Book Request properties
        Properties props = ChequeManagementProperties.loadProps(ChequeManagementConstants.PARAM_PROPERTY);

        // Validations before create order
        String accountId = "";
        String chequeIssueId = "";
        accountId = chequeBook.getAccountID() != "" ? chequeBook.getAccountID() : "";
        chequeIssueId = chequeBook.getChequeIssueId() != "" ? chequeBook.getChequeIssueId() : "";

        JSONObject requestBody = new JSONObject();
        requestBody.put("chequeIssueId", chequeIssueId);
        requestBody.put("note", chequeBook.getNote());
        
        if (StringUtils.isNotBlank(chequeBook.getNumberOfLeaves())) {
			requestBody.put("numberOfLeaves", chequeBook.getNumberOfLeaves());
		}
        if (StringUtils.isNotBlank(chequeBook.getNumberOfChequeBooks())) {
			requestBody.put("numberOfChequeBooks", chequeBook.getNumberOfChequeBooks());
		}
        if (StringUtils.isNotBlank(chequeBook.getDeliveryType())) {
			requestBody.put("deliveryType", chequeBook.getDeliveryType());
		}
        if (StringUtils.isNotBlank(chequeBook.getAddress())) {
			requestBody.put("address", chequeBook.getAddress());
		}
        if (StringUtils.isNotBlank(chequeBook.getFees())) {
			requestBody.put("fees", chequeBook.getFees());
		}
        
        // requestBody.put("chequeStatus", props.getProperty("chequeStatus"));

        // Set Input Parameters for create Order service
        String  requestbody = requestBody.toString().replaceAll("\"", "'");
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("templateName", props.getProperty("chequeBookTemplateName"));
        inputMap.put("type", props.getProperty("chequeBookType"));
        inputMap.put("subtype", props.getProperty("chequeBookSubType"));
        inputMap.put("accountId", accountId);
        inputMap.put("requestBody", requestbody);
        inputMap.put("signatoryApprovalRequired",chequeBook.getSignatoryApprovalRequired());
        alert.prepareError("OMS Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        // Making a call to order request API
        String chequeBookResponse = null;
        JSONObject Response = new JSONObject();
       try {
            chequeBookResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
        	 request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Unable to create cheque book request order " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26011);
        }

        if (StringUtils.isNotBlank(chequeBookResponse)) {
            alert.prepareError("OMS Response " + chequeBookResponse).log();
            Response = new JSONObject(chequeBookResponse);
        }

        if (Response.has(ChequeManagementConstants.PARAM_ORDER_ID)
                && StringUtils.isNotBlank(Response.getString(ChequeManagementConstants.PARAM_ORDER_ID))) {

            chequeBookOrder.setChequeIssueId(Response.getString(ChequeManagementConstants.PARAM_ORDER_ID));
            chequeBookOrder.setStatus(Response.getString(ChequeManagementConstants.PARAM_ORDER_STATUS));
            request.addRequestParam_("backendEndId", chequeBookOrder.getChequeIssueId());
            request.addRequestParam_("isSrmsFailed", "false");
        }else {
        	 request.addRequestParam_("isSrmsFailed", "true");
        }
        
        if(Response.has("dbpErrMsg")&&StringUtils.isNotBlank(Response.getString("dbpErrMsg")))
        	chequeBookOrder.setMessage(Response.getString("dbpErrMsg"));
        return chequeBookOrder;
    }

    @Override
    public ChequeBook validateChequeBookOrder(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException {

        ChequeBook chequeBookOrder = new ChequeBook();

        // Validations before create order
        String chequeIssueId = "";
        chequeIssueId = chequeBook.getChequeIssueId() != "" ? chequeBook.getChequeIssueId() : "";

        // Input map
        HashMap<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("chequeIssueId", chequeIssueId);
        inputMap.put("note", chequeBook.getNote());
        inputMap.put("validate", chequeBook.getValidate());

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        // Making a call to transact cheque book request API
        String chequeBookResponse = null;
        JSONObject Response = new JSONObject();
        try {
            chequeBookResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.T24TRANSACT_CREATECHEQUEBOOK.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.T24TRANSACT_CREATECHEQUEBOOK.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Error occured while validating cheque book request " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26015);
        }

        if (StringUtils.isNotBlank(chequeBookResponse)) {
            alert.prepareError("Transact cheque book Response " + chequeBookResponse).log();
            Response = new JSONObject(chequeBookResponse);
        }

        if (Response.has("chequeIssueId") && StringUtils.isNotBlank(Response.getString("chequeIssueId"))) {

            chequeBookOrder.setChequeIssueId(Response.getString("chequeIssueId"));
            chequeBookOrder.setFees(Response.getString("fees"));
            if (Response.has("note") && StringUtils.isNotBlank(Response.getString("note"))) {
                chequeBookOrder.setNote(Response.getString("note"));
            }
            if (Response.has("recordVersion") && StringUtils.isNotBlank(Response.getString("recordVersion"))) {
                chequeBookOrder.setRecordVersion(Response.getString("recordVersion"));
            }

		} else {
			if (Response.has("dbpErrCode"))
				chequeBookOrder.setCode(Response.getString("dbpErrCode"));
			if (Response.has("dbpErrMsg") || Response.has("errorDetails")) {
				if (Response.has("errorDetails")) {
					String errorDetail = Response.getString("errorDetails");
					alert.prepareError("Validate cheque book errrorDetails " + errorDetail).log();
					JSONArray errorDetails = new JSONArray(errorDetail);
					JSONObject errorDetailObj = errorDetails.getJSONObject(0);
					String id="";
					String message="";
					String errMsg=Response.getString("dbpErrMsg");
					if(errorDetailObj.has("id")) {
					 id = errorDetailObj.getString("id");
					}
					if(errorDetailObj.has("message")) {
					 message = errorDetailObj.getString("message");
					}
					if(StringUtils.isNotBlank(id)) {
						errMsg=errMsg + " with id " + id;
					}
				    if (StringUtils.isNotBlank(message)) {
				    	errMsg=errMsg + " with message " + message;
					}
					chequeBookOrder.setMessage(errMsg);
				} else {
					chequeBookOrder.setMessage(Response.getString("dbpErrMsg"));
				}
			}
		}

        return chequeBookOrder;
    }

}
